package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.MainFragmentAdapter;
import com.duoshoulist.duoshoulist.bmob.User;
import com.duoshoulist.duoshoulist.fragment.MainFragment;

import cn.bmob.v3.BmobUser;

/**
 * Created by Dan on 2016/2/1.
 */
public class ProfileActivity extends AppCompatActivity {

    private User user;

    Toolbar toolbar;
    private ImageView avatar;
    private TextView realName;
    private TextView nickName;
    private TextView status;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupToolbar();

        Intent intent = new Intent();
//        user = (User) intent.getSerializableExtra("user");

        user = BmobUser.getCurrentUser(this, User.class);

        realName = (TextView) findViewById(R.id.profile_real_name);
        nickName = (TextView) findViewById(R.id.profile_nick_name);
        status = (TextView) findViewById(R.id.profile_status);
        avatar = (ImageView) findViewById(R.id.profile_avatar);

        setupData();

    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "主页");
        adapter.addFragment(new MainFragment(), "朋友");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void setupData() {

        if (user.getRealName() != null) {
            realName.setText(user.getRealName());
        } else {
            realName.setText("点击编辑你的名字");
        }

        if (user.getNickName() != null) {
            nickName.setText(user.getNickName());
        } else {
            nickName.setText("点击编辑你的昵称");
        }

        if (user.getNickName() != null) {
            status.setText(user.getNickName());
        } else {
            status.setText("点击编辑你的心情");
        }

        if (user.getAvatar() != null) {
            Glide.with(this).load(user.getAvatar()).crossFade().into(avatar);
        }
    }
}
