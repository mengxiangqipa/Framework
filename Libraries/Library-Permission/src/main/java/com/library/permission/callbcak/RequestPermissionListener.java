package com.library.permission.callbcak;

import com.library.permission.bean.Permission;

public interface RequestPermissionListener {

    /**
     * 得到权限检查结果
     *
     * @param permissions 封装权限的数组
     */
    void onPermissionResult(Permission[] permissions);
}
