package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Wrong extends AppCompatActivity {

    Button RetryBtn, BackBtn;
    TextView totalScore;
    GuessNumberInGame easyMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RetryBtn = (Button) findViewById(R.id.RetryBtn);
        BackBtn = (Button) findViewById(R.id.BackBtn);
        easyMode = new GuessNumberInGame();
        easyMode = new GuessNumberInGame();
        totalScore = (TextView) findViewById(R.id.totalScore);
        totalScore.setText("누적된 값은 : " + String.valueOf(GuessNumberInGame.score));
    }

    public void Back(View v){
        Intent intent = new Intent(Wrong.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("user", (User) (getIntent().getSerializableExtra("user")));
        Toast.makeText(getApplicationContext(), "최종 점수는 : " + GuessNumberInGame.score + "입니다!", Toast.LENGTH_SHORT).show();
        GuessNumberInGame.score = 0;

        startActivity(intent);
    }

    public void Retry(View v){
        Intent intent = new Intent(Wrong.this,GuessNumberMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;

    }
    public void onBackPressed(){
        return;
    }
}