package com.duoshoulist.duoshoulist.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.activity.PostActivityOne;
import com.duoshoulist.duoshoulist.adapter.MainFragmentAdapter;
import com.duoshoulist.duoshoulist.bmob.MyUser;

/**
 * Created by Dan on 2016-04-10.
 */
public class MainFragment extends Fragment {

    final String TAG = "MainFragment";
    public static RelativeLayout relativeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        // View Pager
        ViewPager viewPager = (ViewPager) relativeLayout.findViewById(R.id.fragment_main_viewPager);
        // Tab Layout
        TabLayout tabLayout = (TabLayout) relativeLayout.findViewById(R.id.fragment_main_tabLayout);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.window_background));
        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.GRAY));

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            Log.i(TAG, "viewPager != null的IF方法执行了");
        }

        return relativeLayout;
    }

    private void setupViewPager(ViewPager viewPager) {
        MainFragmentAdapter adapter = new MainFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(new BoughtListFragment(), "已购清单");
        adapter.addFragment(new WishListFragment(), "心愿清单");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }


}
