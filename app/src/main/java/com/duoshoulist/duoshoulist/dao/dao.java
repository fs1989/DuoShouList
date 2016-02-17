package com.duoshoulist.duoshoulist.dao;

import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.FeedItem;

import java.util.List;

/**
 * Created by Dan on 2016/2/17.
 */
public class dao {

    /*
    主要功能
    */
    public List<FeedItem> getFeedItems() {

        return null;
    }

    public void addItem() {

    }


    /*
    评论功能
    */
    public List<Comment> getCommentsByID() {

        return null;
    }

    public void addComment(String productID, String userID, String text) {
        Comment comment = new Comment(productID, userID, text);
    }


}
