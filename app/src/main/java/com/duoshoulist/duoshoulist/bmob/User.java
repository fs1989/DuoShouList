package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;

import com.duoshoulist.duoshoulist.utils.utils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Dan on 2016/1/29.
 */
public class User extends BmobUser {

    String userName;
    String userAvatar;
    User user;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser(final Context context, String userID) {

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("objectId", userID);
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                user = list.get(0);
            }
            @Override
            public void onError(int code, String msg) {
                utils.showToast(context, "查询用户失败："+msg);
            }
        });
        return user;
    }
}
