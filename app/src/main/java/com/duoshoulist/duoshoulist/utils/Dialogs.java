package com.duoshoulist.duoshoulist.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Login;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Success;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Verify;

/**
 * Created by Dan on 2016-02-21.
 */
public class Dialogs {

    public void successLogin(final Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity_User_Success.class);
        context.startActivity(intent);
        new MaterialDialog.Builder(context)
                .autoDismiss(false)
                .title("登陆成功")
                .positiveText("确定")
                .iconRes(R.mipmap.ic_launcher)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (LoginActivity_User_Login.loginActivity_User_Login != null) {
                            LoginActivity_User_Login.loginActivity_User_Login.finish();
                        }
                        if (LoginActivity_User_Verify.loginActivity_user_verify != null) {
                            LoginActivity_User_Verify.loginActivity_user_verify.finish();
                        }
                    }
                })
                .build();
    }
}
