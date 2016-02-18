package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;


/**
 * Created by Dan on 2016-01-31.
 */



public class LoginActivity_User_Login extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Intent intent = getIntent();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.login_phone_number);
        button = (Button) findViewById(R.id.login_button);
        register = (TextView) findViewById(R.id.login_register);

        button.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String phoneNumber = autoCompleteTextView.getText().toString();
                Intent intent1 = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("phoneNumber", phoneNumber);
                bundle.putSerializable("loginType", "LOGIN");
                intent1.putExtras(bundle);
                intent1.setClass(LoginActivity_User_Login.this, LoginActivity_User_Verify.class);
                startActivity(intent1);
                break;
            case  R.id.login_register:
                Intent intent2 = new Intent();
                intent2.setClass(LoginActivity_User_Login.this, LoginActivity_User_Register.class);
                startActivity(intent2);
                break;
        }
    }
}
