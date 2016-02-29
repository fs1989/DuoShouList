package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;
import android.content.Intent;

import com.duoshoulist.duoshoulist.activity.LoginActivityLogin;

import cn.bmob.v3.BmobUser;

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
        intent.setClass(context, LoginActivityLogin.class);
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
