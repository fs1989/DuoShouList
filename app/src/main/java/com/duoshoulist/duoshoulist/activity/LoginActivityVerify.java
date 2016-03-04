package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.utils.utils;

import org.buraktamturk.loadingview.LoadingView;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivityVerify extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivityVerify";
    public static LoginActivityVerify loginActivity_user_verify = null;

    private TextView textViewPhoneNumber;
    private AutoCompleteTextView autoCompleteTextView;
    private Button problem;
    private Button button;
    private LoadingView loadingView;

    private String phoneNumber;

    Handler handler = new Handler();

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
        loadingView = (LoadingView) findViewById(R.id.verify_loadingView);
        loadingView.setLoading(false);

        setupLoginListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_button:
                String verifyCode = autoCompleteTextView.getText().toString();
                SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                loadingView.setLoading(true);
                loadingView.setText("验证中");
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
            public void afterEvent(int event, int result, final Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成

                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //验证码验证成功
                            Log.i(TAG, "验证成功");
                            login();

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //发送验证码成功
                            Snackbar.make(loadingView, "智能验证，即将登陆", Snackbar.LENGTH_LONG);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //发送验证码成功
                            Log.i(TAG, "验证码发送请求成功");
                            boolean smart = (Boolean) data;
                            if (smart) {
                                //通过智能验证
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingView.setText("智能验证成功，即将登陆");
                                    }
                                }, 1500);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        login();
                                    }
                                }, 2000);
                            } else {
                                //依然走短信验证
                            }
                            Log.i(TAG, "验证码发送请求成功");
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }


                } else {
                    ((Throwable) data).printStackTrace();
                    utils.hideKeyboard(LoginActivityVerify.this, autoCompleteTextView);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.setLoading(false);
                            Snackbar.make(autoCompleteTextView, data.toString(), Snackbar.LENGTH_LONG).show();
                        }
                    }, 2000);
                    Log.i(TAG, "错误信息: " + ((Throwable) data).getMessage());
//                    login();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivitySuccess.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("phoneNumber", phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void problem() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh); //注册短信回调
    }

}
