package com.framework.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * @author Yobert Jomi
 * className ScreenBrightnessUtil
 * created at  2016/10/2  9:01
 * 屏幕亮度调节工具类
 */
public class ScreenBrightnessUtil {

    private static volatile ScreenBrightnessUtil instance;

    private ScreenBrightnessUtil() {
    }

    public static ScreenBrightnessUtil getInstance() {
        if (null == instance) {
            synchronized (ScreenBrightnessUtil.class) {
                if (null == instance) {
                    instance = new ScreenBrightnessUtil();
                }
            }
        }
        return instance;
    }

    public boolean hasSettingsPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return Settings.System.canWrite(context);
        else
            return true;
    }

    /**
     * 判断是否是自动亮度
     *
     * @param activity activity
     * @return 是否是自动调节模式
     */
    public boolean isAutomaticScreenBrightness(Activity activity) {
        try {
            return (Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * @param activity activity
     */
    public void closeAutomaticScreenBrightness(Activity activity,
                                               boolean closeAutomaticScreenBrightness) {
        if (closeAutomaticScreenBrightness) {
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings
                    .System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        } else {
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings
                    .System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
    }

    public int getCurrentBrightness(Activity activity) {
        //        获取当前亮度,获取失败则返回255
        try {
            return Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置屏幕亮度
     *
     * @param activity   activity
     * @param brightness 亮度值0-255
     */

    public void setScreenBritness(Activity activity, int brightness) {
        //不让屏幕全暗
        if (brightness <= 1) {
            brightness = 1;
        }
        if (brightness > 255) {
            brightness = 255;
        }
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = brightness / 255f;
        activity.getWindow().setAttributes(lp);
        Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                brightness);
    }
}
