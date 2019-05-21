package com.framework.utils;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.framework.application.FrameApplication;

/**
 * 显示toast的util
 *
 * @author Yobert Jomi
 * className ToastUtil
 * created at  2016/10/17  14:58
 */
public class ToastUtil {

    private static volatile ToastUtil singleton;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        if (singleton == null) {
            synchronized (ToastUtil.class) {
                if (singleton == null) {
                    singleton = new ToastUtil();
                }
            }
        }
        return singleton;
    }

    public void showToast(final String message) {
        showToast(message, false);
    }

    public void showToast(@NonNull final String message, final boolean toastLong) {
        if (!TextUtils.isEmpty(message)) {
            FrameApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Toast.makeText(FrameApplication.frameApplication, message, toastLong ? Toast.LENGTH_LONG :
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void showToast(@StringRes final int resId) {
        showToast(resId, false);
    }

    public void showToast(@StringRes final int resId, final boolean toastLong) {
        FrameApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(FrameApplication.frameApplication,
                            FrameApplication.frameApplication.getResources().getString(resId),
                            toastLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}