package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Login;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Success;
import com.duoshoulist.duoshoulist.activity.LoginActivity_User_Verify;
import com.duoshoulist.duoshoulist.utils.Dialogs;

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

    public void mylogin(Context context, String loginTye, String phoneNumber) {
        switch (loginTye) {
            case "REGISTER":
                bmobRegister(context, phoneNumber);
                break;
            case "LOGIN":
                bmobLogin(context, phoneNumber);
                break;
        }
    }

    private void bmobLogin(final Context context, String phoneNumber) {

        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(phoneNumber);
        bmobUser.setPassword(phoneNumber + "225252928000000");
        bmobUser.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "BMOB登陆成功");
                Intent intent = new Intent(context, LoginActivity_User_Success.class);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB登录失败");
            }
        });
    }

    private void bmobRegister(final Context context, final String phoneNumber) {
        BmobUser user = new BmobUser();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber + "225252928000000");
        //注意：不能用save方法进行注册
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "BMOB注册成功");
                bmobLogin(context, phoneNumber);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB注册失败" + msg);
            }
        });
    }


    public void sendVerifyCode(Context context, String phoneNumber, String loginType) {

        Intent intent = new Intent();
        intent.setClass(context, LoginActivity_User_Verify.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("phoneNumber", phoneNumber);
        bundle.putSerializable("loginType", loginType);
        intent.putExtras(bundle);
        context.startActivity(intent);
        //发送验证码
        SMSSDK.getVerificationCode("86", phoneNumber);
    }




    private Boolean isUnique(String name) {
        Boolean unique;

        return true;
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
