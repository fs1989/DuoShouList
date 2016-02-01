package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.duoshoulist.duoshoulist.R;

/**
 * Created by Dan on 2016-01-31.
 */



public class LoginActivity_3 extends AppCompatActivity {

    private AutoCompleteTextView textView;
    private Button problem;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_3);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = (AutoCompleteTextView) findViewById(R.id.code);
        problem = (Button) findViewById(R.id.login3_problem);
        button = (Button) findViewById(R.id.button);

        problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void problem() {
    }

    private void login() {
        Intent intent = new Intent();
    }

}
