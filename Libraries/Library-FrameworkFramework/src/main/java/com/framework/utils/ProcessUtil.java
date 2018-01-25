package com.framework.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * 进程util
 *
 * @author YobertJomi
 *         className ProcessUtil
 *         created at  2017/9/11  9:23
 */
public class ProcessUtil {
    private static volatile ProcessUtil singleton;

    private ProcessUtil() {
    }

    public static ProcessUtil getInstance() {
        if (singleton == null) {
            synchronized (ProcessUtil.class) {
                if (singleton == null) {
                    singleton = new ProcessUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 根据context获取进程名称
     *
     * @param context context
     * @return String
     */
    public String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        try {
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            if (mActivityManager == null)
                for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                        .getRunningAppProcesses()) {
                    if (appProcess.pid == pid) {
                        return appProcess.processName;
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
