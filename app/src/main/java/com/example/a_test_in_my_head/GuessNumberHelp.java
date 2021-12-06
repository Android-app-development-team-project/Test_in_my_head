package com.example.a_test_in_my_head;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GuessNumberHelp extends AppCompatActivity {
    long backKeyPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_number_help);
        Button btn = (Button) findViewById(R.id.backmainbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.GNtext);
//        TextView textView2 = findViewById(R.id.GNtext);
        String context = textView.getText().toString();
        Spannable spannableString = new SpannableString(context);
//        String context2 = textView2.getText().toString();
//        Spannable spannableString2 = new SpannableString(context2);
//        String context3 = textView.getText().toString();
//        Spannable spannableString3 = new SpannableString(context3);
        String word = "숫자찾기게임이란?";

        int start = context.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(2f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        String word2 = " easy모드";
//        int start2 = context2.indexOf(word2);
//        int end2 = start2 + word2.length();
//        spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString2.setSpan(new RelativeSizeSpan(1.5f), start2, end2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        String word3 = "hard모드";
//        int start3 = context3.indexOf(word3);
//        int end3 = start3 + word3.length();
//        spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString3.setSpan(new RelativeSizeSpan(1.5f), start3, end3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 4
        textView.setText(spannableString);
//        textView.setText(spannableString2);
//        textView.setText(spannableString3);
    }
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime=System.currentTimeMillis();
            Toast.makeText(this,"뒤로가기 버튼을 한번 더 누르시면 return home!",Toast.LENGTH_SHORT).show();
            return;
        } else { finish(); }
    }
}