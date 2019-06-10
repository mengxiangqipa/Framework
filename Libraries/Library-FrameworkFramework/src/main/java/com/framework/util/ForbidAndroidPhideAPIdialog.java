package com.framework.util;

import android.os.Build;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ForbidAndroidPhideAPIdialog {

    private static volatile ForbidAndroidPhideAPIdialog singleton;

    private ForbidAndroidPhideAPIdialog() {
    }

    public static ForbidAndroidPhideAPIdialog getInstance() {
        if (singleton == null) {
            synchronized (ForbidAndroidPhideAPIdialog.class) {
                if (singleton == null) {
                    singleton = new ForbidAndroidPhideAPIdialog();
                }
            }
        }
        return singleton;
    }

    public void closeAndroidPdialog() {
        if (Build.VERSION.SDK_INT >= 28) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
