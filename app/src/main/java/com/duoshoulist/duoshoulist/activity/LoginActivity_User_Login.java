package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;


/**
 * Created by Dan on 2016-01-31.
 */



public class LoginActivity_User_Login extends AppCompatActivity {

    private AutoCompleteTextView inputPhoneNumber;
    private Button button;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputPhoneNumber = (AutoCompleteTextView) findViewById(R.id.login_phone_number);
        button = (Button) findViewById(R.id.register_button);
        register = (TextView) findViewById(R.id.login_register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode(inputPhoneNumber.getText().toString());
                Intent intent = new Intent();
                intent.setClass(LoginActivity_User_Login.this, LoginActivity_User_Verify.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity_User_Login.this, LoginActivity_User_Register.class);
                startActivity(intent);
            }
        });

    }

    private void sendCode(String phoneNumber) {
    }

}
