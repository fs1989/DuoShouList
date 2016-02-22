package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Login;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Success;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Verify;
import com.duoshoulist.duoshoulist.utils.Dialogs;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.SMSSDK;

/**
 * Created by Dan on 2016/1/29.
 */
public class User extends BmobUser {

    final String TAG = "User";

    String nickName;
    String avatar;
    String sex;

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity_User_Login.class);
        context.startActivity(intent);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

}
