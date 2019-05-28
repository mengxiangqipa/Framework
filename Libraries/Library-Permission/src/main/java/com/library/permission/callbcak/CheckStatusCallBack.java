package com.library.permission.callbcak;

import android.app.Activity;

public interface CheckStatusCallBack {

    /**
     * 状态OK
     *
     * @param activity 状态可用的回调
     */
    void onStatusOk(Activity activity);

    /**
     * 状态错误
     */
    void onStatusError();
}
