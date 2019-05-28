package com.library.permission.adapter;

import com.library.permission.bean.Special;
import com.library.permission.callbcak.SpecialPermissionListener;

public abstract class SimpleSpecialPermissionAdapter implements SpecialPermissionListener {

    @Override
    public void onDenied(Special permission) {

    }

    @Override
    public void onGranted(Special permission) {

    }
}
