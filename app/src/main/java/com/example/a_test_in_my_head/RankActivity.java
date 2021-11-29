package com.example.a_test_in_my_head;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RankActivity extends AppCompatActivity {

//    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"} ;
    private ListView list;
    private String[] nicknames;
    private String[] nBackScores;
    private String[] dwmtScores;
    private String[] guessNumScores;
    private String[] totalScores;
    private String rankSql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        // 1~10 SQL
        rankSql = "SELECT * FROM score ORDER BY total DESC LIMIT 10;";

        RankList adapter = new RankList(this);

        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(adapter);

    }

    public class RankList extends ArrayAdapter<String> {
        private final Activity context;
        public RankList(Activity context){
            super(context, R.layout.listitem);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem, null, true);

            // flag를 두고 All Rank, part Rank, My Rank 구분해서 작성
            TextView rankView = (TextView) rowView.findViewById(R.id.rank);
            TextView nicknameView = (TextView) rowView.findViewById(R.id.nickname);
            TextView nBackScoreView = (TextView) rowView.findViewById(R.id.nBackScore);
            TextView dwmtScoreView = (TextView) rowView.findViewById(R.id.dwmtScore);
            TextView guessNumScoreView = (TextView) rowView.findViewById(R.id.guessNumScore);
            TextView totalScoreView = (TextView) rowView.findViewById(R.id.totalScore);

//            rankView.setText();


            return rowView;
        }
    }
}