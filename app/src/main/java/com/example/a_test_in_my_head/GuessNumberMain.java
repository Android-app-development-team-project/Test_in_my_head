package com.example.a_test_in_my_head;


import static java.util.Collections.shuffle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class GuessNumberMain extends AppCompatActivity {
    TextView timer; // 타이머 textView
    int value; // 타이머 숫자 표시
    int i;
    Button easyButton, hardButton;
    Intent easyIntent, hardIntent;
    private long backKeyPressedTime = 0;
    TextView sumScore;
    GuessNumberInGame easyMode;
    private String tag = "GuessNumberMain";

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 return home!",Toast.LENGTH_SHORT).show();
            GuessNumberInGame.score = 0;
            return;
        } else {
            Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_number_main);

        easyIntent = new Intent(this, GuessNumberInGame.class);
        hardIntent = new Intent(this, GuessNumberInGameHard.class);
        easyButton = (Button)findViewById(R.id.easyLevel);
        hardButton = (Button)findViewById(R.id.hardLevel);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // easy모드로 이동.
                startActivity(easyIntent);
            }
        });
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // hard 모드로 이동.
                startActivity(hardIntent);
            }
        });
        easyMode = new GuessNumberInGame();
        sumScore = (TextView) findViewById(R.id.textView7);
        sumScore.setText("누적된 값은 : " + String.valueOf(GuessNumberInGame.score));

    }

}