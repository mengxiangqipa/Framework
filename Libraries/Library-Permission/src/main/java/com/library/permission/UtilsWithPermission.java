package com.library.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import com.library.permission.adapter.CheckPermissionAdapter;
import com.library.permission.adapter.CheckPermissionWithRationaleAdapter;
import com.library.permission.bean.Permission;


public class UtilsWithPermission {

    /**
     * 拨打指定电话
     */
    public static void makeCall(final Context context, final String phoneNumber) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.CALL_PHONE,
                new CheckPermissionWithRationaleAdapter("如果你拒绝了权限，你将无法拨打电话，请点击授予权限",
                        new Runnable() {
                            @Override
                            public void run() {
                                //retry
                                makeCall(context, phoneNumber);
                            }
                        }) {
                    @Override
                    public void onPermissionGranted(Permission permission) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + phoneNumber);
                        intent.setData(data);
                        if (!(context instanceof Activity)) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                    }
                });
    }

    /**
     * 选择联系人
     */
    public static void chooseContact(final Activity activity, final int requestCode) {
        SoulPermission.getInstance().checkAndRequestPermission(Manifest.permission.READ_CONTACTS,
                new CheckPermissionAdapter() {
                    @Override
                    public void onPermissionGranted(Permission permission) {
                        activity.startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), requestCode);
                    }
                });
    }
}
