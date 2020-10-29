package com.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import custom.org.greenrobot.eventbus.EventBus;

/**
 * activity的base类
 *
 * @author Yangjie
 * className BaseActivity
 * created at  2017/3/15  13:44
 */
public abstract class BaseAbsSlideFinishActivity extends BaseActivity {

    public abstract int[] initPrimeryColor();

    private SlidrListener slidrListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        if (null != slidrListener) {
                            slidrListener.onSlideStateChanged(state);
                        }
                    }

                    @Override
                    public void onSlideChange(float percent) {
                        if (null != slidrListener) {
                            slidrListener.onSlideChange(percent);
                        }
                    }

                    @Override
                    public void onSlideOpened() {
                        if (null != slidrListener) {
                            slidrListener.onSlideOpened();
                        }
                    }

                    @Override
                    public void onSlideClosed() {
                        if (null != slidrListener) {
                            slidrListener.onSlideClosed();
                        }
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

    public SlidrListener getSlidrListener() {
        return slidrListener;
    }

    public void setSlidrListener(SlidrListener slidrListener) {
        this.slidrListener = slidrListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Override
    public void startActivity(@Nullable Class<?> cls) {
        startActivity(cls, new Bundle());
    }

    /**
     * 返回主页，这个会clearHistoryTasks，并新建HomepageActivity
     */
    @Override
    public void goBackHomepage(boolean shouldRefresh) {
        Intent intent =
                new Intent(BaseAbsSlideFinishActivity.this, HomePageActivity.class).setFlags(Intent
                        .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ConstantsME.CITY, shouldRefresh);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    @Override
    public void goBackHomepage() {
        goBackHomepage(false);
    }

    /**
     * 返回重新登录
     */
    @Override
    public void reLogin() {
//        ResetConstantUtil.getProxyApplication().clearUserLoginInfo();
//        PreferencesHelper.getProxyApplication().putInfo(ConstantsME.token, "");
//        startActivity(new Intent(BaseActivity.this,
//                LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
//                .FLAG_ACTIVITY_NEW_TASK));
//        finish();
    }

    @Override
    public void startActivity(@Nullable Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    @Override
    public void startActivity(@Nullable Class<?> cls, Entity entity) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != entity) {
            intent.putExtra(ConstantsME.entity, entity);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    @Override
    public void startActivityForResult(@Nullable Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseAbsSlideFinishActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    @Override
    public void beforeFinishActivity() {

    }

    @Override
    public void finishActivity() {
//        Utils.isCloseSoftInputMethod(this, null, true);
        finish();
        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);
    }
}
