package com.duoshoulist.duoshoulist.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;

import org.buraktamturk.loadingview.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Success extends AppCompatActivity {

    private String TAG = "LoginActivity_User_Success";
    public static LoginActivity_User_Success loginActivity_user_success = null;

    private TextView textIndicator;
    private ListView listView;
    private Button button;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> phoneMap = new HashMap<String, String>();
    HashMap<String, String> nameMap = new HashMap<String, String>();

    private LoadingView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_success);
        loginActivity_user_success = this;


        textIndicator = (TextView) findViewById(R.id.success_text_indicator);
        loadingView = (LoadingView) findViewById(R.id.success_loadingView);
        listView = (ListView) findViewById(R.id.success_list_view);
        button = (Button) findViewById(R.id.success_button);

        String phoneNumber = (String) getIntent().getSerializableExtra("phoneNumber");
        String loginType = (String) getIntent().getSerializableExtra("loginType");

        switch (loginType) {
            case "LOGIN":
                setTitle("登陆中");
                textIndicator.setText("欢迎回来");
                loadingView.setText("登陆中");
                break;
            case "REGISTER":
                setTitle("注册中");
                textIndicator.setText("注册成功");
                loadingView.setText("注册中");
                break;
        }

        mylogin(this, loginType, phoneNumber);

        loadingView.setLoading(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setupListView();

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
        String phone = "手机号码";
        String phoneNumber = currentUser.getUsername();
        String name = "昵称";
        String nickName = currentUser.getNickName();

        phoneMap.put("title", "手机号码");
        phoneMap.put("value", phoneNumber);
        nameMap.put("title", "昵称");
        nameMap.put("value", nickName);
        mylist.add(phoneMap);
        mylist.add(nameMap);

        Log.i(TAG, phoneNumber.toString());
        Log.i(TAG, nickName.toString());
        Log.i(TAG, mylist.toString());

        simpleAdapter.notifyDataSetChanged();
        loadingView.setLoading(false);
    }


    public void mylogin(Context context, String loginTye, String phoneNumber) {
        switch (loginTye) {
            case "REGISTER":
                bmobRegister(context, phoneNumber);
                break;
            case "LOGIN":
                bmobLogin(context, phoneNumber);
                break;
        }
    }

    private void bmobLogin(Context context, String phoneNumber) {

        BmobUser bmobUser = new BmobUser();
        bmobUser.setUsername(phoneNumber);
        bmobUser.setPassword(phoneNumber + "225252928000000");
        bmobUser.login(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "BMOB登陆成功");
                setupData();
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB登录失败");
            }
        });
    }

    private void bmobRegister(final Context context, final String phoneNumber) {
        BmobUser user = new BmobUser();
        user.setUsername(phoneNumber);
        user.setPassword(phoneNumber + "225252928000000");
        //注意：不能用save方法进行注册
        user.signUp(context, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "BMOB注册成功");
                bmobLogin(context, phoneNumber);
            }

            @Override
            public void onFailure(int code, String msg) {
                Log.i(TAG, "BMOB注册失败" + msg);
            }
        });
    }


}
