package com.library.permission.adapter;

import com.library.permission.bean.Permission;
import com.library.permission.callbcak.CheckRequestPermissionListener;


public abstract class SimplePermissionAdapter implements CheckRequestPermissionListener {

    @Override
    public void onPermissionGranted(Permission permission) {

    }

    @Override
    public void onPermissionDenied(Permission permission) {

    }
}
