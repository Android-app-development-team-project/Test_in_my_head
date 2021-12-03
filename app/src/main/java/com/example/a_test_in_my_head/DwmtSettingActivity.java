package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DwmtSettingActivity extends AppCompatActivity {

    private Intent intent;
    private TextView nowAnswer;
    private TextView nowCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwmt_setting);
        intent = getIntent();

        nowAnswer = findViewById(R.id.nowanswer);
        nowCheck = findViewById(R.id.nowcheck);
    }

    public void onClickOXBtn(View view){
        intent.putExtra("answerType", 1);
        nowAnswer.setText("OX퀴즈");
    }

    public void onClickFourBtn(View view){
        intent.putExtra("answerType", 2);
        nowAnswer.setText("객관식");
    }

    public void onClickAllBtn(View view){
        intent.putExtra("answerType", 3);
        nowAnswer.setText("둘 다");
    }

    public void onClickSpeedBtn(View view){
        intent.putExtra("checkType", 1);
        nowCheck.setText("1분 제한");
    }

    public void onClickManyBtn(View view){
        intent.putExtra("checkType", 2);
        nowCheck.setText("20문제");
    }

    public void onClickSettingEndBtn(View v){
        setResult(RESULT_OK, intent);
        finish();
    }
}