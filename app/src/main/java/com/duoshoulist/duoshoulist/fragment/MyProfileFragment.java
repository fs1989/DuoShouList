package com.duoshoulist.duoshoulist.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duoshoulist.duoshoulist.R;


/**
 * Created by Dan on 2016/1/29.
 */
public class MyProfileFragment extends Fragment {

//    List<FeedItem> profileData = new ArrayList<Profile>();

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.swipe_refresh_layout_profile, container, false);
        return swipeRefreshLayout;
    }
}
