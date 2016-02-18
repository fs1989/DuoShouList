package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.duoshoulist.duoshoulist.R;

/**
 * Created by Dan on 2016-01-31.
 */



public class LoginActivity_User_Verify extends AppCompatActivity {

    private LinearLayout linearLayout;
    private AutoCompleteTextView textView;
    private Button problem;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = (LinearLayout) findViewById(R.id.verify_linearlayout);
        textView = (AutoCompleteTextView) findViewById(R.id.verify_code);
        problem = (Button) findViewById(R.id.verify_problem);
        button = (Button) findViewById(R.id.register_button);

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
        Snackbar.make(linearLayout, "登陆成功", Snackbar.LENGTH_LONG).show();
    }

}
