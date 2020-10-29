package com.demo.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.demo.entity.Entity;
import com.demo.enums.StateEnum;
import com.framework.util.ForbidAndroidPhideAPIdialog;
import com.framework.util.KeyBoardUtil;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import custom.org.greenrobot.eventbus.EventBus;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

/**
 * activity基类
 *
 * @author YobertJomi
 * className BaseActivity
 * created at  2020/10/29  11:57
 */
public class BaseActivity extends AppCompatActivity {

    private WeakReference<BaseActivity> weakReference;

    private StateEnum netStateEnum = StateEnum.NONE;

    /**
     * @param info String
     */
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "eventBusT")
    public void receivedEventBusMes(String info) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ForbidAndroidPhideAPIdialog.getInstance().closeAndroidPdialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        weakReference = new WeakReference<>(this);
        //绑定布局
        initContentView(savedInstanceState);
        initLocalData(savedInstanceState);
        initUi(savedInstanceState);
        initLocalDataAfterUi(savedInstanceState);
        //绑定监听
        initListener();
        requestNetData();
    }

    /**
     * 初始化本地数据，intent传递，数据库等
     *
     * @param savedInstanceState @Nullable Bundle
     */
    public void initLocalData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化本地数据，intent传递，数据库等(初始化UI后)
     *
     * @param savedInstanceState @Nullable Bundle
     */
    public void initLocalDataAfterUi(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 绑定布局
     *
     * @param savedInstanceState Bundle
     */
    public void initContentView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 初始化Ui
     *
     * @param savedInstanceState @Nullable Bundle
     */
    public void initUi(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 绑定监听
     */
    public void initListener() {
    }

    /**
     * 请求网络数据
     */
    public void requestNetData() {
        netStateEnum = StateEnum.DOING;
    }

    public void setNetStateEnum(StateEnum netStateEnum) {
        this.netStateEnum = netStateEnum;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (netStateEnum == StateEnum.FAIL) {
            requestNetData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    public WeakReference<BaseActivity> getWeakReference() {
        return weakReference;
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
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivity(@Nullable Class<?> cls, Entity entity) {
        Intent intent = new Intent(BaseActivity.this, cls);
        if (null != entity) {
            intent.putExtra(ConstantsME.entity, entity);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void startActivityForResult(@Nullable Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(BaseActivity.this, cls);
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
        KeyBoardUtil.getInstance().isCloseSoftInputMethod(this, null, true);
        finish();
        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);
    }
}
