package com.example.a_test_in_my_head;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private final String loginURL = "http://192.168.0.10:3000/login";
    private EditText id;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
    }

    public void onClickLoginBtn(View view){
        //json obj 생성 및 저장
        JSONObject loginJsonObj = new JSONObject();
        String secretPassword = "";

        try {
            //비밀번호 암호화 코드
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getText().toString().getBytes());
            secretPassword = String.format("%128x", new BigInteger(1, md.digest()));

            loginJsonObj.accumulate("id", id.getText());
            loginJsonObj.accumulate("password", secretPassword);

            RequestingServer req = new RequestingServer(this, loginJsonObj);              // 요청 객체 생성

            String response = req.execute(loginURL).get();
            Log.i("LoginActivity", "result: " + response);

            if (response == null)
                return;

            String[] resResult = response.split(",");

            switch (resResult[0]){
                case "Login success":
                    Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
                    menuIntent.putExtra("user", new User(resResult[1], resResult[2], resResult[3],resResult[4],resResult[5]));
                    startActivity(menuIntent);
                    break;
                case "Login id fail":
                    Toast.makeText(this, "id가 틀렸습니다!", Toast.LENGTH_SHORT).show();
                    break;
                case "Login pw fail":
                    Toast.makeText(this, "비밀번호가 틀렸습니다!", Toast.LENGTH_SHORT).show();
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
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
        Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
        menuIntent.putExtra("user", new User("public", "0","0","0","0"));
        startActivity(menuIntent);
    }
}