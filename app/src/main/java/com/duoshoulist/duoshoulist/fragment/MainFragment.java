/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duoshoulist.duoshoulist.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.FeedAdapter;
import com.duoshoulist.duoshoulist.bmob.FeedItem;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainFragment extends Fragment {

    final String TAG = "MainFragment";

    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始

    private static List<FeedItem> feedItemList = new ArrayList<FeedItem>();
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    FeedAdapter adapter;
    boolean isLoadingMore;

    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.swipe_refresh_layout_main, container, false);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        queryData(0, STATE_REFRESH);
                        Log.i(TAG, "onRefresh方法执行了，开始获取数据");
                    }
                });
                swipeRefreshLayout.setRefreshing(true);
            }
        });

        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (RecyclerView) getView().findViewById(R.id.main_recycler_view);
        setupRecyclerView(recyclerView);
        checkLaunchTime();
    }

    private void checkLaunchTime() {
        // 启动次数
        SharedPreferences preferences = getContext().getSharedPreferences("launchCount", getContext().MODE_PRIVATE);
        int count = preferences.getInt("launchCount", 0);

        //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
        if (count == 0) {
//            Intent intent = new Intent();
//            intent.setClass(getApplicationContext(), LaunchGuideViewActivity.class);
//            startActivity(intent);
            swipeRefreshLayout.setRefreshing(true);
            Log.i(TAG, "第一次运行程序");
        }
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("launchCount", ++count);
        //提交修改
        editor.commit();
        Log.i(TAG, "第N次运行程序, N = " + count);
        queryData(0, STATE_REFRESH);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        queryData(0, STATE_REFRESH);
        adapter = new FeedAdapter(getActivity(), feedItemList);
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    Log.i(TAG, "loadPage方法执行了");
                    if (!isLoadingMore) {
                        loadPage();
                    }
                }
            }
        });
    }

    private void loadPage() {
        swipeRefreshLayout.setRefreshing(true);
        queryData(curPage, STATE_MORE);
    }

    private void queryData(final int page, final int actionType) {
        swipeRefreshLayout.setRefreshing(true);
        isLoadingMore = true;
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        BmobQuery<FeedItem> query = new BmobQuery<FeedItem>();
        query.setLimit(limit);            // 设置每页多少条数据
        query.setSkip(page * limit);        // 从第几条数据开始
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<FeedItem>() {

            @Override
            public void onSuccess(List<FeedItem> feedItems) {

                Log.i(TAG, "feedItems的大小: " + feedItems.size());
                if (feedItems.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                        curPage = 0;
                        feedItemList.clear();
                    }

                    for (FeedItem feedItem : feedItems) {
                        feedItemList.add(feedItem);
                    }
                    // 将本次查询的数据添加到bankCards中

                    adapter.notifyDataSetChanged();
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    Snackbar.make(getView(), "第" + (page + 1) + "页数据加载完成", Snackbar.LENGTH_LONG).show();
                } else if (actionType == STATE_MORE) {
                    Log.i(TAG, "没有更多数据了");
                    Snackbar.make(swipeRefreshLayout, "没有更多数据了", Snackbar.LENGTH_LONG).show();
                } else if (actionType == STATE_REFRESH) {
                    Log.i(TAG, "没有新数据");
                    Snackbar.make(swipeRefreshLayout, "没有新数据", Snackbar.LENGTH_LONG).show();
                }
                swipeRefreshLayout.setRefreshing(false);
                isLoadingMore = false;
            }

            @Override
            public void onError(int arg0, String arg1) {
                Log.i(TAG, "查询失败:" + arg1);
                swipeRefreshLayout.setRefreshing(false);
                isLoadingMore = false;
            }
        });
    }


}
