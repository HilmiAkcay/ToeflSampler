package com.example.sss.toeflsampler;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.appcompat.*;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    public void  btnDictionary_Click(View view)
    {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }

    public  void btnQuiz_Click(View view)
    {
        Intent intent = new Intent(WelcomeActivity.this, QuizActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }

    public  void  btnTranslate_Click(View view)
    {
        Intent intent = new Intent(WelcomeActivity.this, TranslateActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }


}
