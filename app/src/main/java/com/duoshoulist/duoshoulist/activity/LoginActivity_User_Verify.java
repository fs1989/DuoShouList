package com.duoshoulist.duoshoulist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;
import com.duoshoulist.duoshoulist.utils.utils;

import org.buraktamturk.loadingview.LoadingView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Verify extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivity_User_Verify";
    public static LoginActivity_User_Verify loginActivity_user_verify = null;

    private TextView textViewPhoneNumber;
    private AutoCompleteTextView autoCompleteTextView;
    private Button problem;
    private Button button;

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify);
        loginActivity_user_verify = this;

        phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");

        // Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.verify_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // View
        textViewPhoneNumber = (TextView) findViewById(R.id.verify_phone_number);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.verify_code);
        button = (Button) findViewById(R.id.verify_button);
        problem = (Button) findViewById(R.id.verify_problem);
        button.setOnClickListener(this);
        problem.setOnClickListener(this);
        textViewPhoneNumber.setText(phoneNumber);

        setupLoginListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button:
                String verifyCode = autoCompleteTextView.getText().toString();
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
                    Log.i(TAG, "错误信息: " + ((Throwable) data).getMessage());
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity_User_Success.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("phoneNumber", phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
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

}
