package com.demo.application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;

import com.demo.configs.InterfaceConfig;
import com.framework.application.FrameApplication;
import com.framework.utils.StrictModeUtil;
import com.framework.utils.Y;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //			initLocation_gaode();
            Y.y("initLocation_gaode");
        }

    }

    private void initFrameworkApplication() {
        FrameApplication frameApplication = new FrameApplication();
        frameApplication.onCreate(this);
    }

    /**
     * 初始化全局未被捕获的异常
     */
    private void initDefaultUncaughtExceptionHandler() {
        // 设置该MyUncaughtExceptionHandler为程序的默认处理器
        MyUncaughtExceptionHandler myUncaughtExceptionHandler = new MyUncaughtExceptionHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(myUncaughtExceptionHandler);
    }
}
