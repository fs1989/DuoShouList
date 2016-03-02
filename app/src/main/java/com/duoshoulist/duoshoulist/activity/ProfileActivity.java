package com.duoshoulist.duoshoulist.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.MainFragmentAdapter;
import com.duoshoulist.duoshoulist.bmob.MyUser;
import com.duoshoulist.duoshoulist.fragment.ProfileLikeFragment;
import com.duoshoulist.duoshoulist.fragment.ProfileOriginalFragment;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dan on 2016/2/1.
 */
public class ProfileActivity extends AppCompatActivity {

    private String TAG = "ProfileActivity";

    Toolbar toolbar;
    private CircleImageView avatar;
    private TextView nickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nickName = (TextView) findViewById(R.id.profile_nick_name);
        avatar = (CircleImageView) findViewById(R.id.profile_avatar);

        setupToolbar();
        setupData();
    }

    private void setupToolbar() {
        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile_logoff:
                        MyUser.logOut(ProfileActivity.this);   //清除缓存用户对象
                        BmobUser user = BmobUser.getCurrentUser(ProfileActivity.this, MyUser.class);
                        Log.i(TAG, "user after logoff clicked:" + user);
                        ProfileActivity.this.finish();
                        break;
                    case R.id.profile_setting:
                        break;
                }
                return true;
            }
        });


        // ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        if (viewPager != null) {
            MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
            adapter.addFragment(new ProfileLikeFragment(), "喜欢");
            adapter.addFragment(new ProfileOriginalFragment(), "原创");
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.profile_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupData() {

        String userNickName = (String) BmobUser.getObjectByKey(ProfileActivity.this, "nickName");
        String userAvatar = (String) BmobUser.getObjectByKey(ProfileActivity.this, "avatar");

        if (userNickName != null) {
            nickName.setText(userNickName);
        } else {
            nickName.setText("没有昵称");
        }

        if (userAvatar != null) {
            Glide.with(this).load(userAvatar).crossFade().into(avatar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
