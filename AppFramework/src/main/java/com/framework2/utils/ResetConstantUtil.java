package com.framework2.utils;

import com.demo.configs.ConstantsME;
import com.framework.utils.PreferencesHelper;

/**
 * 重置sharePreference的一些常量
 *
 * @author Yangjie
 * className ResetConstantUtil
 * created at  2017/4/21  16:10
 */
public class ResetConstantUtil {
    private static volatile ResetConstantUtil singleton;

    private ResetConstantUtil() {
    }

    public static ResetConstantUtil getInstance() {
        if (singleton == null) {
            synchronized (ResetConstantUtil.class) {
                if (singleton == null) {
                    singleton = new ResetConstantUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 清除出库订单的用户信息
     */
    public void resetOrderClientInfo() {
        PreferencesHelper.getInstance().putInfo(ConstantsME.clientNameCache, "");
        PreferencesHelper.getInstance().putInfo(ConstantsME.clientContactTypeCache, 0);
        PreferencesHelper.getInstance().putInfo(ConstantsME.clientContactCache, "");
        PreferencesHelper.getInstance().putInfo(ConstantsME.clientRemarkCache, "");
    }

    /**
     * 清除登录信息
     */
    public void clearUserLoginInfo() {
        PreferencesHelper.getInstance().putInfo(ConstantsME.token, "");
        PreferencesHelper.getInstance().putInfo(ConstantsME.qqBind, false);
        PreferencesHelper.getInstance().putInfo(ConstantsME.wChatBind, false);
        PreferencesHelper.getInstance().putInfo(ConstantsME.LOGINED, false);
    }
}
