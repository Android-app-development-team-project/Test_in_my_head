package com.example.a_test_in_my_head;

import java.io.Serializable;

public class User implements Serializable {
    String nickname;
    int nBackScore;
    int dwmtScore;
    int guessNumberScore;
    int totalScore;

    public User(String nickName, int nBackScore, int dwmtScore, int guessNumberScore, int totalScore){
        this.nickname = nickName;
        this.nBackScore = nBackScore;
        this.dwmtScore = dwmtScore;
        this.guessNumberScore = guessNumberScore;
        this.totalScore = totalScore;
    }

    public void setUser(String nickName, int nBackScore, int dwmtScore, int guessNumberScore, int totalScore){
        this.nickname = nickName;
        this.nBackScore = nBackScore;
        this.dwmtScore = dwmtScore;
        this.guessNumberScore = guessNumberScore;
        this.totalScore = totalScore;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }          // 닉네임 변경 기능 고민

    public int getnBackScore() { return nBackScore; }
    public void setnBackScore(int nBackScore) { this.nBackScore = nBackScore; }

    public int getDwmtScore() { return dwmtScore; }
    public void setDwmtScore(int dwmtScore) { this.dwmtScore = dwmtScore; }

    public int getGuessNumberScore() { return guessNumberScore; }
    public void setGuessNumberScore(int guessNumberScore) { this.guessNumberScore = guessNumberScore; }

    public int getTotalScore() { return totalScore; }

    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
}
