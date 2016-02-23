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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.CommentAdapter;
import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.duoshoulist.duoshoulist.bmob.User;
import com.duoshoulist.duoshoulist.view.FullyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "DetailActivity";

    private Handler handler = new Handler();

    FeedItem feedItem;
    List<Comment> commentData = new ArrayList<Comment>();
    int commentCount;

    // 图 标题 描述
    ImageView imageView;
    TextView textView_title;
    TextView textView_desc;
    TextView textView_comment;
    CoordinatorLayout coordinatorLayout;

    // 3 Buttons
    Button btn_like;
    Button btn_comments;
    Button btn_share;

    // Comment
    ProgressBar commentProgressBar;
    ImageButton commentRefreshButton;

    // Comment RecyclerView
    RecyclerView commentRecyclerView;
    FullyLinearLayoutManager mLayoutManager;
    CommentAdapter commentAdapter;

    // MaterialDialog
    EditText newCommentEditText;
    MaterialDialog materialDialog;
    View positiveAction;

    int comment_count;

    String objectId;
    Integer likes;
    String title;
    String brand;
    String price;
    String desc;
    String name;
    String image;
    String createAt;

    int screenWidth;
    int screenHight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        Intent intent = getIntent();
        feedItem = (FeedItem) intent.getSerializableExtra("feedItem");

        initView();
        getData();
        bindData();
        loadBackdrop();
        setupMaterialDialog();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new Thread() {
            @Override
            public void run() {
                loadComments(objectId);
            }
        }.start();
    }

    private void initView() {
        // Main
        imageView = (ImageView) findViewById(R.id.detail_image);
        textView_title = (TextView) findViewById(R.id.detail_title);
        textView_desc = (TextView) findViewById(R.id.detail_desc);
        textView_comment = (TextView) findViewById(R.id.detail_textView_comment);

        btn_like = (Button) findViewById(R.id.detail_btn_likes);
        btn_comments = (Button) findViewById(R.id.detail_btn_comments);
        btn_share = (Button) findViewById(R.id.detail_btn_share);

        btn_like.setOnClickListener(this);
        btn_comments.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        // Comment
        commentRefreshButton = (ImageButton) findViewById(R.id.detail_comment_refresh);
        commentProgressBar = (ProgressBar) findViewById(R.id.detail_comment_pb);
        commentRefreshButton.setOnClickListener(this);
        commentProgressBar.setVisibility(View.VISIBLE);


        // Comment RecyclerView
        commentRecyclerView = (RecyclerView) findViewById(R.id.detail_comment);
        mLayoutManager = new FullyLinearLayoutManager(DetailActivity.this, OrientationHelper.VERTICAL, true);
        commentRecyclerView.setLayoutManager(mLayoutManager);

        // Comment RecyclerView Adapter
        commentAdapter = new CommentAdapter(DetailActivity.this, commentData);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setNestedScrollingEnabled(false);
    }


    void getData() {
        objectId = feedItem.getObjectId();
        likes = feedItem.getLikes();
        title = feedItem.getTitle();
        brand = feedItem.getBrand();
        price = feedItem.getPrice();
        desc = feedItem.getDesc();
        name = feedItem.getName();
        image = feedItem.getImage();
        createAt = feedItem.getCreatedAt();
    }


    private void bindData() {
        textView_title.setText(title);
        textView_desc.setText(desc);
        btn_like.setText(likes.toString());
    }

    private void loadBackdrop() {
        Glide.with(this).load(image).crossFade(1500).into(imageView);

        screenWidth = getScreenWidth(this);
        screenHight = getScreenHeight(this);

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth;

        imageView.setLayoutParams(lp);
    }

    private void loadComments(String objectId) {
        commentProgressBar.setVisibility(View.VISIBLE);
        commentData.clear();
        commentAdapter.notifyDataSetChanged();
        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("productID", objectId);
        query.findObjects(this, new FindListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                btn_comments.setText(comments.size() + " COMMENTS");
                commentCount = comments.size();
                for (Comment comment : comments) {
                    updateUser(DetailActivity.this, comment, comment.getUserID());
                }
                commentProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError(int code, String msg) {
                commentProgressBar.setVisibility(View.GONE);
                textView_comment.setText("评论加载失败");
            }
        });
    }

    public void updateUser(Context context, final Comment comment, String userID) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", userID);
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                User user = list.get(0);
                comment.setUser(user);
                comment.setNickName(user.getNickName());
                comment.setAvatar(user.getAvatar());
                commentData.add(comment);
                commentAdapter.notifyDataSetChanged();
                Log.i(TAG, "更新成功" + comment.getObjectId());
                Log.i(TAG, "更新成功Adapter已经更新");
            }

            @Override
            public void onError(int code, String msg) {
                Log.v(TAG, "查询用户失败：" + msg);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_detail, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_btn_likes:
                break;
            case R.id.detail_btn_comments:
                if (BmobUser.getCurrentUser(this) != null) {
                    materialDialog.show();
                } else {
                    User.startLoginActivity(this);
                }
                break;
            case R.id.detail_btn_share:
                break;
            case R.id.detail_comment_refresh:
                loadComments(objectId);
                break;
        }
    }

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


    private void setupMaterialDialog() {

        materialDialog = new MaterialDialog.Builder(this)
                .title("评论")
                .customView(R.layout.material_dialog_detail_new_comment, true)
                .positiveText("提交评论")
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newComment = newCommentEditText.getText().toString();
                        if (newComment != null) {
                            Comment comment = new Comment(objectId, BmobUser.getCurrentUser(DetailActivity.this).getObjectId(), newComment);
                            comment.save(DetailActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Snackbar.make(coordinatorLayout, "评论成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Snackbar.make(coordinatorLayout, "评论失败，请稍后尝试", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            });
                        } else {
                            Snackbar.make(coordinatorLayout, "请输入一些评论", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                }).build();
        positiveAction = materialDialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        newCommentEditText = (EditText) materialDialog.getCustomView().findViewById(R.id.new_comment_edit_text);
        newCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        positiveAction.setEnabled(false); // disabled by default
    }
}
