package com.framework2.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class CustomLoadingDialogUtils {

    private static volatile CustomLoadingDialogUtils singleton;
    private CustomLoadingDialog customProgress;

    private CustomLoadingDialogUtils() {
    }

    public static CustomLoadingDialogUtils getInstance() {
        if (singleton == null) {
            synchronized (CustomLoadingDialogUtils.class) {
                if (singleton == null) {
                    singleton = new CustomLoadingDialogUtils();
                }
            }
        }
        return singleton;
    }

    public void dismissDialog() {
        try {
            if (customProgress != null) {
                customProgress.dismiss();
                customProgress = null;
            }
        } catch (Exception e) {

        }
    }

    public Dialog showDialog(Context ac, String message) {
        try {
            if (customProgress != null) {
                try {
                    customProgress.setContent(message);
                    customProgress.showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                customProgress.setMessage(message);
                return customProgress;
            }
            customProgress = new CustomLoadingDialog(ac, message);
            // customProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            customProgress.setMessage(message);
            customProgress.setCancelable(false);
//            customProgress.setIndeterminate(false);
            customProgress.setCancelable(false);

            try {

                if (null != customProgress) {
                    customProgress.showDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            customProgress.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode,
                                     KeyEvent event) {
                    if (null != customProgress) {
                        customProgress.dismiss();
                    }
                    customProgress = null;
                    return false;
                }
            });
            return customProgress;
        } catch (Exception e) {
            return null;
        }
    }
}
