package com.duoshoulist.duoshoulist.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Dan on 2016/2/1.
 */
public class Comment extends BmobObject {

    private String productID;
    private String userID;
    private String text;

    public Comment(String productID, String userID, String text) {
        this.productID = productID;
        this.userID = userID;
        this.text = text;
    }

    public Comment() {
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

}
