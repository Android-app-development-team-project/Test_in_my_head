package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DwmtGameActivity extends AppCompatActivity {

    private final int numberOfQuestions = 60;
    private long backKeyPressedTime;
    private DwmtMetronome metronome;
    private int answerType;
    private int checkType;
    private int questionNumber;            // 현재 문제 번호 -1
    private TextView leftView;
    private TextView questionView;
    private Button[] answerBtnArray;
    private EditText metronomeAnswer;
    private Button startBtn;
    private Button metronomeBtn;
    private DwmtQuiz[] quizArray;
    private SharedPreferences spref;
    private int maxScore;
    private int currentScore;
    private String userAnswer;
    private int metronemeCnt;
    private User user;
    String tag = "DwmtGameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dwmt_game);

        Intent intent = getIntent();
        metronome = new DwmtMetronome(this);
        answerType = intent.getIntExtra("answerType", 1);
        checkType = intent.getIntExtra("checkType", 1);
        backKeyPressedTime = 0;
        questionNumber = 0;
        currentScore = 0;

        spref = getSharedPreferences("user.pref", Context.MODE_PRIVATE);
        maxScore = Integer.parseInt(spref.getString("dwmtScore", "0"));
        user = new User(spref.getString("nickname", "public"), "0", spref.getString("dwmtScore", "0"), "0", "0");

        answerBtnArray = new Button[4];
        startBtn = findViewById(R.id.dwmtStartBtn);
        metronomeAnswer = findViewById(R.id.metronomeAnswer);
        metronomeBtn = findViewById(R.id.metronomeBtn);


        quizArray = new DwmtQuiz[numberOfQuestions];
        int[] answerBtnID = new int[] {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4};
        userAnswer = "";

        Log.i("intent", "DwmtActivity  answerType: " + answerType + "  checktype: " + checkType);   // DB 설계 이후에 Type변수 이용하는 코드로 변경

        // 퀴즈 초기화, Activity 시작 시 전부 가져오기
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

            if(cursor != null){
                int k=0;
                // Table에 있는 튜플 전체 가져오기
                while ( cursor.moveToNext() ){
                    // 문제 id, question, answer 1, 2, 3, 4, rigthAnswer
                    quizArray[k++] = new DwmtQuiz(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6));
                }
            }
            dbHelper.close();
        }catch (Exception e){
            Log.e("DB", e.toString());
        }
    }

    // 문제들을 랜덤하게 섞기 위한 셔플 메소드
    public void shuffleQuizArray(DwmtQuiz[] quizArray){
        DwmtQuiz tempQuiz1, tempQuiz2;
        int randomIdx1, randomIdx2;

        // 0~1부터 랜덤하게 나온 수에 length(60)을 곱해서 0~60의 index를 만들고 뒤바꾸는 코드, 최대 문제 수인 60번을 셔플
        for (int i=0; i < quizArray.length; i++){
            randomIdx1 = (int) (Math.random() * quizArray.length );
            randomIdx2 = (int) (Math.random() * quizArray.length );
            tempQuiz1 = quizArray[randomIdx1];
            tempQuiz2 = quizArray[randomIdx2];
            quizArray[randomIdx1] = tempQuiz2;
            quizArray[randomIdx2] = tempQuiz1;
        }
    }

    public void gameStart(View v){
        currentScore += 3;
        
        shuffleQuizArray(quizArray);                // 셔플
        metronome.setRandomCnt();
        v.setVisibility(View.GONE);
        leftView.setText("" + (questionNumber+1));
        questionView.setText(quizArray[questionNumber].getQuestion());
        metronemeCnt = metronome.getCnt();

        // 보기 표시
        for (int i=0; i<4; i++) {
            if (answerBtnArray[i] == null)
                continue;
            answerBtnArray[i].setText(quizArray[questionNumber].getAnswerArray(i));
            answerBtnArray[i].setVisibility(View.VISIBLE);
        }

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (metronome.getCnt() > 0 ) {
                    metronome.play();
                    handler.postDelayed(this, 1000);
                }
                else{                                                   // 게임 종료
                    for (int i=0; i<4; i++)                             // 보기 버튼 없애기
                        answerBtnArray[i].setVisibility(View.GONE);
                    leftView.setText("게임 종료!");

                    // 메트로놈 적을 EditText 표시
                    metronomeAnswer.setVisibility(View.VISIBLE);
                    metronomeBtn.setVisibility(View.VISIBLE);
                }
            }
        }, 1000);
    }

    public void onClickMetronomeBtn(View v){
        if (CheckChar(metronomeAnswer.getText().toString())){
            Toast.makeText(this,"정답에 문자가 들어갔습니다!",Toast.LENGTH_SHORT).show();
            metronomeAnswer.setVisibility(View.GONE);
            v.setVisibility(View.GONE);
            startBtn.setVisibility(View.VISIBLE);
            return;
        }

        int cntAnswer = Integer.parseInt(metronomeAnswer.getText().toString());
        Log.i(tag, "Score 계산 전: " + currentScore);
        Log.i(tag, "cntAnswer: " + cntAnswer + "   metronemeCnt: " + metronemeCnt);

        currentScore = currentScore - Math.abs(metronemeCnt - cntAnswer);
        Log.i(tag, "Score2: " + currentScore);
        Log.i(tag, "(metronemeCnt/3): " + (metronemeCnt/3) + "   userAnswer.length(): " + userAnswer.length());

        currentScore = currentScore - ((metronemeCnt/3) - userAnswer.length());           // 메트로놈 수에 따른 문제 길이 = 메트로놈 수/3
        Log.i(tag, "Score3: " + currentScore);

        caculateScore();
        Log.i(tag, "caculateScore(): " + currentScore);

        questionView.setText("Score: " + currentScore);

        metronomeAnswer.setVisibility(View.GONE);
        v.setVisibility(View.GONE);
        startBtn.setVisibility(View.VISIBLE);                      // start Btn 다시 Visible
    }
    
    // 숫자가 아닌 문자가 있는지 검사하는 메소드          있으면 true, 없으면 false
    public Boolean CheckChar(String metronomeAnswer){
        String numberChar = "0123456789";

        for (int i=0; i<metronomeAnswer.length(); i++){
            if ((i == 3) || (i == 8)) continue;
            if (!(numberChar.contains(metronomeAnswer.charAt(i)+"")))
                return true;
            Log.i(tag, "CheckCharacter: " + i);
        }
        return false;
    }

    public void caculateScore(){
        /* 점수 시스템
            => count 10초당 3점씩 부여 후 (메트로놈 정답 - 유저 답), 틀린 문제 수 빼기(문제는 10초당 3문제), 총 문제 수 = cnt / 3
            합격 점수 =  (부여된 점수 * 0.7) 반 버림
             ex) 랜덤한 카운트 = 30
             부여 점수 = 9점
             합격 점수 = 6점
             문제 풀이 완료 후 -> 점수 = 부여 점수 - 메트로놈 답 차익 - 총 문제 수에서 틀린 문제 수(하나당 1점)
        */
        for(int i=0; i<userAnswer.length(); i++){


            if (quizArray[i].getRightAnswer().charAt(0) == userAnswer.charAt(i))
                Log.i(tag, "정답!!!");
            else {
                Log.i(tag, "틀림!!!");
                currentScore--;
            }
        }
        Log.i(tag, "score: " + currentScore);

        // user 최고 점수와 비교해 기록 갱신 시 DB에 저장
        if (user.getNickname().equals("public"))
            Toast.makeText(this, "public 유저는 점수를 랭크에 저장할 수 없습니다!", Toast.LENGTH_SHORT).show();
        else if (currentScore > maxScore) {
            Toast.makeText(this,"최고 점수를 갱신하셨습니다!",Toast.LENGTH_SHORT).show();
            user.setScore(this, "DWMT", currentScore);
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("dwmtScore", user.getDwmtScore());
            editor.commit();
            maxScore = currentScore;
            Toast.makeText(this, "랭크에 저장되었습니다!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"나가시면 현재 점수가 초기화됩니다! 상관 없으시다면 한번 더 누르세요!",Toast.LENGTH_SHORT).show();
            return;
        } else {
            metronome.setCnt(-100);                           // Stop Game
            finish();
        }
    }

    public void clickAnswer(View v){
        if (questionNumber > 58)
            return;

        switch (v.getId()){
            case R.id.answer1:
                userAnswer += "1";
                break;
            case R.id.answer2:
                userAnswer += "2";
                break;
            case R.id.answer3:
                userAnswer += "3";
                break;
            case R.id.answer4:
                userAnswer += "4";
                break;
        }

        // 다음 문제 표시 처리
        leftView.setText("" + ((++questionNumber) + 1));
        questionView.setText(quizArray[questionNumber].getQuestion());
        Log.i(tag, quizArray[questionNumber].getRightAnswer());

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



}