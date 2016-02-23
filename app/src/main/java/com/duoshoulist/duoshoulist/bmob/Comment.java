package com.duoshoulist.duoshoulist.bmob;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Dan on 2016/2/1.
 */
public class Comment extends BmobObject {

    private String TAG = "Comment";

    private String productID;
    private String userID;
    private String text;

    private User user;
    private String avatar;
    private String nickName;


    public Comment(String productID, String userID, String text) {
        this.productID = productID;
        this.userID = userID;
        this.text = text;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAvatar() {
        user.getAvatar();
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        user.getNickName();
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
