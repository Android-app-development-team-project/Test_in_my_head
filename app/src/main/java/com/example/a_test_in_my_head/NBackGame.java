
package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NBackGame extends AppCompatActivity {
    private long backKeyPressedTime;
    private TextView levelTextView;
    private TextView sumResultView;
    private ArrayList<NBack> nBackList;
    private Boolean mode;
    private int level;
    private boolean parallel;
    private int examNum;
    private int nBackScore;
    private final float passScoreRatio = 0.28f;
    private User user;
    private SharedPreferences spref;
    private String tag = "NBackGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nback_game);

        backKeyPressedTime = 0;
        spref = getSharedPreferences("user.pref", Context.MODE_PRIVATE);
        user = new User(spref.getString("nickname", "public"), spref.getString("nBackScore", "0"), "0", "0", "0");

        Intent intent = getIntent();
        mode = intent.getBooleanExtra("mode", true);          // mode: true는 랭킹 모드, false는 연습 모드
        parallel = intent.getBooleanExtra("parallel", false);
        nBackScore = 0;

        // intent를 통해 가져온 매개변수를 가지고 NBack 객체 초기화
        nBackList = new ArrayList<NBack>();
        nBackList.add( new NBack(intent.getIntExtra("n", 2),
                intent.getIntExtra("examLength", 7),
                intent.getIntExtra("delayTime", 2),
                findViewById(R.id.examView),
                findViewById(R.id.resultView)));

        Log.i(tag, "mode: "+ mode + "\nN: " + nBackList.get(0).getN() +
                "\nlength: " + nBackList.get(0).getExamLength() +
                "\ndelay: " + nBackList.get(0).getDelayTime() +
                "\nparallel: " + parallel);

        // 모드를 통한 초기 레벨 설정
        if (mode) {
            levelTextView = findViewById(R.id.levelTextView);
            level = 0;
            levelUP();
        }

        // 병렬 화면 설정
        if (parallel){
            nBackList.add( new NBack(intent.getIntExtra("n", 2),
                    intent.getIntExtra("examLength", 7),
                    intent.getIntExtra("delayTime", 1),
                    findViewById(R.id.examView2),
                    findViewById(R.id.resultView2)));

            sumResultView = findViewById(R.id.sumResultView);
            nBackList.get(1).getExamView().setVisibility(View.VISIBLE);
        }

        Log.i(tag, "listSize: " + nBackList.size());
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"나가시면 현재 점수가 초기화됩니다! 상관 없으시다면 한번 더 누르세요!",Toast.LENGTH_SHORT).show();
            return;
        } else {
            examNum = 10000;                         // Stop Game
            finish();
        }
    }

    public void onClickStart(View startView) {
        startView.setVisibility(View.GONE);

        if (parallel) {
            sumResultView.setVisibility(View.GONE);
            findViewById(R.id.answerBtn2).setVisibility(View.VISIBLE);
        }

        for(int i = 0; i < nBackList.size(); i++) {
            nBackList.get(i).getResultView().setVisibility(View.GONE);
            nBackList.get(i).init();
        }
        threeCount(startView);
    }

    public void threeCount(View startView){
        nBackList.get(0).getExamView().setTextSize(50);
        if (parallel)
            nBackList.get(1).getExamView().setVisibility(View.GONE);
        new Thread(new Runnable() {                                         // 수정?
            @Override
            public void run() {
                for (int i=4; i>0; i--){
                    int threeCnt = i-1;
                    NBackGame.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(threeCnt==0) {
                                nBackList.get(0).getExamView().setText("시작!");
                                nBackList.get(0).getExamView().setTextSize(100);
                            }
                            else
                                nBackList.get(0).getExamView().setText(threeCnt + "초 후 시작!");
                        }
                    });
                    try {
                        if(i== 1) {
                            // gameStart
                            Thread.sleep(500);
                            NBackGame.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gameStart(startView);
                                }
                            });
                        }
                        else
                            Thread.sleep(1000);             // 3초후, 3 ,2, 1,
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void gameStart(View startView){
        examNum = -1;
        for(int i=0; i<nBackList.size(); i++) {
            nBackList.get(i).getExamView().setText("");
            nBackList.get(i).getExamView().setVisibility(View.VISIBLE);
        }

        Log.i(tag,"userAnswer: " + nBackList.get(0).getUserAnswer());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            String[] numColors = {"#4F4B4B", "#E08F31", "#036994"};   // 반복할 글씨의 컬러, 같은 숫자가 연속될 경우 구분하기 위함.

            @Override
            public void run() {
                if (++examNum < nBackList.get(0).getExamLength()) {                                       // 문제 푸는 구간
                    for(int i=0; i<nBackList.size(); i++){                                                          // 문제 표시구간
                        nBackList.get(i).getExamView().setTextColor(Color.parseColor(numColors[examNum % 3]));
                        nBackList.get(i).getExamView().setText(nBackList.get(i).getExam().charAt(examNum)+"");
                    }
                    handler.postDelayed(this, nBackList.get(0).getDelayTime());
                }
                else{
                    // test 끝나고 결과 출력
                    showResult();
                    startView.setVisibility(View.VISIBLE);
                }
            }
        }, nBackList.get(0).getDelayTime());
    }

    public void onClickO(View v){
        if (examNum >= nBackList.get(0).getN() && examNum < nBackList.get(0).getExamLength()) {             // "O" 버튼 클릭시  userAnswer의 "O" 추가
            nBackList.get(0).setUserAnswer(examNum);

            Log.i(tag,"userAnswer: " + nBackList.get(0).getUserAnswer() +
                    " examNum: " + examNum +
                    " Exam: " + nBackList.get(0).getExam().charAt(examNum));
        }
    }

    public void onClickO2(View v){
        if (examNum >= nBackList.get(1).getN() && examNum < nBackList.get(1).getExamLength())            // "O" 버튼 클릭시  userAnswer의 "O" 추가
            nBackList.get(1).setUserAnswer(examNum);
    }

    public void showResult(){
        float sumScore = 0f;

        for(int i=0; i<nBackList.size(); i++) {

            nBackList.get(i).caculateScore();

            nBackList.get(i).getResultView().setText("");
            nBackList.get(i).getResultView().append((i+1) + "번 문제 : " + nBackList.get(i).getExam() +
                    "\n정답 : " + nBackList.get(i).getRightAnswer() +
                    "\nuser 답 : " + nBackList.get(i).getUserAnswer() +
                    "\nScore : " + nBackList.get(i).getScore());
            sumScore += nBackList.get(i).getScore();
            nBackList.get(i).getResultView().setVisibility(View.VISIBLE);
        }
        nBackScore += Math.round(sumScore);

        if (nBackList.size()>1){
            sumResultView.setText("Score 합: " + sumScore);
            sumResultView.setVisibility(View.VISIBLE);
        }
        if (sumScore >= Math.floor(nBackList.get(0).getExamLength()*passScoreRatio) && mode) {                      // 합격 점수 이상일 때
            Log.i(tag, "nBackScore: " + nBackScore + ", user.getnBackScore(): " + user.getnBackScore()
                    +", Math.round(sumScore): " + Math.round(sumScore) +" , sumScore: " + sumScore);

            if (user.getNickname().equals("public"))
                Toast.makeText(this, "public 유저는 점수를 랭크에 저장할 수 없습니다!", Toast.LENGTH_SHORT).show();
            else if (Integer.parseInt(user.getnBackScore()) < nBackScore) {
                user.setScore(this, "N_Back", nBackScore);
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("nBackScore", user.getnBackScore());
                editor.commit();
                Toast.makeText(this, "랭크에 저장되었습니다!", Toast.LENGTH_SHORT).show();
            }
            levelUP();
        }
    }

    public void levelUP(){
        ((TextView)findViewById(R.id.levelTextView)).setText("랭크게임: LEVEL " + (++level));
        if (level==1)
            return;

        for(int i=0; i<nBackList.size(); i++) {
            nBackList.get(i).plusExamLength();
            if (level > 10 && level < 20)
                nBackList.get(i).plusN();
            else if (level == 23 || level == 26)
                nBackList.get(i).minusDelayTime();
            else if (level == 27) {                                                       // 새로운 nBack 추가
                parallel = true;
                nBackList.add(new NBack(nBackList.get(0).getN(),
                        nBackList.get(0).getExamLength(),
                        nBackList.get(0).getDelayTime(),
                        findViewById(R.id.examView2),
                        findViewById(R.id.resultView2)));
            } else if (level == 31) {                                           // 마지막 30 level clear
                for(int j=0; j<nBackList.size(); j++)
                    nBackList.get(j).getResultView().setVisibility(View.GONE);
                sumResultView.setText("GAME CLEAR");
            }
        }
    }
}
