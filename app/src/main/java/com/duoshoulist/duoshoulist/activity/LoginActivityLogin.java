package com.duoshoulist.duoshoulist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.utils.utils;

import cn.smssdk.SMSSDK;


/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivityLogin extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "LoginActivityLogin";
    public static LoginActivityLogin loginActivity_User_Login = null;

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private TextView register;

    MaterialDialog materialDialog;
    View positiveAction;

    String phoneNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        loginActivity_User_Login = this;

        // Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Views
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.login_phone_number);
        button = (Button) findViewById(R.id.login_next);
        button.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_next:
                phoneNumber = autoCompleteTextView.getText().toString();
                if (utils.isMobile(phoneNumber)) {
                    setupMaterialDialog(phoneNumber);
                    materialDialog.show();
                } else {
                    utils.hideKeyboard(this, autoCompleteTextView);
                    Snackbar.make(autoCompleteTextView, "请输入正确的手机号码", Snackbar.LENGTH_LONG).show();
                }
                break;
//            case R.id.login_register:
//                Intent intent2 = new Intent();
//                intent2.setClass(LoginActivityLogin.this, LoginActivity_User_Register.class);
//                startActivity(intent2);
//                break;
        }
    }

    private void setupMaterialDialog(String phoneNumber) {
        String text = "我们将发送验证码短信到这个号码：\r\n" + phoneNumber;
        materialDialog = new MaterialDialog.Builder(this)
                .title("确认手机号码")
                .positiveText("确定")
                .negativeText("取消")
                .iconRes(R.mipmap.ic_launcher)
                .content(text)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        sendVerifyCode(LoginActivityLogin.this, LoginActivityLogin.this.phoneNumber);
                    }
                }).build();
        positiveAction = materialDialog.getActionButton(DialogAction.POSITIVE);
    }

    public void sendVerifyCode(Context context, String phoneNumber) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivityVerify.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("phoneNumber", phoneNumber);
        intent.putExtras(bundle);
        context.startActivity(intent);
        //发送验证码
        SMSSDK.getVerificationCode("86", phoneNumber);
        Log.i(TAG, "sendVerifyCode执行了");
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
