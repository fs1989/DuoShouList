package com.duoshoulist.duoshoulist.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.LoginActivitySuccess;
import com.duoshoulist.duoshoulist.activity.LoginActivityVerify;
import com.duoshoulist.duoshoulist.activity.LoginActivityLogin;

/**
 * Created by Dan on 2016-02-21.
 */
public class Dialogs {

    public void successLogin(final Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivitySuccess.class);
        context.startActivity(intent);
        new MaterialDialog.Builder(context)
                .autoDismiss(false)
                .title("登陆成功")
                .positiveText("确定")
                .iconRes(R.mipmap.ic_launcher)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (LoginActivityLogin.loginActivity_User_Login != null) {
                            LoginActivityLogin.loginActivity_User_Login.finish();
                        }
                        if (LoginActivityVerify.loginActivity_user_verify != null) {
                            LoginActivityVerify.loginActivity_user_verify.finish();
                        }
                    }
                })
                .build();
    }
}
