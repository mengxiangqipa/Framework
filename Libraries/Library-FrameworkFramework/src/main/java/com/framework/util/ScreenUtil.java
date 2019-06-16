package com.framework.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author YobertJomi
 * className ScreenUtils
 * created at  2016/10/5  10:49
 */
public class ScreenUtil {
    private static volatile ScreenUtil instance;

    private ScreenUtil() {
    }

    public static ScreenUtil getInstance() {
        if (null == instance) {
            synchronized (ScreenUtil.class) {
                if (null == instance) {
                    instance = new ScreenUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取屏幕宽度 单位px
     *
     * @param context context
     * @return int
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
     * @param context context
     * @return int
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
     * @return int
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
     * @param activity activity
     * @return dp值
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (int) (statusHeight / scale + 0.5f);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidthPx(activity);
        int height = getScreenHeightPx(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidthPx(activity);
        int height = getScreenHeightPx(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 设置activity状态栏悬浮
     *
     * @param activity             activity
     * @param setTranslucentStatus 是否设置透明
     * @return boolean
     */
    public boolean setTranslucentStatus(Activity activity, boolean setTranslucentStatus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams winParams = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (setTranslucentStatus) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            window.setAttributes(winParams);
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
    public void setStatusBarTintColor(Activity activity, int colorResource, boolean StatusBarTintEnabled, boolean
            NavigationBarTintEnabled) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(StatusBarTintEnabled);
        tintManager.setNavigationBarTintEnabled(NavigationBarTintEnabled);
        // 设置一个颜色给系统栏
        tintManager.setTintColor(colorResource);// 通知栏所需颜色
    }

    /**
     * 设置状态栏alpha
     */
    public void setStatusBarTintAlpha(Activity activity, float alpha) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintAlpha(alpha);// 通知栏所需颜色
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarTintDrawable(Activity activity,
                                         int drawableRes) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
        tintManager.setTintDrawable(activity.getResources().getDrawable(drawableRes));// 通知栏所需drawable
    }

    /**
     * <p>
     * 状态栏颜色--深浅设置
     * 如果调用setTranslucentStatus(Activity , boolean )，第二个参数不能为true
     * xml中根路径需要android:fitsSystemWindows="false"或true；
     *
     * @param activity Activity
     * @param dark     状态栏颜色--深浅设置
     * @return 是否设置成功
     * @see #setTranslucentStatus(Activity, boolean)
     * </p>
     */
    public boolean setSystemUiColorDark(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (null == activity)
                return false;
            Window window = activity.getWindow();
            if (null == window)
                return false;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
                if (dark) {
                    //设置状态栏文字颜色及图标为深色
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    //设置状态栏文字颜色及图标为浅色
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                }
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    /**设置状态栏透明，布局会顶上去
     * @param activity  Activity
     * @return boolean
     */
    public boolean setStatusBarColorTRANSPARENT(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (null == activity || null == activity.getWindow())
                return false;
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏虚拟按键
     */
    public void hideNavigationUIAndStatusBar(Activity activity) {
        if (activity == null) {
            return;
        }
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            Window window = activity.getWindow();
            if (null != window) {
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    /**
     * 隐藏虚拟按键(显示时状态栏变色)
     */
    public void hideNavigationBarAndStatusBar(Activity activity) {
        if (activity == null) {
            return;
        }
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;//0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        try {
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } catch (Exception e) {
        }
    }

    /**
     * 是否显示底部栏
     *
     * @param activity Activity
     * @param show     是否显示
     */
    public void setNavigationBarVisible(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (null != window) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions;
            if (show) {
                uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            } else {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 是否显示状态栏
     *
     * @param activity Activity
     * @param show     是否显示 (false会自动隐藏，手动下拉也会隐藏)
     */
    public void setStatusBarVisible(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    /**
     * 是否显示导航栏和状态栏
     *
     * @param activity Activity
     * @param show     是否显示
     */
    private void setSystemUIVisible(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

    /**
     * 是否显示导航栏和状态栏(不显示时，状态栏自动隐藏，底部栏只是占位，会显示阴影)
     *
     * @param activity Activity
     * @param show     是否显示
     */
    private void setSystemUIVisible2(Activity activity, boolean show) {
        if (activity == null) {
            return;
        }

        if (null != activity.getWindow()) {
            if (!show) {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                activity.getWindow().setAttributes(lp);
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            } else {
                WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
                attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                activity.getWindow().setAttributes(attr);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
    }
}
