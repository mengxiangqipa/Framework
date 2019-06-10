package com.framework.util;

import android.os.StrictMode;

/**
 * StrictMode使用工具
 *
 * @author YobertJomi
 * className StrictModeUtil
 * created at  2017/5/26  10:46
 */
public class StrictModeUtil {

    private static volatile StrictModeUtil singleton;

    private StrictModeUtil() {
    }

    public static StrictModeUtil getInstance() {
        if (singleton == null) {
            synchronized (StrictModeUtil.class) {
                if (singleton == null) {
                    singleton = new StrictModeUtil();
                }
            }
        }
        return singleton;
    }

    public void setThreadPolicy() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyDialog() //弹出违规提示对话框
                .penaltyLog() //在Logcat 中打印违规异常信息
                .penaltyFlashScreen() //API等级11
                .build());
    }

    public void setVmPolicy() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects() //API等级11
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}