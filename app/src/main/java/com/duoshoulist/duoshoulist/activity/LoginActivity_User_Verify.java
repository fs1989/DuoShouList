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
import android.widget.TextView;
import android.widget.Toast;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Verify extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;
    private TextView textViewPhoneNumber;
    private AutoCompleteTextView AutoCompleteTextView;
    private Button problem;
    private Button button;

    private String phoneNumber;
    private String loginTye;
    private String verifyCode;

    EventHandler eh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verify);

        phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");
        loginTye = (String) getIntent().getSerializableExtra("loginType");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupLoginListener();

        linearLayout = (LinearLayout) findViewById(R.id.verify_linearlayout);
        textViewPhoneNumber = (TextView) findViewById(R.id.verify_phone_number);
        AutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.verify_code);
        problem = (Button) findViewById(R.id.verify_problem);
        button = (Button) findViewById(R.id.verify_button);

        textViewPhoneNumber.setText(phoneNumber);
        problem.setOnClickListener(this);
        button.setOnClickListener(this);
        //发送验证码
        SMSSDK.getVerificationCode("86", phoneNumber);
    }

    private void setupLoginListener() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        snackbar("验证成功");
                        login(data);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        snackbar("获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    snackbar("Error");
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    private void login(Object data) {
        boolean smart = (Boolean) data;
        if (smart) {
            //通过智能验证
            finish();
            Snackbar.make(linearLayout, "智能登陆成功", Snackbar.LENGTH_LONG).show();
        } else {
            switch (loginTye) {
                case "REGISTER":
                    bmobRegister(phoneNumber);
                    break;
                case "LOGIN":
                    bmobLogin(phoneNumber);
                    break;
            }
        }
    }

    private void problem() {
    }


    private void bmobLogin(String phoneNumber) {

        BmobUser.loginByAccount(this, phoneNumber, phoneNumber + "225252928000000", new LogInListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                // TODO Auto-generated method stub
                if (user != null) {
                    snackbar("BMOB登陆成功");
                }
            }
        });
    }

    private void bmobRegister(String phoneNumber) {
        BmobUser bu = new BmobUser();
        bu.setUsername(phoneNumber);
        bu.setPassword(phoneNumber + "225252928000000");
        //注意：不能用save方法进行注册
        bu.signUp(this, new SaveListener() {
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                snackbar("BMOB注册成功");
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                snackbar("BMOB注册失败:" + msg);
            }
        });
    }

    void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    void snackbar(String text) {
        Snackbar.make(linearLayout, text, Snackbar.LENGTH_LONG).show();
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
                snackbar(loginTye);
                verifyCode = AutoCompleteTextView.getText().toString();
//                SMSSDK.submitVerificationCode("86", phoneNumber, verifyCode);
                switch (loginTye) {
                    case "REGISTER":
                        bmobRegister(phoneNumber);
                        break;
                    case "LOGIN":
                        bmobLogin(phoneNumber);
                        break;
                }
                break;
            case R.id.verify_problem:
                problem();
                break;
        }

    }
}
