package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DwmtGameActivity extends AppCompatActivity {

    private DwmtMetronome metronome;
    private int answerType;
    private int checkType;
    private int questionNumber;            // 현재 문제 번호 -1
    private TextView leftView;
    private TextView questionView;
    private Button[] answerBtnArray;
    private DwmtQuiz[] quizArray;
    private int score;
    String tag = "DwmtGameActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwmt_game);

        Intent intent = getIntent();
        metronome = new DwmtMetronome(this);
        answerType = intent.getIntExtra("answerType", 1);
        checkType = intent.getIntExtra("checkType", 1);
        questionNumber = 0;
        score = 0;
        answerBtnArray = new Button[4];
        quizArray = new DwmtQuiz[60];
        int answerBtnID[] = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};

        Log.i("intent", "DwmtActivity  answerType: " + answerType + "  checktype: " + checkType);   // DB 설계 이후에 Type변수 이용하는 코드로 변경

        // 퀴즈 초기화
        String tableName = "quizlist";
        init(tableName);

        // View 초기화
        leftView = findViewById(R.id.leftView);
        questionView = findViewById(R.id.questionView);
        for (int i=0; i<4; i++)
            answerBtnArray[i] = findViewById(answerBtnID[i]);
    }

    public void init(String tableName){             // 매개변수: DB 설계에 따라 달라 질 수 있음

        DwmtDBHelper dbHelper = new DwmtDBHelper(this);
        SQLiteDatabase db;
        try {
            dbHelper.createDataBase();
            dbHelper.openDataBase();

            db = dbHelper.getReadableDatabase();        // 사용할 DB
            String sql ="SELECT * FROM " + tableName;

            Cursor cursor = db.rawQuery(sql, null);
            String[] answerArray = new String[4];

            if(cursor != null){
                int k=0;
                // Table에 있는 튜플 전체 가져오기
                while ( cursor.moveToNext() ){
                    // 보기 1, 2, 3, 4
                    for (int i=0; i<4; i++)
                        answerArray[i] = cursor.getString(i + 2)+"";

                    quizArray[k++] = new DwmtQuiz(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            Integer.parseInt(cursor.getString(6)));
                }
            }
            dbHelper.close();
            Log.i(tag, "quizArray: " + quizArray[13].getAnswerArray(0)+ quizArray[13].getAnswerArray(1)+ quizArray[13].getAnswerArray(2)+ quizArray[13].getAnswerArray(3));
            Log.i(tag, "quizArray: " + quizArray[14].getAnswerArray(0)+ quizArray[14].getAnswerArray(1)+ quizArray[14].getAnswerArray(2)+ quizArray[14].getAnswerArray(3));
        }catch (Exception e){
            Log.e("DB", e.toString());
        }

    }

    public void gameStart(View v){

        v.setVisibility(View.GONE);
        leftView.setText("" + (questionNumber+1));
        questionView.setText(quizArray[questionNumber].getQuestion());

        for (int i=0; i<4; i++) {
            if (answerBtnArray[i] == null)
                continue;
            answerBtnArray[i].setText(quizArray[questionNumber].getAnswerArray(i));
            answerBtnArray[i].setVisibility(View.VISIBLE);
        }

        metronome.setRandomCnt();
        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (metronome.getCnt() > 0) {
                    metronome.play();
                    handler.postDelayed(this, 500);
                }
                else{                                       // 게임 종료
                    for (int i=0; i<4; i++)
                        answerBtnArray[i].setVisibility(View.GONE);
                    v.setVisibility(View.VISIBLE);
                }
            }
        }, 500);
    }

    public void clickAnswer(View v){
        if (questionNumber > 58)
            return;

        // score 처리
        String vString = ((Button)v).getText().toString();
        Log.i("score",  quizArray[questionNumber].getRightAnswer() + "   " + vString);   //+ vString.substring(vString.length()-1)

        // 다음 문제 표시 처리
        leftView.setText("" + ((++questionNumber) + 1));
        questionView.setText(quizArray[questionNumber].getQuestion());

        // 다음 보기 표시
        for (int i=3; i>-1; i--) {
            if (quizArray[questionNumber].getAnswerArray(i) == null){
                Log.i(tag, "null입니다!!!");
                answerBtnArray[0].setVisibility(View.GONE);
                answerBtnArray[1].setVisibility(View.GONE);
                break;
            }
            Log.i(tag, quizArray[questionNumber].getAnswerArray(i));
            answerBtnArray[i].setText(quizArray[questionNumber].getAnswerArray(i));
            answerBtnArray[i].setVisibility(View.VISIBLE);
        }
    }
    
    /* 점수 방법? 
    => count 10초당 3점씩 부여 후 (메트로놈 정답 - 유저 답), 틀린 문제 수 빼기(문제는 10초당 5문제)
     */
}