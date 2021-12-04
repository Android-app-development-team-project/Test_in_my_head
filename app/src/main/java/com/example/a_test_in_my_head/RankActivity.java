package com.example.a_test_in_my_head;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class RankActivity extends AppCompatActivity {
    private final String showAllRankURL = "http://192.168.0.10:3000/showAllRank";
    private final String showPartRankURL = "http://192.168.0.10:3000/showPartRank";
    private final String showMyRankURL = "http://192.168.0.10:3000/showMyRank";
    private ListView list;
    private RankList adapter;
    private String showRankMode;
    private User users[];
    private JSONObject rabkJsonObj;
    private int[] textViewIdArray;
    private String tag = "RankActivity";
    private String[] myRank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        textViewIdArray = new int[]{R.id.rank, R.id.nickname, R.id.nBackScore, R.id.dwmtScore, R.id.guessNumScore, R.id.totalScore};
        users = new User[11];
        users[0] = new User("닉네임", "NB", "DT", "GN", "Total");

        adapter = new RankList(this);
        list = (ListView) findViewById(R.id.listView);

        showAllRank();
    }

    public void showAllRank(){
        showRankMode = "";
        try {
            RequestingServer req = new RequestingServer(this, new JSONObject());

            String response = req.execute(showAllRankURL).get();
            Log.i(tag, "result: " + response);

            if (response == null)
                return;
            String[] resResult = response.split(",");

            Log.i(tag, "resResult[0]: " + resResult[0]);

            switch (resResult[0]){
                case "show rank":
                    int j = 1;
                    for(int i=1; i<11; i++){
                        users[i] = new User(resResult[j], resResult[j+1], resResult[j+2], resResult[j+3], resResult[j+4]);

                        Log.i(tag, users[i].getNickname());
                        Log.i(tag, users[i].getnBackScore());
                        Log.i(tag, users[i].getDwmtScore());
                        Log.i(tag, users[i].getGuessNumberScore());
                        Log.i(tag, users[i].getTotalScore());
                        j += 5;
                    }

                    Toast.makeText(this, "Show All Rank", Toast.LENGTH_SHORT).show();
                    break;
                case "error":
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            list.setAdapter(adapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClickShowAllRank(View v){
        Log.i(tag, "onClickShowTotalRank");
        showAllRank();
    }

    public void onClickShowPartRank(View v){
        switch (v.getId()){
            case R.id.nBackBtn:
                showRankMode = "NB";
                break;
            case R.id.dwmtBtn:
                showRankMode = "DT";
                break;
            case R.id.guessNumBtn:
                showRankMode = "GN";
                break;
            default:
                break;
        }

        rabkJsonObj = new JSONObject();
        try {
            rabkJsonObj.accumulate("partRank", showRankMode);

            RequestingServer req = new RequestingServer(this, rabkJsonObj);

            String response = req.execute(showPartRankURL).get();
            Log.i(tag, "result: " + response);

            if (response == null)
                return;
            String[] resResult = response.split(",");
            Log.i(tag, "resResult[0]: " + resResult[0]);

            int j = 1;
            switch (showRankMode){
                case "NB":
                    for(int i=1; i<11; i++){
                        users[i].setNickname(resResult[j]);
                        users[i].setnBackScore(resResult[j+1]);
                        Log.i(tag, users[i].getNickname());
                        Log.i(tag, users[i].getnBackScore());
                        j += 2;
                    }
                    Toast.makeText(this, "Show NB Rank", Toast.LENGTH_SHORT).show();
                    break;
                case "DT":
                    for(int i=1; i<11; i++){
                        users[i].setNickname(resResult[j]);
                        users[i].setDwmtScore(resResult[j+1]);
                        Log.i(tag, users[i].getNickname());
                        Log.i(tag, users[i].getDwmtScore());
                        j += 2;
                    }
                    Toast.makeText(this, "Show DT Rank", Toast.LENGTH_SHORT).show();
                    break;
                case "GN":
                    for(int i=1; i<11; i++){
                        users[i].setNickname(resResult[j]);
                        users[i].setGuessNumberScore(resResult[j+1]);
                        Log.i(tag, users[i].getNickname());
                        Log.i(tag, users[i].getGuessNumberScore());
                        j += 2;
                    }
                    Toast.makeText(this, "Show GN Rank", Toast.LENGTH_SHORT).show();
                    break;
                case "error":
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void onClickShowMyRank(View v){
        SharedPreferences spref = getSharedPreferences("user.pref", Context.MODE_PRIVATE);

        // public user 접근 제한!!
        if (spref.getString("nickname", "public").equals("public")){
            Toast.makeText(this, "public 유저는 접근할 수 없습니다!", Toast.LENGTH_SHORT).show();
            return;
        }

        rabkJsonObj = new JSONObject();
        showRankMode = "My";
        myRank = new String[4];
        try {
            rabkJsonObj.accumulate("nickname", spref.getString("nickname", "public"));
            RequestingServer req = new RequestingServer(this, rabkJsonObj);

            String response = req.execute(showMyRankURL).get();
            Log.i(tag, "result: " + response);

            if (response == null)
                return;
            String[] resResult = response.split(",");
            Log.i(tag, "resResult[0]: " + resResult[0]);

            switch (resResult[0]){
                case "show my rank":
                    users[1] = new User(resResult[1], resResult[2], resResult[3], resResult[4], resResult[5]);
                    for (int i=2; i<5; i++){
                        switch (i) {
                            case 2:
                                users[i] = new User(resResult[1], resResult[2], "-", "-", "-");
                                break;
                            case 3:
                                users[i] = new User(resResult[1], "-", resResult[3], "-", "-");
                                break;
                            case 4:
                                users[i] = new User(resResult[1], "-", "-", resResult[4], "-");
                                break;
                        }
                    }
                    int j = 0;
                    for(int i=6; i<10; i++)
                        myRank[j++] = resResult[i];

                    Log.i(tag, "myRank[0]: "+myRank[0] + "myRank[3]: "+myRank[3]);
                    Toast.makeText(this, "MY rank", Toast.LENGTH_SHORT).show();
                    list.setAdapter(adapter);
                    break;
                case "error":
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
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


    public class RankList extends ArrayAdapter<String> {
        private final Activity context;
        public RankList(Activity context){
            super(context, R.layout.listitem, new String[11]);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem, null, true);
            String[] textAraay;

            // 코드를 간결하게 작성하고 싶어서 스트링 배열을 사용했습니다!
            textAraay = new String[] {Integer.toString(position), users[position].getNickname(), users[position].getnBackScore(),
                                    users[position].getDwmtScore(), users[position].getGuessNumberScore(), users[position].getTotalScore()};

            // textViewIdArray = new int[]{R.id.rank, R.id.nickname, R.id.nBackScore, R.id.dwmtScore, R.id.guessNumScore, R.id.totalScore};
            for (int i=0; i<textViewIdArray.length; i++)
                ((TextView) rowView.findViewById(textViewIdArray[i])).setText(textAraay[i]);

            if (position==0)
                ((TextView) rowView.findViewById(textViewIdArray[0])).setText("랭킹");

            switch (showRankMode){
                case "NB":
                    for (int i=3; i<textViewIdArray.length; i++)
                        rowView.findViewById(textViewIdArray[i]).setVisibility(View.GONE);
                    break;
                case "DT":
                    for (int i=2; i<textViewIdArray.length; i++) {
                        if (i==3) continue;
                        rowView.findViewById(textViewIdArray[i]).setVisibility(View.GONE);
                    }
                    break;
                case "GN":
                    for (int i=2; i<textViewIdArray.length; i++) {
                        if (i==4) continue;
                        rowView.findViewById(textViewIdArray[i]).setVisibility(View.GONE);
                    }
                    break;
                case "My":
                    if (position > 4){
                        for (int i=0; i<textViewIdArray.length; i++)
                            rowView.findViewById(textViewIdArray[i]).setVisibility(View.GONE);
                        break;
                    }
                    else if (position != 0)
                        ((TextView) rowView.findViewById(textViewIdArray[0])).setText(myRank[position-1]);
                    break;
                default:
                    for (int i=2; i<textViewIdArray.length; i++)
                        rowView.findViewById(textViewIdArray[i]).setVisibility(View.VISIBLE);
                    break;
            }
            return rowView;
        }
    }
}