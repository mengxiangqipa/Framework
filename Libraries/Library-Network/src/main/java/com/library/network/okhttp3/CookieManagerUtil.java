package com.library.network.okhttp3;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.framework.configs.FrameworkConstant;
import com.framework.security.SecurityManagerUtil;
import com.framework.utils.PreferencesHelper;
import com.framework.utils.Y;

/**
 * @author YobertJomi
 * className CookieManagertUtil
 * created at  2017/6/14  11:02
 */
public class CookieManagerUtil {
    private static volatile CookieManagerUtil singleton;

    private CookieManagerUtil() {
    }

    public static CookieManagerUtil getInstance() {
        if (singleton == null) {
            synchronized (CookieManagerUtil.class) {
                if (singleton == null) {
                    singleton = new CookieManagerUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 同步一下cookie
     */
    public void synCookies(Context context, String url) {
        String cookies = decodeCookie(context, PreferencesHelper.getInstance().getStringData(FrameworkConstant.COOKIE));
        Y.y("synCookies:" + cookies);
        if (!TextUtils.isEmpty(cookies)) {
            String[] cookie = cookies.split(";");
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            for (int i = 0; i < cookie.length; i++) {
                cookieManager.setCookie(url, cookie[i]);//cookies是在HttpClient中获得的cookie
            }
            CookieSyncManager.getInstance().sync();
        }
    }

    public void saveCookie(Context context, String cookies) {
        SecurityManagerUtil.getInstance().put(context, FrameworkConstant.COOKIE, cookies);
    }

    public String decodeCookie(Context context, String cookies) {
        return SecurityManagerUtil.getInstance().get(context, cookies);
    }
}
