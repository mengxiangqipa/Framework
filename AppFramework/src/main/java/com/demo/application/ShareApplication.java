package com.demo.application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.demo.configs.InterfaceConfig;
import com.framework.application.ProxyApplication;
import com.framework.util.StrictModeUtil;
import com.framework.util.Y;
import com.library.swipefinish.SwipeFinishHelper;
import com.meituan.android.walle.WalleChannelReader;

//public class ShareApplication extends FrameApplication{
public class ShareApplication extends ShareApplicationTemp {//使用Tinker时改变Application

    public static volatile ShareApplication share;
    public static Handler hander_other;

    /**
     * 获取ShareApplication的单例
     *
     * @return ApplicationController singleton instance
     */
    public static synchronized ShareApplication getInstance() {
        return share;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initFrameworkApplication();//使用Tinker时改变Application
        if (!InterfaceConfig.allowLog) {
            //StrictMode模式
            StrictModeUtil.getInstance().setThreadPolicy();
            StrictModeUtil.getInstance().setVmPolicy();
        }
        hander_other = new Handler();
        //		Utils.h = new Handler();
        share = this;
        if (!InterfaceConfig.allowLog) {
            // TODO: 2017/6/8
//            initDefaultUncaughtExceptionHandler();
        }
        Y.y("ShareApplication:" + "initMyLocation1");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager
                .PERMISSION_GRANTED) {
            //			initLocation_gaode();
            Y.y("initLocation_gaode");
        }

        //设置MTA渠道
        initMTAchannel();

        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeFinishHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        SwipeFinishHelper.init(this, null);
    }

    /**
     * 设置MTA渠道
     */
    private void initMTAchannel() {
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        Log.e("yy", "channel:" + channel);
//        if (!TextUtils.isEmpty(channel)) {
//            StatConfig.setInstallChannel(getApplicationContext(), channel);
//        } else {
//            StatConfig.setInstallChannel(getApplicationContext(), "天际官方");
//        }
    }

    private void initFrameworkApplication() {
        ProxyApplication.onCreate(this);
    }

    /**
     * 初始化全局未被捕获的异常
     */
    private void initDefaultUncaughtExceptionHandler() {
        // 设置该MyUncaughtExceptionHandler为程序的默认处理器
        MyUncaughtExceptionHandler myUncaughtExceptionHandler =
                new MyUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);
    }
}
