package com.duoshoulist.duoshoulist.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.duoshoulist.duoshoulist.R;

import butterknife.Bind;

/**
 * Created by Dan on 2016-02-29.
 */
public class PostActivityLink extends AppCompatActivity {


    @Bind(R.id.post_link_link)
    AppCompatEditText editText;

    @Bind(R.id.post_link_button)
    Button button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_link);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("productLink",0);
                SharedPreferences.Editor se=sp.edit();;
                se.putString("productLink", editText.getText().toString());
                se.commit();

            }
        });
    }
}
