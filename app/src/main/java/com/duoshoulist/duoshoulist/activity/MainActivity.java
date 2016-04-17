package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.MainFragmentAdapter;
import com.duoshoulist.duoshoulist.bmob.MyUser;
import com.duoshoulist.duoshoulist.fragment.SelectedFragment;
import com.duoshoulist.duoshoulist.fragment.MainFragment;
import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.smssdk.SMSSDK;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    public static BottomBar mBottomBar;
    private FragNavController mNavController;

    public static CoordinatorLayout coordinatorLayout;


    // BMOB
    private String bmobKey = "02c7c284a874541120575b2d2e839059";

    // ShareSDK SMS
    //    private String mobAppKey = "f87379172d4e";
    //    private String mobAppSecret = "cbd32bc5c4d5381d63047311262dce49";
    private String mobAppKey = "f3fc6baa9ac4";
    private String mobAppSecret = "7f3dedcb36d92deebcb373af921d635a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Bmob SDK
        Bmob.initialize(this, bmobKey);

        // ShareSDK SMS
        SMSSDK.initSDK(this, mobAppKey, mobAppSecret);

        // CoordinatorLayout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main);

        // ActionBar
//        final ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);

        setupBottomBar(savedInstanceState);
        addFlotingActionButton();

        //  Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupBottomBar(Bundle savedInstanceState) {

        final List<Fragment> fragments = new ArrayList<>(4);
        fragments.add(new SelectedFragment());
        fragments.add(new SelectedFragment());
        fragments.add(new MainFragment());
        fragments.add(new SelectedFragment());
        mNavController = new FragNavController(getSupportFragmentManager(), R.id.main, fragments);

//        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar = BottomBar.attachShy(coordinatorLayout, findViewById(R.id.main_nestedScrollView), savedInstanceState);

        // Show all titles even when there's more than three tabs.
//        mBottomBar.useFixedMode();
//        mBottomBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        mBottomBar.setItems(
                new BottomBarTab(R.drawable.ic_home_white_24dp, "精选"),
                new BottomBarTab(R.drawable.ic_people_white_24dp, "原创"),
                new BottomBarTab(R.drawable.ic_format_list_numbered_white_24dp, "清单"),
                new BottomBarTab(R.drawable.ic_account_circle_white_24dp, "我的")
        );


        // Listen for tab changes
        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int position) {
                // The user selected a tab at the specified position
                mNavController.switchTab(position);
            }

            @Override
            public void onTabReSelected(int position) {
                // The user reselected a tab at the specified position!
            }
        });

        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.colorPrimaryDark));
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new SelectedFragment(), "剁手精选");
        adapter.addFragment(new SelectedFragment(), "朋友原创");
//        adapter.addFragment(new MyProfileFragment(), "我的");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_profile:
                MyUser user = BmobUser.getCurrentUser(this, MyUser.class);
                Log.i(TAG, "user = " + user);
                if (user != null) {
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    MyUser.startLoginActivity(this);
                }

                break;
            case R.id.main_settings:
                if (mBottomBar.isShown()) {
                    mBottomBar.hide();
                } else {
                    mBottomBar.show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }


    private void addFlotingActionButton() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MyUser.getCurrentUser(MainActivity.this) == null) {
//                    MyUser.startLoginActivity(MainActivity.this);
//                } else {
//                    Intent intent = new Intent(MainActivity.this, PostActivityOne.class);
//                    startActivity(intent);
//                }
//            }
//        });
    }

}