package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NBackMain extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
