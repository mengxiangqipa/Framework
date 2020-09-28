package com.library.permission.request.fragment;

import androidx.annotation.NonNull;
import com.library.permission.bean.Special;
import com.library.permission.callbcak.RequestPermissionListener;
import com.library.permission.debug.PermissionDebug;
import com.library.permission.request.IPermissionActions;
import com.library.permission.callbcak.SpecialPermissionListener;



public class FragmentProxy implements IPermissionActions {

    private static final String TAG = FragmentProxy.class.getSimpleName();

    private IPermissionActions fragmentImp;

    public FragmentProxy(IPermissionActions fragmentImp) {
        this.fragmentImp = fragmentImp;
    }

    @Override
    public void requestPermissions(@NonNull String[] permissions, RequestPermissionListener listener) {
        this.fragmentImp.requestPermissions(permissions, listener);
        PermissionDebug.d(TAG, fragmentImp.getClass().getSimpleName() + " request:" + hashCode());
    }

    @Override
    public void requestSpecialPermission(Special permission, SpecialPermissionListener listener) {
        this.fragmentImp.requestSpecialPermission(permission, listener);
        PermissionDebug.d(TAG, fragmentImp.getClass().getSimpleName() + " requestSpecial:" + hashCode());
    }

}
