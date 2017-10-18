package com.framework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yobert Jomi
 *         className ActivityTaskUtil
 *         created at  2016/10/15  17:22
 *         存储管理activity实例
 */
public class ActivityTaskUtil {
    // 存储已打开的Activity集合
    private List<Activity> list = new ArrayList<>();
    private static volatile ActivityTaskUtil singleton;

    private ActivityTaskUtil() {
    }

    public static ActivityTaskUtil getInstance() {
        if (singleton == null) {
            synchronized (ActivityTaskUtil.class) {
                if (singleton == null) {
                    singleton = new ActivityTaskUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * @param context 上下文
     * @param TAG     activity的全名 如：com.activty.loging
     * @return activity是否处在顶层
     */
    @SuppressWarnings("deprecation")
    public boolean isTopActivity(Context context, String TAG) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        try {
            return cn.getClassName().contains(TAG);
        } catch (Exception e) {
            return false;
        }
    }

    public int getActivitySize() {
        return list==null?0:list.size();
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity activity) {
        list.add(activity);
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity activity) {
        list.remove(activity);
    }
    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public List<Activity> getActivityList() {
       return list;
    }
    public void exit() {
        for (Activity activity : list) {
            if (!activity.isFinishing())
                activity.finish();
            Y.y("程序已完全退出");
        }
        list.clear();
        System.gc();
        // 杀死该应用进程
        //android.os.Process.killProcess(android.os.Process.myPid());
        // 释放下面的注释，会把后台进程杀掉
        // System.exit(0);
    }

    /**
     * <pre>
     *
     * @param context context
     * @return 最后不要忘记在AndroidManifest.xml中增加权限：
     * <uses-permission android:name ="android.permission.GET_TASKS"/>
     * </pre>
     */
    public String getTopActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)
            return runningTaskInfos.get(0).topActivity.getClassName();
        else
            return "";
    }
}
