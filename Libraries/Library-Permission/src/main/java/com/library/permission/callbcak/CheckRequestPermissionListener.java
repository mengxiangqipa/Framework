package com.library.permission.callbcak;


import com.library.permission.bean.Permission;


public interface CheckRequestPermissionListener {

    /**
     * 权限ok，可做后续的事情
     *
     * @param permission 权限实体类
     */
    void onPermissionGranted(Permission permission);

    /**
     * 权限不ok，被拒绝或者未授予
     *
     * @param permission 权限实体类
     */
    void onPermissionDenied(Permission permission);
}
