package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.MainFragmentAdapter;
import com.duoshoulist.duoshoulist.fragment.MainFragment;
import com.duoshoulist.duoshoulist.fragment.MyProfileFragment;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

import cn.bmob.v3.Bmob;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    // BMOB
    private String bmobKey = "02c7c284a874541120575b2d2e839059";

    // 微信 SDK
    private static final String APP_ID = "";
    private IWXAPI api;

    // ShareSDK SMS
    private String mobAppKey = "f87379172d4e";
    private String mobAppSecret = "cbd32bc5c4d5381d63047311262dce49";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bmob SDK
        Bmob.initialize(this, bmobKey);

        // ShareSDK SMS
        SMSSDK.initSDK(this, mobAppKey, mobAppSecret);

        // 微信 SDK


        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // ActionBar
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)

        {
            setupDrawerContent(navigationView);
        }

        // ViewPager
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null)

        {
            setupViewPager(viewPager);

        }

        // Tab Layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        addFlotingActionButton();

    }

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        api.registerApp(APP_ID);
    }


    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "主页");
        adapter.addFragment(new MainFragment(), "朋友");
//        adapter.addFragment(new MyProfileFragment(), "我的");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void addFlotingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity_User_Register.class);
                startActivity(intent);

//                RegisterPage registerPage = new RegisterPage();
//                registerPage.setRegisterCallback(new EventHandler() {
//                    public void afterEvent(int event, int result, Object data) {
//                        // 解析注册结果
//                        if (result == SMSSDK.RESULT_COMPLETE) {
//                            @SuppressWarnings("unchecked")
//                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//                            String country = (String) phoneMap.get("country");
//                            String phone = (String) phoneMap.get("phone");
//
//                            // 提交用户信息
////                            registerUser(country, phone);
//                        }
//                    }
//                });
//                registerPage.show(MainActivity.this);
            }
        });
    }

}