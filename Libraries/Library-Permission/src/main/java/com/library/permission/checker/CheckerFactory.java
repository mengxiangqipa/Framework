package com.library.permission.checker;

import android.content.Context;

import com.library.permission.PermissionTools;
import com.library.permission.bean.Special;

public class CheckerFactory {

    public static PermissionChecker create(Context context, Special permission) {
        return new SpecialChecker(context, permission);
    }

    public static PermissionChecker create(Context context, String permission) {
        if (PermissionTools.isOldPermissionSystem(context)) {
            return new AppOpsChecker(context, permission);
        } else {
            return new RunTimePermissionChecker(context, permission);
        }
    }
}
