package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.duoshoulist.duoshoulist.R;


/**
 * Created by Dan on 2016-01-31.
 */



public class LoginActivity_2 extends AppCompatActivity {

    private AutoCompleteTextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_2);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView = (AutoCompleteTextView) findViewById(R.id.phone_number);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(textView.getText());
                Intent intent = new Intent();
                intent.setClass(LoginActivity_2.this, LoginActivity_3.class);
                startActivity(intent);
            }
        });

    }

    private void sendCode(Editable phoneNumber) {
    }

}
