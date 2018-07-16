package com.framework2.eventbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.demo.demo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        EventBus.getDefault().register(this);
        User user = new User();
        user.age = "23";
        EventBus.getDefault().post(user, "我的");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerEventBus(String msg) {
        Log.e("yy", msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerEventBus1(String msg) {
        Log.e("yy", msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handlerEventBus2(User user) {
        Log.e("yy", "user空");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, tag = "我的")
    public void handlerEventBus3(User user) {
        Log.e("yy", "user我的");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
