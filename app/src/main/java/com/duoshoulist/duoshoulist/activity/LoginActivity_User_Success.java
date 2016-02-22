package com.duoshoulist.duoshoulist.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.bmob.User;

import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobUser;

/**
 * Created by Dan on 2016-01-31.
 */


public class LoginActivity_User_Success extends AppCompatActivity {

    private String TAG = "LoginActivity_User_Success";
    public static LoginActivity_User_Success loginActivity_user_success = null;

    private ListView listView;
    private Button button;
    SimpleAdapter simpleAdapter;

    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_success);
        loginActivity_user_success = this;

        listView = (ListView) findViewById(R.id.success_list_view);
        button = (Button) findViewById(R.id.success_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.i(TAG, "onClick方法执行了");
            }
        });
        setupListView();
        setupData();
    }

    private void setupData() {
        User currentUser = BmobUser.getCurrentUser(this, User.class);

        String phone = "手机号码";
        String phoneNumber = currentUser.getUsername();
        String name = "昵称";
        String nickName = currentUser.getNickName();

        map.put(phone, phoneNumber);
        map.put(name, nickName);
        mylist.add(map);
        Log.i(TAG, mylist.toString());
        simpleAdapter.notifyDataSetChanged();
    }

    private void setupListView() {
         simpleAdapter = new SimpleAdapter(
                this, //没什么解释
                mylist,//数据来源
                R.layout.listview_success,//ListItem的XML实现
                //动态数组与ListItem对应的子项
                new String[]{"ItemTitle", "ItemText"},
                //ListItem的XML文件里面的两个TextView ID
                new int[]{R.id.success_title, R.id.success_value});
        //添加并且显示
        listView.setAdapter(simpleAdapter);
    }

}
