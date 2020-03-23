package com.library.permission.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.library.permission.SoulPermission;
import com.library.permission.bean.Permission;
import com.library.permission.callbcak.CheckRequestPermissionListener;

public abstract class CheckPermissionAdapter implements CheckRequestPermissionListener {

    @Override
    public void onPermissionDenied(Permission permission) {
        //Permission提供栈顶Activity
        Activity activity = SoulPermission.getInstance().getTopActivity();
        if (null == activity) {
            return;
        }
        String permissionDesc = permission.getPermissionNameDesc();
        new AlertDialog.Builder(activity)
                .setTitle("提示")
                .setMessage(permissionDesc + "异常，请前往设置－>权限管理，打开" + permissionDesc + "。")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //去设置页
                        SoulPermission.getInstance().goPermissionSettings();
                    }
                }).create().show();
    }
}
