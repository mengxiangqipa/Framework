package com.demo.util;

import android.content.Context;

public class ReloginUtil {
    private static volatile ReloginUtil singleton;

    private ReloginUtil() {
    }

    public static ReloginUtil getInstance() {
        if (singleton == null) {
            synchronized (ReloginUtil.class) {
                if (singleton == null) {
                    singleton = new ReloginUtil();
                }
            }
        }
        return singleton;
    }

    public void gotoLogin(Context context) {
//        PreferencesHelper.getProxyApplication().putInfo(ConstantsME.cookies, "");
//        Intent intent;
//        if (DebugConfig.TBSx5) {
//            intent = new Intent(context, LoginTBSWebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
// Intent.FLAG_ACTIVITY_NEW_TASK);
//        } else {
//            intent = new Intent(context, LoginWebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
// Intent.FLAG_ACTIVITY_NEW_TASK);
//        }
//        intent.putExtra(ConstantsME.url, RealInterfaceConfig.getRealBaseServerUrl()
////                + InterfaceConfig.webLogin
//        );
//        context.startActivity(intent);
//        ((BaseActivity) context).overridePendingTransition(R.anim.slide_right_in,
//                R.anim.slide_left_out);
    }
}
