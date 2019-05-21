package com.library.videoview.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author YobertJomi
 * className ScreenUtils
 * created at  2016/10/5  10:49
 */
public class ScreenUtils {
    private static volatile ScreenUtils instance;

    private ScreenUtils() {
    }

    public static ScreenUtils getInstance() {
        if (null == instance) {
            synchronized (ScreenUtils.class) {
                if (null == instance) {
                    instance = new ScreenUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 获取屏幕宽度 单位px
     *
     * @param context 上下文
     * @return 宽度px
     */
    public int getScreenWidthPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度 单位px
     *
     * @param context 上下文
     * @return 高度px
     */
    public int getScreenHeightPx(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return px值
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @return dp值
     */
    public int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 高度px
     */
    public int getStatusBarHeightPx(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return db值
     */
    public int getStatusBarHeightDp(Activity activity) {
        // Rect rect = new Rect();
        // activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        final float scale = activity.getResources().getDisplayMetrics().density;
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return (int) (statusHeight / scale + 0.5f);
    }

    /**
     * 设置activity状态栏悬浮
     *
     * @param activity             activity
     * @param setTranslucentStatus 设置activity状态栏悬浮
     * @return 设置activity状态栏悬浮
     */
    public boolean setTranslucentStatus(Activity activity, boolean setTranslucentStatus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (setTranslucentStatus) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
            return true;
        }
        return false;
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarTintColor(Activity activity) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        // tintManager.setNavigationBarTintEnabled(true);
        //		tintManager.setTintColor(activity.getResources().getColor(
        //				R.color.transparent));// 通知栏所需颜色
        tintManager.setTintColor(Color.parseColor("#00000000"));// 通知栏所需颜色
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarTintColor(Activity activity, int colorResource) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
        tintManager.setTintColor(colorResource);// 通知栏所需颜色
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarTintColor(Activity activity, int colorResource, boolean StatusBarTintEnabled,
                                      boolean NavigationBarTintEnabled) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(StatusBarTintEnabled);
        tintManager.setNavigationBarTintEnabled(NavigationBarTintEnabled);
        // 设置一个颜色给系统栏
        tintManager.setTintColor(colorResource);// 通知栏所需颜色
    }
}
