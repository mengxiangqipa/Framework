package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.demo.entity.Entity;
import com.framework.config.Config;
import com.framework.util.ForbidAndroidPhideAPIdialog;
import com.framework.util.ScreenUtil;
import com.framework.util.ViewServer;
import com.library.swipefinish.SwipeFinishHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import custom.org.greenrobot.eventbus.EventBus;

/**
 * 滑动结束 基类2
 *
 * @author YobertJomi
 * className BaseAbsSwipeFinishActivity
 * created at  2020/10/23  17:43
 */
public abstract class BaseAbsSwipeFinishActivity extends AppCompatActivity {

    public abstract void _onCreate();

    public abstract SwipeFinishHelper.Delegate initDelegate();

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeFinishHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeFinishFinish();
        super.onCreate(savedInstanceState);
        _onCreate();
        ForbidAndroidPhideAPIdialog.getInstance().closeAndroidPdialog();
        ScreenUtil.getInstance().setSystemUiColorDark(this, true);
        int primary = getResources().getColor(R.color.black);
        int secondary = getResources().getColor(R.color.black);
        EventBus.getDefault().register(this);
        if (Config.ENABLE_LOG) {
            ViewServer.get(this).addWindow(this);
        }
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

    public void startActivity(@Nullable Class<?> cls) {
        startActivity(cls, new Bundle());
    }

    /**
     * 返回主页，这个会clearHistoryTasks，并新建HomepageActivity
     */
    public void goBackHomepage(boolean shouldRefresh) {
        Intent intent =
                new Intent(BaseAbsSwipeFinishActivity.this, HomePageActivity.class).setFlags(Intent
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
        Intent intent = new Intent(BaseAbsSwipeFinishActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivity(@Nullable Class<?> cls, Entity entity) {
        Intent intent = new Intent(BaseAbsSwipeFinishActivity.this, cls);
        if (null != entity) {
            intent.putExtra(ConstantsME.entity, entity);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivityForResult(@Nullable Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseAbsSwipeFinishActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
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

    protected SwipeFinishHelper mSwipeFinishHelper;

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeFinishFinish() {
        mSwipeFinishHelper = new SwipeFinishHelper(this, null == initDelegate() ? delegate :
                initDelegate());

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeFinishHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeFinishHelper.setSwipeFinishEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeFinishHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeFinishHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeFinishHelper.setShadowResId(R.drawable.sfl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeFinishHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeFinishHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeFinishHelper.setSwipeFinishThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeFinishHelper.setIsNavigationBarOverlap(false);
    }

    private SwipeFinishHelper.Delegate delegate = new SwipeFinishHelper.Delegate() {
        /**
         * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
         *
         * @return
         */
        @Override
        public boolean isSupportSwipeFinish() {
            return true;
        }

        /**
         * 正在滑动返回
         *
         * @param slideOffset 从 0 到 1
         */
        @Override
        public void onSwipeFinishLayoutSlide(float slideOffset) {
        }

        /**
         * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
         */
        @Override
        public void onSwipeFinishLayoutCancel() {
        }

        /**
         * 滑动返回执行完毕，销毁当前 Activity
         */
        @Override
        public void onSwipeFinishLayoutExecuted() {
            mSwipeFinishHelper.swipeFinishward();
        }
    };

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeFinishHelper.isSliding()) {
            return;
        }
        mSwipeFinishHelper.backward();
    }
}
