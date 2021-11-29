package com.example.a_test_in_my_head;

import java.io.Serializable;

public class User implements Serializable {
    String nickname;
    String nBackScore;
    String dwmtScore;
    String guessNumberScore;
    String totalScore;

    public User(String nickName, String nBackScore, String dwmtScore, String guessNumberScore, String totalScore){
        this.nickname = nickName;
        this.nBackScore = nBackScore;
        this.dwmtScore = dwmtScore;
        this.guessNumberScore = guessNumberScore;
        this.totalScore = totalScore;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }          // 닉네임 변경 기능 고민

    public String getnBackScore() { return nBackScore; }
    public void setnBackScore(String nBackScore) { this.nBackScore = nBackScore; }

    public String getDwmtScore() { return dwmtScore; }
    public void setDwmtScore(String dwmtScore) { this.dwmtScore = dwmtScore; }

    public String getGuessNumberScore() { return guessNumberScore; }
    public void setGuessNumberScore(String guessNumberScore) { this.guessNumberScore = guessNumberScore; }

    public String getTotalScore() { return totalScore; }
    public void setTotalScore(String totalScore) { this.totalScore = totalScore; }
}
