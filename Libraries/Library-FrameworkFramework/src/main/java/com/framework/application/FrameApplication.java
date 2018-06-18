package com.framework.application;

import android.app.Application;
import android.os.Handler;
import android.support.annotation.NonNull;

//public class FrameApplication extends Application {//主module未使用Tinker时继承Application 并修改FrameApplication--onCreate()
public class FrameApplication {

    public static Application frameApplication;
    public static Handler mHandler;

    /**
     * 获取FrameApplication的单例
     *
     * @return ApplicationController singleton instance
     */
    public static Application getInstance() {
        return frameApplication;
    }

//    @CallSuper
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        frameApplication = this;
//        mHandler = new Handler();
//    }

    public static void onCreate(@NonNull Application application) {
        frameApplication = application;
        mHandler = new Handler();
    }
}
