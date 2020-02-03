package com.library.permission.request.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.library.permission.Constants;
import com.library.permission.PermissionTools;
import com.library.permission.bean.Permission;
import com.library.permission.bean.Special;
import com.library.permission.checker.SpecialChecker;
import com.library.permission.debug.PermissionDebug;
import com.library.permission.request.IPermissionActions;
import com.library.permission.callbcak.RequestPermissionListener;
import com.library.permission.callbcak.SpecialPermissionListener;

import static android.os.Build.VERSION_CODES.M;


public class PermissionSupportFragment extends Fragment implements IPermissionActions {

    private static final String TAG = PermissionSupportFragment.class.getSimpleName();

    private RequestPermissionListener runtimeListener;

    private SpecialPermissionListener specialListener;

    private Special specialToRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @TargetApi(M)
    @Override
    public void requestPermissions(String[] permissions, RequestPermissionListener listener) {
        requestPermissions(permissions, Constants.REQUEST_CODE_PERMISSION);
        this.runtimeListener = listener;
    }

    @Override
    public void requestSpecialPermission(Special permission, SpecialPermissionListener listener) {
        this.specialListener = listener;
        this.specialToRequest = permission;
        Intent intent = PermissionTools.getSpecialPermissionIntent(getActivity(), permission);
        if (null == intent) {
            PermissionDebug.w(TAG, "create intent failed");
            return;
        }
        try {
            startActivityForResult(intent, Constants.REQUEST_CODE_PERMISSION_SPECIAL);
        } catch (Exception e) {
            e.printStackTrace();
            PermissionDebug.e(TAG, e.toString());
        }
    }

    @TargetApi(M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permission[] permissionResults = new Permission[permissions.length];
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            for (int i = 0; i < permissions.length; ++i) {
                Permission permission = new Permission(permissions[i], grantResults[i], this.shouldShowRequestPermissionRationale(permissions[i]));
                permissionResults[i] = permission;
            }
        }
        if (null != runtimeListener && PermissionTools.isActivityAvailable(getActivity())) {
            runtimeListener.onPermissionResult(permissionResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Activity activity = getActivity();
        if (specialListener == null || !PermissionTools.isActivityAvailable(activity)) {
            return;
        }
        if (requestCode == Constants.REQUEST_CODE_PERMISSION_SPECIAL) {
            boolean result = new SpecialChecker(activity, specialToRequest).check();
            if (result) {
                specialListener.onGranted(specialToRequest);
            } else {
                specialListener.onDenied(specialToRequest);
            }
        }
    }
}
