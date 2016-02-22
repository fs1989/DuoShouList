package com.duoshoulist.duoshoulist.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


public class LoginActivity_User_Verify extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivity_User_Verify";
    public static LoginActivity_User_Verify loginActivity_user_verify = null;

    private TextView textIndicator;
    private TextView textViewPhoneNumber;
    private AutoCompleteTextView autoCompleteTextView;
    private Button problem;
    private Button button;

    private String phoneNumber;
    private String loginType;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify);
        loginActivity_user_verify = this;

        phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");
        loginType = (String) getIntent().getSerializableExtra("loginType");

        // Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.verify_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View
        textIndicator = (TextView) findViewById(R.id.verify_text_indicator);
        textViewPhoneNumber = (TextView) findViewById(R.id.verify_phone_number);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.verify_code);
        button = (Button) findViewById(R.id.verify_button);
        problem = (Button) findViewById(R.id.verify_problem);
        button.setOnClickListener(this);
        problem.setOnClickListener(this);
        textViewPhoneNumber.setText(phoneNumber);

        setupLoginListener();
    }

    private void problem() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button:
                verifyCode = autoCompleteTextView.getText().toString();
//                SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                login();
                break;
            case R.id.verify_problem:
                problem();
                break;
        }
    }


    private EventHandler eh;
    private void setupLoginListener() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //验证码验证成功
                        login();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //发送验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    utils.hideKeyboard(LoginActivity_User_Verify.this, autoCompleteTextView);
                    Snackbar.make(autoCompleteTextView, data.toString(), Snackbar.LENGTH_LONG).show();
                    login();
                    Log.i(TAG, "错误信息: " + ((Throwable) data).getMessage());
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void login() {
        User user = new User();
        user.mylogin(LoginActivity_User_Verify.this, loginType, phoneNumber);
    }

}
