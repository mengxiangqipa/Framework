package com.framework2.util;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 专门适配小米的状态栏
 *
 * @author YobertJomi
 * className UIConstantUtil
 * created at  2018/11/29  18:08
 */
public class MiuiStatusBarUtil {
    private static volatile MiuiStatusBarUtil singleton;

    private MiuiStatusBarUtil() {
    }

    public static MiuiStatusBarUtil getInstance() {
        if (singleton == null) {
            synchronized (MiuiStatusBarUtil.class) {
                if (singleton == null) {
                    singleton = new MiuiStatusBarUtil();
                }
            }
        }
        return singleton;
    }

    public void setStatusBarDarkMode(@NonNull Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            Window window = activity.getWindow();
            if (darkmode) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                int flag = activity.getWindow().getDecorView().getSystemUiVisibility()
                        & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                window.getDecorView().setSystemUiVisibility(flag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
