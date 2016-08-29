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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.CommentAdapter;
import com.duoshoulist.duoshoulist.adapter.ImagesAdapter;
import com.duoshoulist.duoshoulist.bmob.Comment;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.duoshoulist.duoshoulist.bmob.MyUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "DetailActivity";

    private Handler handler = new Handler();

    MyUser currentUser;
    FeedItem feedItem;
    List<Comment> commentsData = new ArrayList<>();
    int commentCount;

    BGABanner mCubeBanner;
    // Images Rec

    // 图 标题 描述
//    @Bind(R.id.detail_image)
//    ImageView imageView;
    @Bind(R.id.detail_title)
    TextView textView_title;
    @Bind(R.id.detail_desc)
    TextView textView_desc;
    @Bind(R.id.detail_textView_comment)
    TextView textView_comment;
    @Bind(R.id.detail_toolbar)
    Toolbar toolbar;
    @Bind(R.id.detail_fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.detail_content)
    CoordinatorLayout coordinatorLayout;

    // 3 Buttons
    @Bind(R.id.detail_btn_likes)
    Button btn_like;
    @Bind(R.id.detail_btn_comments)
    Button btn_comments;
    @Bind(R.id.detail_btn_share)
    Button btn_share;

    // Image RecyclerView
    @Bind(R.id.detail_image_linearLayout)
    LinearLayout imageLinearLayout;
    @Bind(R.id.detail_images_recycler_view)
    RecyclerView imageRecyclerView;
    LinearLayoutManager imageLayoutManager;
    ImagesAdapter imageAdapter;

    // Images
    @Bind(R.id.detail_image_pb)
    ProgressBar ImageProgressBar;

    // Comment
    @Bind(R.id.detail_comment_pb)
    ProgressBar commentProgressBar;
    @Bind(R.id.detail_comment_refresh)
    ImageButton commentRefreshButton;

    // Comment RecyclerView
    @Bind(R.id.detail_comment_recycler_view)
    RecyclerView commentRecyclerView;
    LinearLayoutManager commentLayoutManager;
    CommentAdapter commentAdapter;

    // MaterialDialog
    EditText newCommentEditText;
    MaterialDialog materialDialog;
    View positiveAction;

    String objectId;
    Integer likesCount;
    String title;
    String brand;
    String price;
    String desc;
    String name;
    String image;
    List<BmobFile> imagePaths;
    String createAt;

    int screenWidth;
    int screenHight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        feedItem = (FeedItem) intent.getSerializableExtra("feedItem");

        currentUser = BmobUser.getCurrentUser(DetailActivity.this, MyUser.class);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        objectId = feedItem.getObjectId();
        likesCount = feedItem.getLikeCount();
        title = feedItem.getTitle();
        brand = feedItem.getBrand();
        price = feedItem.getPrice();
        desc = feedItem.getDesc();
        name = feedItem.getName();
        image = feedItem.getImage();
        createAt = feedItem.getCreatedAt();
        imagePaths = feedItem.getImagePaths();

        textView_title.setText(title);
        textView_desc.setText(desc);
        btn_like.setText(likesCount.toString());


        setupBanner();
        loadBackdrop();
        loadComments();
        initView();
        setupMaterialDialog();


    }

    private void setupBanner() {

        screenWidth = getScreenWidth(this);
        screenHight = getScreenHeight(this);

        mCubeBanner = (BGABanner)findViewById(R.id.banner_splash_pager);
        List<ImageView> mCubeViews = new ArrayList<>();

        ViewGroup.LayoutParams lp = mCubeBanner.getLayoutParams();
        lp.width = screenWidth;
        lp.height = screenWidth;

        mCubeBanner.setLayoutParams(lp);

        if (imagePaths != null) {
            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(lp);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mCubeViews.add(imageView);
            }

            for (int i = 0; i < imagePaths.size(); i++) {
                ImageView imageView;
                imageView = mCubeViews.get(i);
                String imagePath = imagePaths.get(i).getUrl();
                Glide.with(this).load(imagePath).crossFade().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(imageView);

                // 为每一页添加点击事件
                final int finalPosition = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击了第" + (finalPosition + 1) + "页", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(lp);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mCubeViews.add(imageView);
            Glide.with(this).load(image).crossFade().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(imageView);
        }

        mCubeBanner.setViews(mCubeViews);
        // banner.setCurrentItem(1);
    }

    private void initView() {

        btn_like.setOnClickListener(this);
        btn_comments.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        if (imagePaths != null) {
            setupImages();
        } else {
            imageLinearLayout.setVisibility(View.INVISIBLE);
        }


        // Comment RecyclerView
        commentLayoutManager = new LinearLayoutManager(DetailActivity.this, OrientationHelper.VERTICAL, true);
        commentRecyclerView.setLayoutManager(commentLayoutManager);

        // Comment
        commentRefreshButton.setOnClickListener(this);
        commentProgressBar.setVisibility(View.VISIBLE);

        // Comment RecyclerView
        commentLayoutManager = new LinearLayoutManager(DetailActivity.this, OrientationHelper.VERTICAL, true);
        commentRecyclerView.setLayoutManager(commentLayoutManager);

        // Comment RecyclerView Adapter
        commentAdapter = new CommentAdapter(DetailActivity.this, commentsData);
        commentRecyclerView.setAdapter(commentAdapter);
        commentRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupImages() {
        // Images RecyclerView
        imageLayoutManager = new LinearLayoutManager(DetailActivity.this);
        imageLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        imageRecyclerView.setLayoutManager(imageLayoutManager);

        // Image RecyclerView Adapter
        imageAdapter = new ImagesAdapter(DetailActivity.this, imagePaths);
        imageRecyclerView.setAdapter(imageAdapter);
        imageRecyclerView.setNestedScrollingEnabled(false);

        // Images
//        ImageProgressBar.setVisibility(View.VISIBLE);
    }


    private void loadBackdrop() {
//        Glide.with(this).load(image).crossFade(1500).into(imageView);


    }

    private void loadComments() {
        commentProgressBar.setVisibility(View.VISIBLE);
        commentsData.clear();

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.addWhereEqualTo("feedItem", new BmobPointer(feedItem));
        query.order("createdAt");
        query.include("user");
        query.findObjects(this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> comments) {
                Log.i(TAG, "获取评论成功");
                commentCount = comments.size();
                btn_comments.setText(commentCount + " COMMENTS");
                commentsData.addAll(comments);
                Log.i(TAG, "commentsData: " + commentsData);
                commentAdapter.notifyDataSetChanged();
//                commentRecyclerView.setAdapter(commentAdapter);
                commentProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i(TAG, "获取评论失败");
                textView_comment.setText("获取评论失败");
                commentProgressBar.setVisibility(View.GONE);
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
                if (currentUser != null) {
                    materialDialog.show();
                } else {
                    MyUser.startLoginActivity(this);
                }
                break;
            case R.id.detail_btn_share:
                break;
            case R.id.detail_comment_refresh:
                loadComments();
                break;
        }
    }


    void like() {

        feedItem.increment("likeCount");
        feedItem.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "喜欢第一步成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("life", "喜欢第一步失败");
            }
        });

        BmobRelation relation = new BmobRelation();
        relation.add(currentUser);
        feedItem.setLikes(relation);
        feedItem.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                Log.i(TAG, "喜欢第二步成功");

            }

            @Override
            public void onFailure(int arg0, String arg1) {
                Log.i("life", "喜欢第二步失败");
            }
        });
    }

    void disLike() {
        feedItem.increment("likeCount", -1);
        feedItem.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "disLike第一步成功");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.i("life", "disLike第一步失败");
            }
        });


        BmobRelation relation = new BmobRelation();
        relation.remove(currentUser);
        feedItem.setLikes(relation);
        feedItem.update(this, new UpdateListener() {

            @Override
            public void onSuccess() {
                Log.i("life", "disLike第二部成功");
            }

            @Override
            public void onFailure(int arg0, String arg1) {
                Log.i("life", "disLike第二部失败：" + arg0 + "-" + arg1);
            }
        });
    }

    //获取屏幕的宽度
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
                            final Comment comment = new Comment();
                            comment.setText(newComment);
                            comment.setFeedItem(feedItem);
                            comment.setUser(BmobUser.getCurrentUser(DetailActivity.this, MyUser.class));

                            comment.save(DetailActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Snackbar.make(coordinatorLayout, "评论成功", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    loadComments();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Snackbar.make(coordinatorLayout, "评论失败，请稍后尝试", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            });
                        } else {
                            Snackbar.make(coordinatorLayout, "请输入一些评论", Snackbar.LENGTH_LONG).show();
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
