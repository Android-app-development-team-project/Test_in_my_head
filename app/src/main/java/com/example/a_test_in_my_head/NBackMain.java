package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NBackMain extends AppCompatActivity {
    private long backKeyPressedTime = 0;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 return home!",Toast.LENGTH_SHORT).show();
            return;
        } else { finish(); }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nback_main);
    }

    public void onClickLevelTest(View v){
        Intent intent = new Intent(this, NBackGame.class);
        startActivity(intent);
    }

    public void onClickPractice(View v) {
        Intent intent = new Intent(this, NBackPracticeGameSetting.class);
        startActivity(intent);
    }

}
