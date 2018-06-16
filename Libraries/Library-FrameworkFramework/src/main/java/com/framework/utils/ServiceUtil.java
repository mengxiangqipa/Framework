package com.framework.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * 距离
 *
 * @author YobertJomi
 * className DistanceUtil
 * created at  2017/8/4  11:29
 */
public class ServiceUtil {
    private static volatile ServiceUtil singleton;

    private ServiceUtil() {
    }

    public static ServiceUtil getInstance() {
        if (singleton == null) {
            synchronized (ServiceUtil.class) {
                if (singleton == null) {
                    singleton = new ServiceUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 获取运行的service
     *
     * @param context
     * @return
     */
    public List<ActivityManager.RunningServiceInfo> getRunningServices(Context context, int maxNum) {

        ActivityManager activityManager;
        try {
            activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            //获取服务
            List<ActivityManager.RunningServiceInfo> serviceTasks =
                    activityManager.getRunningServices(maxNum);
            return serviceTasks;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取运行的service
     */
    public List<ActivityManager.RunningServiceInfo> getRunningServices(Context context) {
        return getRunningServices(context, 30);
    }

    /**
     * 获取运行的service
     *
     * @param context     context
     * @param processName 进程名称
     */
    public boolean isServiceRunning(Context context, String processName) {

        ActivityManager activityManager;
        try {
            activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            //获取服务
            List<ActivityManager.RunningServiceInfo> serviceTasks =
                    activityManager.getRunningServices(30);
            if (null == processName || "".equals(processName)) {
                return false;
            }
            for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceTasks) {
//                Y.y("获取服务是否存在：" + runningServiceInfo.process + "   " + runningServiceInfo.pid + "   " +
// runningServiceInfo.clientPackage);
                if (processName.equals(runningServiceInfo.process)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 获取运行的service的ComponentName
     *
     * @param processName 进程名称
     * @return ComponentName ComponentName
     */
    public ComponentName getRunningServiceComponentName(Context context, String processName) {

        ActivityManager activityManager;
        try {
            activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            //获取服务
            List<ActivityManager.RunningServiceInfo> serviceTasks =
                    activityManager.getRunningServices(30);
            if (null == processName || "".equals(processName)) {
                return null;
            }
            for (ActivityManager.RunningServiceInfo runningServiceInfo : serviceTasks) {
//                Y.y("获取服务是否存在：" + runningServiceInfo.process + "   " + runningServiceInfo.pid + "   " +
// runningServiceInfo.clientPackage);
                if (processName.equals(runningServiceInfo.process)) {
//                    runningServiceInfo.
                    return runningServiceInfo.service;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
