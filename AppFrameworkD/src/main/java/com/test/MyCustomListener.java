package com.test;

import android.os.Handler;
import android.os.Message;

import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;

public class MyCustomListener implements BaseAbstractPullToRefreshLayout.OnRefreshListener {

    @Override
    public void onRefresh(final BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
        // 下拉刷新操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 千万别忘了告诉控件刷新完毕了哦！
                pullToRefreshLayout.refreshFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onLoadMore(final BaseAbstractPullToRefreshLayout pullToRefreshLayout) {
        // 加载操作
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 千万别忘了告诉控件加载完毕了哦！
                pullToRefreshLayout.loadmoreFinish(BaseAbstractPullToRefreshLayout.SUCCEED);
            }
        }.sendEmptyMessageDelayed(0, 1000);
    }
}
