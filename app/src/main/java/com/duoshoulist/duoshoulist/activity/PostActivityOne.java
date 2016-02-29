package com.duoshoulist.duoshoulist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.duoshoulist.duoshoulist.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dan on 2016-02-29.
 */
public class PostActivityOne extends AppCompatActivity {

    @Bind(R.id.post_one_name) TextView name;
    @Bind(R.id.post_one_brand) TextView brand;
    @Bind(R.id.post_one_price) TextView price;
    @Bind(R.id.post_one_button) Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_one);
        ButterKnife.bind(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivityOne.this, PostActivityTwo.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meun_post_one, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.post_one_next:

                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
