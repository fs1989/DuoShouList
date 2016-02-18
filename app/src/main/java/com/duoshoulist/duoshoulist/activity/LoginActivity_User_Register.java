package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Register extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.register_phone_number);
        textView = (TextView) findViewById(R.id.register_login);
        button = (Button) findViewById(R.id.register_button);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity_User_Register.this, LoginActivity_User_Login.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = autoCompleteTextView.getText().toString();

                Intent intent = new Intent();
                intent.setClass(LoginActivity_User_Register.this, LoginActivity_User_Verify.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("phoneNumber", phoneNumber);
                bundle.putSerializable("loginType", "REGISTER");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


    }

}
