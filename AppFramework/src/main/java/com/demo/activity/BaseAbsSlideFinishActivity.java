package com.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.demo.entity.Entity;
import com.framework.config.Config;
import com.framework.util.ActivityTaskUtil;
import com.framework.util.ForbidAndroidPhideAPIdialog;
import com.framework.util.ScreenUtil;
import com.framework.util.ViewServer;
import com.framework.util.Y;
import com.library.slidefinish.Slidr;
import com.library.slidefinish.model.SlidrConfig;
import com.library.slidefinish.model.SlidrListener;
import com.library.slidefinish.model.SlidrPosition;

import java.util.List;

import custom.org.greenrobot.eventbus.EventBus;

/**
 * activity的base类
 *
 * @author Yangjie
 * className BaseActivity
 * created at  2017/3/15  13:44
 */
public abstract class BaseAbsSlideFinishActivity extends AppCompatActivity {

    public abstract void _onCreate();

    public abstract void onSlideClose();

    public abstract int[] initPrimeryColor();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _onCreate();
        ForbidAndroidPhideAPIdialog.getInstance().closeAndroidPdialog();
        ScreenUtil.getInstance().setSystemUiColorDark(this, true);
        int primary = getResources().getColor(R.color.black);
        int secondary = getResources().getColor(R.color.black);
        EventBus.getDefault().register(this);
        ActivityTaskUtil.getInstance().addActivity(this);
        if (Config.ENABLE_LOG) {
            ViewServer.get(this).addWindow(this);
        }
        SlidrConfig mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(SlidrPosition.LEFT)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
                .touchSize(ScreenUtil.getInstance().dip2px(this, 32))
                .listener(new SlidrListener() {
                    @Override
                    public void onSlideStateChanged(int state) {

                    }

                    @Override
                    public void onSlideChange(float percent) {

                    }

                    @Override
                    public void onSlideOpened() {

                    }

                    @Override
                    public void onSlideClosed() {
                        onSlideClose();
                    }
                })
                .build();
        int[] color = initPrimeryColor();
        if (color != null && color.length == 2) {
            Slidr.attach(this, color[0], color[1], mConfig);
        } else {
            Slidr.attach(this, -1, -1, mConfig);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ActivityTaskUtil.getInstance().removeActivity(this);
        List<Activity> list = ActivityTaskUtil.getInstance().getActivityList();
        for (int i = 0; i < list.size(); i++) {
            Activity ac = list.get(i);
            Y.y("i==" + i + "--:" + ac.getLocalClassName());
        }
        try {
            ViewServer.get(this).removeWindow(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            Utils.isCloseSoftInputMethod(this, null, true);
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
        Intent intent =
                new Intent(BaseAbsSlideFinishActivity.this, HomePageActivity.class).setFlags(Intent
                .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ConstantsME.CITY, shouldRefresh);
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
//                LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
//                .FLAG_ACTIVITY_NEW_TASK));
//        finish();
    }

    public void startActivity(@Nullable Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivity(@Nullable Class<?> cls, Entity entity) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != entity)
            intent.putExtra(ConstantsME.entity, entity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivityForResult(@Nullable Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void beforeFinishActivity() {

    }

    public void finishActivity() {
//        Utils.isCloseSoftInputMethod(this, null, true);
        finish();
        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);
    }
}
