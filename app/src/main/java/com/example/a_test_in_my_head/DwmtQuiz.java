package com.example.a_test_in_my_head;

public class DwmtQuiz {
    private int id;
    private String question;
    private String[] answerArray;
    private int rightAnswer;


    public DwmtQuiz(int id, String question, String answer1, String answer2, String answer3, String answer4, int rightAnswer){
        this.id = id;
        this.question = question;
        this.answerArray = new String[] {answer1, answer2, answer3, answer4};
        this.rightAnswer = rightAnswer;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswerArray(int i) {
        if (i < 0 || i > 3)
            return "index error";
        return answerArray[i];
    }

    public int getRightAnswer() { return rightAnswer; }
}
