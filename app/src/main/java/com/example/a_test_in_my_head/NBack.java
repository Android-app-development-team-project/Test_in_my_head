package com.example.a_test_in_my_head;

import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class NBack {

    private int n;
    private int examLength;
    private int delayTime;
    private String exam;
    private String rightAnswer;
    private StringBuilder userAnswer;
    private float score;
    private Random random;
    private TextView examView;
    private TextView resultView;

    // 생성자 매개변수들: N, 문제길이, delayTime, 병렬여부
    public NBack(int n, int examLength, int delayTime, TextView examView, TextView resultView){
        this.n = n;
        this.examLength = examLength;
        this.delayTime = delayTime * 1000;
        this.examView = examView;
        this.resultView = resultView;
        random = new Random();
    }

    // 난수로 문제 생성   => 가능한 문제의 30퍼센트 이상은 "O" 나오도록 만들기!
    public void createExam(){
        int rightAnwerCnt = 0;
        int rightAnswerPercent = (int)Math.round(0.4*(examLength - n));

        Log.i("rightAnwerPercent", "rightAnwerPercent: "+ rightAnswerPercent + "  Cnt: " + rightAnwerCnt);
        for(int i=0; i < examLength; i++){
            exam += String.valueOf(random.nextInt(10));
            if(i < n)                                               // n-back이므로 답지의 크기는 examLength - n
                continue;
            // 답지 생성
            if((rightAnwerCnt < rightAnswerPercent) && (exam.charAt(i) == exam.charAt(i-n))){
                rightAnswer += "O";
                rightAnwerCnt++;
            }
            else if ((rightAnwerCnt < rightAnswerPercent) && ((i % 3 == 0)||(i % 5 ==0))){    // 답지의 "O" 전체 비율 40%로 맞추기
                exam = exam.substring(0, exam.length()-1);
                exam += exam.charAt(i-n);
                rightAnswer += "O";
                rightAnwerCnt++;
            }
            else
                rightAnswer += "X";
        }
        Log.i("rightAnwerPercent", "rightAnswer: "+ rightAnswer + "  Cnt: " + rightAnwerCnt);

    }

    public void init(){
        exam = "";
        rightAnswer = "";
        score = 0f;
        userAnswer = new StringBuilder();
        userAnswer.append(new String(new char[examLength - n]).replace("\0", "X"));   // 답지 길이만큼 "X"로 채우기 => "XXXX" , 코드 나중에 수정할 예정!!

        createExam();
    }

    // 스코어 계산()
    public void caculateScore(){
        String tempUserAnswer = userAnswer.toString();
        for(int i=0; i<rightAnswer.length(); i++){
            if (rightAnswer.charAt(i) == 'O' && tempUserAnswer.charAt(i) == 'O')
                score++;
            else if (rightAnswer.charAt(i) == 'X' && tempUserAnswer.charAt(i) == 'O')               // 틀린 것을 맞았다고 할 경우 0.5점 감점
                score -= 0.5f;
        }
    }

    // 각 변수들 get, set 메소드
    public int getN() { return n; }
    public void plusN() { this.n += 1; }

    public int getExamLength() { return examLength; }
    public void plusExamLength() { this.examLength += 3; }

    public int getDelayTime() { return delayTime; }
    public void minusDelayTime() { this.delayTime -= 500; }           // 오류 주의!!

    public TextView getExamView() { return examView; }
    public TextView getResultView() { return resultView; }

    public String getExam() { return exam; }
    public void setExam(String exam) { this.exam = exam; }

    public String getRightAnswer() { return rightAnswer; }
    public void setRightAnswer(String rightAnswer) { this.rightAnswer = rightAnswer; }

    public String getUserAnswer() { return userAnswer.toString(); }
    public void setUserAnswer(int examNum) {
        Log.i("examNum - n", "examNum - n: " + (examNum - n) + "   eNum: " + examNum + "  n: " + n);
        if (examNum-n < 0 )
            return;
        userAnswer.setCharAt(examNum - n, 'O');    // i번째 문자 "o"로 치환
    }

    public float getScore() { return score; }
    public void setScore(float score) { this.score = score; }
}


