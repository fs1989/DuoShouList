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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.duoshoulist.duoshoulist.R;
import com.duoshoulist.duoshoulist.adapter.FeedAdapter;
import com.duoshoulist.duoshoulist.bmob.FeedItem;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class ProfileOriginalFragment extends Fragment {

    final String TAG = "MainFragment";
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10;        // 每页的数据是10条
    private int curPage = 0;        // 当前页的编号，从0开始

    List<FeedItem> bankCards = new ArrayList<FeedItem>();
    SwipeRefreshLayout swipeRefreshLayout;
    UltimateRecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    FeedAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.swipe_refresh_layout_main, container, false);
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        return swipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = (UltimateRecyclerView) getView().findViewById(R.id.main_recycler_view);
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(UltimateRecyclerView recyclerView) {
        mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.enableLoadmore();
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        queryData(0, STATE_REFRESH);
        adapter = new FeedAdapter(getActivity(), bankCards);

        recyclerView.setAdapter(adapter);

        // Load More
        recyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, int maxLastVisiblePosition) {
                loadPage();
            }
        });
    }

    private void refreshData() {
        queryData(0, STATE_REFRESH);
    }

    private void loadPage() {
        swipeRefreshLayout.setRefreshing(true);
        queryData(curPage, STATE_MORE);

    }

    private void queryData(final int page, final int actionType) {
        Log.i("bmob", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);

        BmobQuery<FeedItem> query = new BmobQuery<FeedItem>();
        query.setLimit(limit);            // 设置每页多少条数据
        query.setSkip(page * limit);        // 从第几条数据开始
        query.order("-createdAt");
        query.findObjects(getActivity(), new FindListener<FeedItem>() {

            @Override
            public void onSuccess(List<FeedItem> feedItems) {
                // TODO Auto-generated method stub

                if (feedItems.size() > 0) {
                    if (actionType == STATE_REFRESH) {
                        // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                        curPage = 0;
                        bankCards.clear();
                    }

                    // 将本次查询的数据添加到bankCards中
                    for (FeedItem feedItem : feedItems) {
                        bankCards.add(feedItem);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                    curPage++;
                    showToast("第" + (page + 1) + "页数据加载完成");
                } else if (actionType == STATE_MORE) {
                    showToast("没有更多数据了");
                } else if (actionType == STATE_REFRESH) {
                    showToast("没有数据");
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(int arg0, String arg1) {
                // TODO Auto-generated method stub
                Log.i(TAG, "查询失败:" + arg1);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}
