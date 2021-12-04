package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Success extends AppCompatActivity {
    Button RetryBtn, BackBtn;
    TextView sumScore;
    GuessNumberInGame easyMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RetryBtn = (Button) findViewById(R.id.RetryBtn);
        BackBtn = (Button) findViewById(R.id.BackBtn);
        easyMode = new GuessNumberInGame();
        sumScore = (TextView) findViewById(R.id.sumScore);
        sumScore.setText("누적된 값은 : " + String.valueOf(GuessNumberInGame.score));

        SharedPreferences spref = getSharedPreferences("user.pref", Context.MODE_PRIVATE);

        if (spref.getString("nickname", "paublic").equals("public"))
            Toast.makeText(this, "public 유저는 점수를 랭크에 저장할 수 없습니다!", Toast.LENGTH_SHORT).show();
        else if (GuessNumberInGame.score > Integer.parseInt(spref.getString("guessNumberScore", "0"))) {
            User user = new User(spref.getString("nickname", ""), "", "", spref.getString("guessNumberScore", ""), "");
            user.setScore(this, "Guess_Number", GuessNumberInGame.score);
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("guessNumberScore", GuessNumberInGame.score+"");
            editor.commit();
            Toast.makeText(this, "랭크에 저장되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Back(View v){
        Intent intent = new Intent(Success.this,MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Toast.makeText(getApplicationContext(), "최종 점수는 : " + GuessNumberInGame.score + "입니다!", Toast.LENGTH_SHORT).show();
        GuessNumberInGame.score = 0;

        startActivity(intent);
    }

    public void Retry(View v){
        Intent intent = new Intent(Success.this,GuessNumberMain.class);
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