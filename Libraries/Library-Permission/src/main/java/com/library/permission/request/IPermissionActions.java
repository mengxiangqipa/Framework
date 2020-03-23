package com.library.permission.request;

import android.annotation.TargetApi;

import com.library.permission.bean.Special;
import com.library.permission.callbcak.RequestPermissionListener;
import com.library.permission.callbcak.SpecialPermissionListener;

import static android.os.Build.VERSION_CODES.M;

public interface IPermissionActions {

    /**
     * 请求权限
     *
     * @param permissions 权限
     * @param listener    回调
     */
    @TargetApi(M)
    void requestPermissions(String[] permissions, RequestPermissionListener listener);

    /**
     * 请求特殊权限
     *
     * @param permission 特殊权限
     * @param listener   回调
     */
    void requestSpecialPermission(Special permission, SpecialPermissionListener listener);
}
