package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class NBackPracticeGameSetting extends AppCompatActivity {
    private int n;
    private int examLength;
    private int delayTime;
    private boolean parallel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nback_practice_game_setting);
        n = 2;
        examLength = 10;
        delayTime = 1;
        parallel = false;
    }

    public void onClickStartPractice(View v){
        Intent intent = new Intent(NBackPracticeGameSetting.this, NBackGame.class);
        intent.putExtra("mode", false);                                   // mode = false 은 연습 모드
        intent.putExtra("n", n);
        intent.putExtra("examLength", examLength);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("parallel", parallel);
        startActivity(intent);
    }

    public void onClickBackBtn(View v){
        finish();
    }

    public void onClickNPlusBtn(View v){
        TextView nText = findViewById(R.id.nText);
        nText.setText("N = " + (++n));
    }
    public void onClickNMinusBtn(View v){
        TextView nText = findViewById(R.id.nText);
        if (n < 3){
            nText.setText("N은 2보다 작을 수 없습니다!");
            return;
        }
        nText.setText("N = " + (--n));
    }

    public void onClickLengthPlusBtn(View v){
        TextView lengthText = findViewById(R.id.lengthText);
        lengthText.setText("문제 길이 = " + (++examLength));
    }
    public void onClickLengthMinusBtn(View v){
        TextView lengthText = findViewById(R.id.lengthText);
        if (examLength < 8){
            lengthText.setText("문제 길이는 7보다 작을 수 없습니다!");
            return;
        }
        lengthText.setText("문제 길이 = " + (--examLength));
    }

    public void onClickDTimePlusBtn(View v){
        TextView dTimeText = findViewById(R.id.dTimeText);
        dTimeText.setText("한 문제당 시간 = " + (++delayTime) + "초");
    }
    public void onClickDTimeMinusBtn(View v){
        TextView dTimeText = findViewById(R.id.dTimeText);
        if (delayTime < 2){
            dTimeText.setText("1보다 작은 값은 안 됩니다!");
            return;
        }
        dTimeText.setText("한 문제당 시간 = " + (--delayTime) + "초");
    }

    public void onClickSwitch(View v){
        parallel = ((Switch) v).isChecked();
        Log.i("msg", parallel+"");
    }

}