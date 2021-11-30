package com.example.a_test_in_my_head;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class RankActivity extends AppCompatActivity {
    private final String showRankURL = "http://192.168.0.10:3000/showRank";
    private ListView list;
    private RankList adapter;
    private String rankSql;
    private User users[];
    private JSONObject rabkJsonObj;
    private String tag = "RankActivity";
    private int[] textViewIdArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        textViewIdArray = new int[]{R.id.rank, R.id.nickname, R.id.nBackScore, R.id.dwmtScore, R.id.guessNumScore, R.id.totalScore};
        users = new User[11];
        users[0] = new User("닉네임", "NB", "DT", "GN", "Total");
        rankSql = "SELECT * FROM score ORDER BY total DESC LIMIT 10;";      // 1~10 SQL
        rabkJsonObj = new JSONObject();

        try {
            rabkJsonObj.accumulate("rankSql", rankSql);

            RequestingServer req = new RequestingServer(this, rabkJsonObj);

            String response = req.execute(showRankURL).get();
            Log.i(tag, "result: " + response);

            if (response == null)
                return;
            String[] resResult = response.split(",");

            Log.i(tag, "overlap: " + resResult[0]);

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

                    Toast.makeText(this, "show rank", Toast.LENGTH_SHORT).show();
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
        

        adapter = new RankList(this);

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);
    }

    public void onClickShowOneRank(View v){
        switch (v.getId()){
            case R.id.nBackBtn:
//                rankSql = "SELECT * FROM score ORDER BY total DESC LIMIT 10;"
                break;
            case R.id.dwmtBtn:
                break;
            case R.id.guessNumBtn:
                break;
            default:
                break;
        }
    }

    public void onClickTotalBtn(View v){
        onRestart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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

            // 나중에 flag를 두고 All Rank, part Rank, My Rank 구분해서 작성

            // 코드를 간결하게 작성하고 싶어서 스트링 배열을 사용했습니다!
            String[] textAraay = new String[] {Integer.toString(position), users[position].getNickname(), users[position].getnBackScore(),
                                    users[position].getDwmtScore(), users[position].getGuessNumberScore(), users[position].getTotalScore()};

            // textViewIdArray = new int[]{R.id.rank, R.id.nickname, R.id.nBackScore, R.id.dwmtScore, R.id.guessNumScore, R.id.totalScore};
            for (int i=0; i<textViewIdArray.length; i++)
                ((TextView) rowView.findViewById(textViewIdArray[i])).setText(textAraay[i]);

            if (position==0)
                ((TextView) rowView.findViewById(textViewIdArray[0])).setText("랭킹");

            return rowView;
        }
    }
}