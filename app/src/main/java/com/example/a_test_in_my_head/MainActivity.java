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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private long backKeyPressedTime=0;
    private User user;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 종료됩니다!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(System.currentTimeMillis()<=backKeyPressedTime+2000){
            finishAffinity();
            System.runFinalization();
            System.exit(0);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean k_f = intent.getBooleanExtra("kill", false);
        if(k_f == true){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = new User("public", "0","0","0","0");

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);

        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.guessnumber);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent1 = new Intent(MainActivity.this,GuessNumberMain.class);
                startActivity(myintent1);
                finish();
            }
        });
        Button button2 = (Button) findViewById(R.id.nback);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent2 = new Intent(MainActivity.this,NBackMain.class);
                startActivity(myintent2);
                finish();
            }
        });
        Button listButton = (Button) findViewById(R.id.exit);
        listButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("정말로 종료하시겠습니까?");
                builder.setTitle("종료 알림창")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finishAffinity();
                                System.runFinalization();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("종료 알림창");
                alert.show();
            }
        });



    }
    public void onClickGuessHint(View view){
        Intent intent3 = new Intent(MainActivity.this,GuessNumberHelp.class);
        startActivity(intent3);
        finish();
/*        if(view==alert){ //view가 alert 이면 팝업실행 즉 버튼을 누르면 팝업창이 뜨는 조건
            Context mContext = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.dialog,(ViewGroup) findViewById(R.id.popup));
            android.app.AlertDialog.Builder aDialog = new android.app.AlertDialog.Builder(MainActivity.this);
            aDialog.setTitle("히든스탯 목록"); //타이틀바 제목
            aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅
            aDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            android.app.AlertDialog ad = aDialog.create();

            ad.show();//보여줌!*/
    }
    public void onClickDWDMHelp(View view){
        Intent intent3 = new Intent(MainActivity.this, DWDMHelp.class);
        startActivity(intent3);
        finish();
    }
    public void onClickNBackHelp(View view){
        Intent intent3 = new Intent(MainActivity.this, DWDMHelp.class);
        startActivity(intent3);
        finish();
    }
    public void onClickRankBtn(View view){
        Intent intent3 = new Intent(MainActivity.this, RankActivity.class);
        intent3.putExtra("user", user);
        startActivity(intent3);
    }
    public void consolUser(){
        Log.i("", user.getNickname());
        Log.i("", user.getnBackScore() + "");
        Log.i("", user.getDwmtScore() + "");
        Log.i("", user.getGuessNumberScore() + "");
        Log.i("", user.getTotalScore() + "");
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("intent", "LoginActivityResult answerType: " + data.getIntExtra("answerType", 1) + "  data: " + data.getIntExtra("checkType", 1));

        user = (User) data.getSerializableExtra("user");
        consolUser();
    }
}