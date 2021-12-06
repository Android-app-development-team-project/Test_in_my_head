package com.example.a_test_in_my_head;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DwmtMainActivity extends AppCompatActivity {

    private Intent gameIntent;
    private long backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwmt_main);

        gameIntent = new Intent(this, DwmtGameActivity.class);
        backKeyPressedTime = 0;
    }

    public void onClickStartTest(View view){
        startActivity(gameIntent);
    }

    public void onClickSetting(View view){
        Intent serttingIntent = new Intent(this, DwmtSettingActivity.class);
        startActivityForResult(serttingIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.i("intent", "onActivityResult answerType: " + data.getIntExtra("answerType", 1) + "  data: " + data.getIntExtra("checkType", 1));

        // SettingActivity에서 받은 answerType와 checkType 넣어주기
        gameIntent.putExtra("answerType", data.getIntExtra("answerType", 1));
        gameIntent.putExtra("checkType", data.getIntExtra("checkType", 1));
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료됩니다!",Toast.LENGTH_SHORT).show();
            return;
        } else { finish(); }
    }
}