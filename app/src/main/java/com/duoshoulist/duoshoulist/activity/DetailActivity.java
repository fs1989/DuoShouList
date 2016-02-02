/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duoshoulist.duoshoulist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.CommentAdapter;
import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.duoshoulist.duoshoulist.utils.utils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class DetailActivity extends AppCompatActivity {

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始

    FeedItem feedItem;
    List<Comment> commentData = new ArrayList<Comment>();

    ImageView imageView;
    TextView textView_title;
    TextView textView_desc;
    Button like_count;
    Button view_count;
    Button share;

    RecyclerView commentRecyclerView;
    LinearLayoutManager mLayoutManager;
    CommentAdapter commentAdapter;

    String objectId;
    Integer id;
    Integer likes;
    Integer views;
    String title;
    String brand;
    String price;
    String desc;
    String name;
    String avatar;
    String image;
    String time;
    String createAt;

    int screenWidth;
    int screenHight;

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        feedItem = (FeedItem) intent.getSerializableExtra("feedItem");

        getData();
        initView();
        bindData();
        loadBackdrop();
        setupRecyclerView();
        loadComments(objectId);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitleEnabled(false);
//        collapsingToolbar.setTitle(feedItem.getTitle());
    }

    private void setupRecyclerView() {
        commentRecyclerView = (RecyclerView) findViewById(R.id.detail_comment);
        mLayoutManager= new LinearLayoutManager(DetailActivity.this);
        commentRecyclerView.setLayoutManager(mLayoutManager);

        commentAdapter = new CommentAdapter(DetailActivity.this, commentData);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setNestedScrollingEnabled(false);
    }

    private void loadComments(String objectId) {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
//查询playerName叫“比目”的数据
        query.addWhereEqualTo("productID", objectId);
//返回50条数据，如果不加上这条语句，默认返回10条数据
//执行查询方法
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                utils.showToast(DetailActivity.this, "获取评论成功：共"+comments.size()+"条数据。");
                for (Comment comment : comments) {
                    commentData.add(comment);
                }
                commentAdapter.notifyDataSetChanged();

//                for (Comment gameScore : object) {
//                    //获得playerName的信息
//                    gameScore.getPlayerName();
//                    //获得数据的objectId信息
//                    gameScore.getObjectId();
//                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                    gameScore.getCreatedAt();
//                }
            }
            @Override
            public void onError(int code, String msg) {
                utils.showToast(DetailActivity.this, "获取评论失败："+msg);
            }
        });
    }

    void getData() {
        objectId = feedItem.getObjectId();
        id = feedItem.getId();
        likes = feedItem.getLikes();
        views = feedItem.getViews();
        title = feedItem.getTitle();
        brand = feedItem.getBrand();
        price = feedItem.getPrice();
        desc = feedItem.getDesc();
        name = feedItem.getName();
        avatar = feedItem.getAvatar();
        image = feedItem.getImage();
        time = feedItem.getTime();
        createAt = feedItem.getCreatedAt();
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.detail_image);
        textView_title = (TextView) findViewById(R.id.detail_title);
        textView_desc = (TextView) findViewById(R.id.detail_desc);
        like_count = (Button) findViewById(R.id.like_count);
        view_count = (Button) findViewById(R.id.like_count);
        share = (Button) findViewById(R.id.share_action);
    }

    private void bindData() {
        textView_title.setText(title);
        textView_desc.setText(desc);
        like_count.setText(likes.toString());
        view_count.setText(views.toString());
    }


    private void loadBackdrop() {
        Glide.with(this).load(image).crossFade(1500).into(imageView);

        screenWidth = getScreenWidth(this);
        screenHight = getScreenHeight(this);

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth;
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageView.setLayoutParams(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    void getDataFromInternet() {
        BmobQuery<FeedItem> bmobQuery = new BmobQuery<FeedItem>();
        bmobQuery.getObject(this, objectId, new GetListener<FeedItem>() {
            @Override
            public void onSuccess(FeedItem object) {
                id = object.getId();
                likes = object.getLikes();
                title = object.getTitle();
                desc = object.getDesc();
                name = object.getName();
                avatar = object.getAvatar();
                image = object.getImage();
                time = object.getTime();
                createAt = object.getCreatedAt();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
//                toast("查询失败：" + msg);
            }
        });
    }
}
