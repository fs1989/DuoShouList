package com.duoshoulist.duoshoulist.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by Dan on 2016/2/1.
 */
public class Comment extends BmobObject {

    private String TAG = "Comment";

    private String text;
    private MyUser user;
    private FeedItem feedItem;

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public FeedItem getFeedItem() {
        return feedItem;
    }

    public void setFeedItem(FeedItem feedItem) {
        this.feedItem = feedItem;
    }
}
