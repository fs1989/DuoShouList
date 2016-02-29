package com.duoshoulist.duoshoulist.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;

import org.buraktamturk.loadingview.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivitySuccess extends AppCompatActivity {

    private String TAG = "LoginActivitySuccess";
    public static LoginActivitySuccess loginActivity_user_success = null;

    private String phoneNumber;

    private LoadingView loadingView;
    private Toolbar toolbar;
    private CircleImageView avatar;
    private Button button;
    private ListView listView;
    SimpleAdapter simpleAdapter;

    private ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> phoneMap = new HashMap<String, String>();
    private HashMap<String, String> nameMap = new HashMap<String, String>();

    android.os.Handler myHandler = new android.os.Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_success);
        loginActivity_user_success = this;

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.success_toolbar);
        toolbar.setTitle("登陆中");
        setSupportActionBar(toolbar);

        // View
        loadingView = (LoadingView) findViewById(R.id.success_loadingView);
        listView = (ListView) findViewById(R.id.success_list_view);
        avatar = (CircleImageView) findViewById(R.id.success_avatar);
        button = (Button) findViewById(R.id.success_button);

        phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");

        setupListView();
        checkUser();

        loadingView.setLoading(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setupListView() {
        simpleAdapter = new SimpleAdapter(
                this, //没什么解释
                mylist,//数据来源
                R.layout.listview_success,//ListItem的XML实现
                //动态数组与ListItem对应的子项
                new String[]{"title", "value"},
                //ListItem的XML文件里面的两个TextView ID
                new int[]{R.id.success_title, R.id.success_value});
        //添加并且显示
        listView.setAdapter(simpleAdapter);
    }

    private void setupData() {
        User currentUser = BmobUser.getCurrentUser(this, User.class);
        String phoneNumber = currentUser.getUsername();
        String nickName = currentUser.getNickName();
        String avatarAddress = currentUser.getAvatar();

        phoneMap.put("title", "手机号码");
        phoneMap.put("value", phoneNumber);
        nameMap.put("title", "昵称");
        nameMap.put("value", nickName);
        mylist.add(phoneMap);
        mylist.add(nameMap);

        Log.i(TAG, "phoneNumber: " + phoneNumber);
        Log.i(TAG, "nickName: " + nickName);
        Log.i(TAG, mylist.toString());

        if (avatar != null) {
            Glide.with(this).load(avatarAddress).crossFade().into(avatar);
        }

        toolbar.setTitle("登陆成功");
        loadingView.setLoading(false);
    }

    private void checkUser() {
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", phoneNumber);
        query.findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> object) {
                int result = object.size();
                Log.i(TAG, "是否查询到用户，Result的大小为" + result);
                switch (result) {
                    case 1:
                        Log.i(TAG, "开始登陆" + phoneNumber);
                        bmobLogin(LoginActivitySuccess.this, phoneNumber);
                        break;
                    case 0:
                        Log.i(TAG, "开始注册" + phoneNumber);
                        bmobRegister(LoginActivitySuccess.this, phoneNumber);
                        break;
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Log.i(TAG, "查询用户是否存在失败：" + msg);
            }
        });
    }

    private void bmobLogin(Context context, final String phoneNumber) {

        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(phoneNumber);
        bmobUser.setPassword(phoneNumber + "225252928000000");
        bmobUser.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Snackbar.make(loadingView, "自动登陆成功", Snackbar.LENGTH_LONG).show();
                Log.i(TAG, "BMOB登陆成功" + phoneNumber);
                setupData();
                close();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB登录失败" + phoneNumber);
                Log.i(TAG, msg);
                finish();
            }
        });
    }

    private void close() {
        if (LoginActivityLogin.class != null) {
            LoginActivityLogin.loginActivity_User_Login.finish();
        }
        if (LoginActivityVerify.class != null) {
            LoginActivityVerify.loginActivity_user_verify.finish();
        }
    }

    private void bmobRegister(final Context context, final String phoneNumber) {
        BmobUser user = new BmobUser();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber + "225252928000000");
        //注意：不能用save方法进行注册
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "BMOB注册成功" + phoneNumber);
                bmobLogin(context, phoneNumber);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB注册失败" + msg);
                Log.i(TAG, "ErrorCode: " + code);
            }
        });
    }

    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            Message msg = new Message();
            msg.what = 1;
            myHandler.sendMessage(msg);

        }
    }
}
