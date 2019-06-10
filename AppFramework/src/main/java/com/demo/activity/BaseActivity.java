package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.demo.entity.Entity;
import com.framework.util.ForbidAndroidPhideAPIdialog;
import com.framework.util.KeyBoardUtil;

import custom.org.greenrobot.eventbus.EventBus;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * activity的base类
 *
 * @author Yangjie
 * className BaseActivity
 * created at  2017/3/15  13:44
 */
public class BaseActivity extends AppCompatActivity {

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "newMessage")
    public void receivedNewMessage(String info) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
//        ActivityTaskUtil.getProxyApplication().addActivity(this);
        ForbidAndroidPhideAPIdialog.getInstance().closeAndroidPdialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        ActivityTaskUtil.getProxyApplication().removeActivity(this);
//        List<Activity> list = ActivityTaskUtil.getProxyApplication().getActivityList();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, null, true);
            beforeFinishActivity();
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startActivity(@Nullable Class<?> cls) {
        startActivity(cls, new Bundle());
    }

    /**
     * 返回主页，这个会clearHistoryTasks，并新建HomepageActivity
     */
    public void goBackHomepage(boolean shouldRefresh) {
        Intent intent = new Intent(BaseActivity.this, HomePageActivity.class).setFlags(Intent
                .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(ConstantsME.shouldRefresh, shouldRefresh);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void goBackHomepage() {
        goBackHomepage(false);
    }

    /**
     * 返回重新登录
     */
    public void reLogin() {
//        ResetConstantUtil.getProxyApplication().clearUserLoginInfo();
//        PreferencesHelper.getProxyApplication().putInfo(ConstantsME.token, "");
//        startActivity(new Intent(BaseActivity.this,
//                LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//        finish();
    }

    public void startActivity(@Nullable Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivity(@Nullable Class<?> cls, Entity entity) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != entity)
            intent.putExtra(ConstantsME.entity, entity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivityForResult(@Nullable Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void beforeFinishActivity() {

    }

    public void finishActivity() {
        KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, null, true);
        finish();
        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);
    }
}
