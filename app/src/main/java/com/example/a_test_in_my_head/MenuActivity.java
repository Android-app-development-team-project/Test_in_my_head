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


public class MenuActivity extends AppCompatActivity {

    private long backKeyPressedTime=0;
    private User user;
    private String tag = "MenuActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        consolUser();

        setContentView(R.layout.activity_menu);
        Button button1 = (Button) findViewById(R.id.guessnumber);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent1 = new Intent(MenuActivity.this,GuessNumberMain.class);
                startActivity(myintent1);
            }
        });
        Button button2 = (Button) findViewById(R.id.nback);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent2 = new Intent(MenuActivity.this,NBackMain.class);
                startActivity(myintent2);
            }
        });
        Button listButton = (Button) findViewById(R.id.exit);
        listButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
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

    public void onClickGuessHint(View view){
        Intent intent3 = new Intent(MenuActivity.this,GuessNumberHelp.class);
        startActivity(intent3);
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
        Intent intent3 = new Intent(MenuActivity.this, DWDMHelp.class);
        startActivity(intent3);
    }
    public void onClickNBackHelp(View view){
        Intent intent3 = new Intent(MenuActivity.this, DWDMHelp.class);
        startActivity(intent3);
    }
    public void onClickRankBtn(View view){
        Intent intent3 = new Intent(MenuActivity.this, RankActivity.class);
        intent3.putExtra("user", user);
        startActivity(intent3);
    }
    public void consolUser(){
        Log.i(tag, user.getNickname());
        Log.i(tag, user.getnBackScore() + "");
        Log.i(tag, user.getDwmtScore() + "");
        Log.i(tag, user.getGuessNumberScore() + "");
        Log.i(tag, user.getTotalScore() + "");
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        user = (User) data.getSerializableExtra("user");

    }
}