package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;
import com.duoshoulist.duoshoulist.utils.utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Register extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivity_User_Register";
    public static LoginActivity_User_Register loginActivity_user_register = null;

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        loginActivity_user_register = this;

        // Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.register_phone_number);
        button = (Button) findViewById(R.id.register_button);
        textView = (TextView) findViewById(R.id.register_login);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                String phoneNumber = autoCompleteTextView.getText().toString();
                if (utils.isMobile(phoneNumber)) {
                    User user = new User();
                    user.sendVerifyCode(LoginActivity_User_Register.this, phoneNumber, "REGISTER");
                } else {
                    utils.hideKeyboard(this, autoCompleteTextView);
                    Snackbar.make(autoCompleteTextView, "请输入正确的手机号码", Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.register_login:
                Intent intent = new Intent();
                intent.setClass(LoginActivity_User_Register.this, LoginActivity_User_Login.class);
                startActivity(intent);
                break;
        }
    }
}
