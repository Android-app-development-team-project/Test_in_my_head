package com.example.a_test_in_my_head;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;

public class User implements Serializable {
    private final String setScoreURL = "http://192.168.0.10:3000/setScore";
    private String nickname;
    private String nBackScore;
    private String dwmtScore;
    private String guessNumberScore;
    private String totalScore;
    private String tag = "User";

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

    public void setScore(Activity activity, String game, int nBackScore){
        int plusScore = nBackScore - Integer.parseInt(this.nBackScore);

        try {
            Log.i(tag, "setScore...  nickname: " + nickname + ", game: " + game + ", plusScore: " + plusScore);
            JSONObject jsonObj = new JSONObject();
            jsonObj.accumulate("nickname", nickname);
            jsonObj.accumulate("game", game);                   // N_Back(NB), DWMT(DT), Guess_Number(GN)
            jsonObj.accumulate("plusScore", plusScore);

            RequestingServer req = new RequestingServer(activity, jsonObj);
            String response = req.execute(setScoreURL).get();

            Log.i(tag, "result: " + response);
            if (response == null)
                return;

            String[] resResult = response.split(",");

            switch (resResult[0]){
                case "set Score success":
                    Toast.makeText(activity, "기록 저장 완료!", Toast.LENGTH_SHORT).show();
                    break;
                case "error":
                    Toast.makeText(activity, "에러 발생!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
