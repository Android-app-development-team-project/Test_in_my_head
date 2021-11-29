package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    EditText id;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);

    }

    public void onClickLoginBtn(View view){

        //json obj 생성 및 저장
        JSONObject loginJsonObj = new JSONObject();

        // ex) select * from member_info WHERE id = 'id';   ,  select * from member_info WHERE password = 'password';
        String loginIdSQL =  "SELECT * FROM member_info WHERE id = '" +id.getText() + "';";

        Log.i("sql", loginIdSQL);

        String secretPassword = "";

        //비밀번호 암호화 코드
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getText().toString().getBytes());
            secretPassword = String.format("%128x", new BigInteger(1, md.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        try {
            loginJsonObj.accumulate("loginIdSQL", loginIdSQL);
            loginJsonObj.accumulate("password", secretPassword);

            RequestingServer req = new RequestingServer(this, loginJsonObj);              // 요청 객체 생성

            String response = req.execute("http://192.168.0.10:3000/login").get();
            Log.i("LoginActivity", "result: " + response);

            if (response == null)
                return;

            String[] resResult = response.split(",");

            switch (resResult[0]){
                case "Login success":
                    Intent intent = getIntent();
                    intent.putExtra("user", new User(resResult[1],
                                            Integer.parseInt(resResult[2]),
                                            Integer.parseInt(resResult[3]),
                                            Integer.parseInt(resResult[4]),
                                            Integer.parseInt(resResult[5])));
                    finish();
                    break;
                case "Login id fail":
                    Toast.makeText(this, "id가 틀렸습니다!", Toast.LENGTH_SHORT).show();
                    break;
                case "Login pw fail":
                    Toast.makeText(this, "비밀번호가 틀렸습니다!", Toast.LENGTH_SHORT).show();
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

    public void onClickJoinBtn(View view){
        Intent intent = new Intent(this, JoinMembershipActivity.class);
        startActivity(intent);
    }

    public void finishBtn(View view){
        finish();
    }
}