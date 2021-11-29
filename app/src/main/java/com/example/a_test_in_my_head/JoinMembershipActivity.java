package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class JoinMembershipActivity extends AppCompatActivity {

    private EditText name;
    private EditText id;
    private EditText password;
    private EditText password2;
    private EditText nickname;
    private EditText phoneNumber;
    private EditText email;
    private TextView checkingTextView;
    private Boolean idOverlapFlag;
    private Boolean nicknameOverlapFlag;
    private Boolean pwCheckFlag;

    private String secretPassword;
    private JSONObject JsonObj;
    String tag = "JoinMembershipActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_membership);

        name = findViewById(R.id.nameEditText);
        id = findViewById(R.id.idEditText);
        password = findViewById(R.id.pwEditText);
        password2 = findViewById(R.id.pwEditText2);
        nickname = findViewById(R.id.nicknameEditText);
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        email = findViewById(R.id.emailEditText);
        checkingTextView = findViewById(R.id.checkingTextView);

        idOverlapFlag = false;
        pwCheckFlag = false;
        nicknameOverlapFlag = false;
    }

    public void onClickCheckOverlap(View v){

        JsonObj = new JSONObject();
        String OverlapSQL = "";
        Boolean idOrNicknameFlag = v.getId() == R.id.idCheckBtn;                        // id: ture, nickname: false

        if (idOrNicknameFlag)                                                                           // id 중복 확인 버튼이라면
            OverlapSQL = "SELECT * FROM member_info WHERE id = '" + id.getText() + "';";                // ex) select * from member_info WHERE id = 'park';
        else                                                                                            // 아니면 닉네임 중복 확인 버튼
            OverlapSQL = "SELECT * FROM member_info WHERE nickname = '" + nickname.getText() + "';";
        
        Log.i(tag, OverlapSQL);


        try {
            JsonObj.accumulate("overlapSQL", OverlapSQL);

            RequestingServer req = new RequestingServer(this, JsonObj);

            String response = req.execute("http://192.168.0.10:3000/checkOverlap").get();
            Log.i(tag, "result: " + response);

            if (response == null)
                return;
            String[] resResult = response.split(",");

            Log.i(tag, "overlap: " + resResult[0]);

            switch (resResult[0]){
                case "can be used":
                    if (idOrNicknameFlag)
                        idOverlapFlag = true;
                    else
                        nicknameOverlapFlag = true;
                    Toast.makeText(this, "사용 가능합니다!!", Toast.LENGTH_SHORT).show();
                    break;
                case "overlap":
                    Toast.makeText(this, "중복됩니다!!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClickCheckPw(View v){

        if (password.getText().toString().equals(password2.getText().toString())){
            
            //비밀번호 암호화 코드
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                md.update(password.getText().toString().getBytes());
                secretPassword = String.format("%128x", new BigInteger(1, md.digest()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Log.i(tag, "secretPassword: " + secretPassword);
            Log.i(tag, "secretPassword 길이: " + secretPassword.length() + "");

            pwCheckFlag = true;
            Toast.makeText(getApplicationContext(), "비밀번호 확인 완료되었습니다!", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), "비밀번호가 서로 다릅니다!", Toast.LENGTH_SHORT).show();
    }

    public void onClickJoin(View v){

        if (!idOverlapFlag)
            Toast.makeText(getApplicationContext(), "아이디 중복 확인 완료하셔야 합니다!", Toast.LENGTH_SHORT).show();
        else if (!pwCheckFlag)
            Toast.makeText(getApplicationContext(), "비밀번호 확인 완료하셔야 합니다!", Toast.LENGTH_SHORT).show();
        else if (!nicknameOverlapFlag)
            Toast.makeText(getApplicationContext(), "닉네임 중복 확인 완료하셔야 합니다!", Toast.LENGTH_SHORT).show();
        else {
            JsonObj = new JSONObject();

            // ex) insert into member_info values ('id', 'pw', 'park', '123-4567-8910', 'e-mail', 'pack');  ,    insert into score values ('pack', 0, 0, 0, 0);
            String memberInfoSQL =  "INSERT INTO member_info values ('" +id.getText() + "', '" + secretPassword + " ', '" +
                    name.getText() + " ', '" + phoneNumber.getText() + " ', '" +email.getText() + " ', '" +nickname.getText() + "');";
            String scoreSQL = "INSERT INTO score values ('" +nickname.getText() + "', 0, 0, 0, 0);";

            Log.i(tag, memberInfoSQL);
            Log.i(tag, scoreSQL);

            try {
                JsonObj.accumulate("memberInfoSQL", memberInfoSQL);
                JsonObj.accumulate("scoreSQL", scoreSQL);

                RequestingServer req = new RequestingServer(this, JsonObj);              // 요청 객체 생성

                String response = req.execute("http://192.168.0.10:3000/join").get();
                Log.i(tag, "result: " + response);

                if (response == null)
                    return;

                String[] resResult = response.split(",");
                Log.i(tag, "resResult[0]: " + resResult[0]);

                if (resResult[0].equals("Join success")){
                    Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "회원가입 실패..", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}