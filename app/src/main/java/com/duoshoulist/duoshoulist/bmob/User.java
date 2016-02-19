package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Dan on 2016/1/29.
 */
public class User extends BmobUser{

    final String TAG = "User";

    String realName;
    String nickName;
    String avatar;
    String sex;
    String status;
    User user;
    Context context;

    public User(Context context) {
        this.context = context;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUserbyId(String userId) {

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", userId);
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                user = list.get(0);
            }

            @Override
            public void onError(int code, String msg) {
            }
        });
        return user;
    }

}
