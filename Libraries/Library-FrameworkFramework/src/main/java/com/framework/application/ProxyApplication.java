package com.framework.application;

import android.app.Application;
import android.os.Handler;
import android.support.annotation.NonNull;

//public class ProxyApplication extends Application {//主module未使用Tinker时继承Application 并修改FrameApplication--onCreate()
public class ProxyApplication {

    public static Application proxyApplication;
    public static Handler proxyHandler;

    /**
     * 获取FrameApplication的单例
     *
     * @return ApplicationController singleton instance
     */
    public static Application getProxyApplication() {
        return proxyApplication;
    }

//    @CallSuper
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        proxyApplication = this;
//        proxyHandler = new Handler();
//    }

    public static void onCreate(@NonNull Application application) {
        proxyApplication = application;
        proxyHandler = new Handler();
    }
}
