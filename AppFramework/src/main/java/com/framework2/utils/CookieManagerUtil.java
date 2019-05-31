package com.framework2.utils;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.demo.configs.ConstantsME;
import com.framework.security.AesUtils;
import com.framework.security.Base64Coder;
import com.framework.security.RSAmethodInRaw;
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
        String cookies = decodeCookie(context, PreferencesHelper.getInstance().getStringData(ConstantsME.cookies));
        Y.y("synCookies:" + cookies);
        if (!TextUtils.isEmpty(cookies)) {
            String[] cookie = cookies.split(";");
            CookieSyncManager.createInstance(context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();//移除
            for (String s : cookie) {
                cookieManager.setCookie(url, s);//cookies是在HttpClient中获得的cookie
            }
            CookieSyncManager.getInstance().sync();
        }
    }

    public void saveCookie(Context context, String cookies) {
        if (!TextUtils.isEmpty(cookies)) {
            if (TextUtils.isEmpty(PreferencesHelper.getInstance().getStringData(ConstantsME.aesKey))) {
                PreferencesHelper.getInstance().putInfo(ConstantsME.aesKey, Base64Coder.encodeString(AesUtils
                        .getInstance().generateKey()));
            }
            if (cookies.length() > 128) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i * 128 < cookies.length(); i++) {
                    String cookiesWithRSA = RSAmethodInRaw.rsaEncrypt(context, cookies.substring(i * 128, Math.min((i
                            + 1) * 128, cookies.length())));
                    if (i > 0)
                        sb.append(",,,,");
                    sb.append(Base64Coder.encodeString(cookiesWithRSA));
                }
                PreferencesHelper.getInstance().putInfo(ConstantsME.cookies, AesUtils.getInstance().encrypt
                        (Base64Coder.decodeString(PreferencesHelper.getInstance().getStringData(ConstantsME.aesKey)),
                                sb.toString()));
            } else {
                String cookiesWithRSA = RSAmethodInRaw.rsaEncrypt(context, cookies);
                PreferencesHelper.getInstance().putInfo(ConstantsME.cookies, AesUtils.getInstance().encrypt
                        (Base64Coder.decodeString(PreferencesHelper.getInstance().getStringData(ConstantsME.aesKey)),
                                Base64Coder.encodeString(cookiesWithRSA)));
            }
        }
//        String aa = Base64Coder.encodeString(Base64Coder.encodeString(cookies));
//        Y.y("encodeString:" + aa);
//        String b = Base64Coder.decodeString(Base64Coder.decodeString(aa));
//        Y.y("decodeString:" + b);
//        Y.y("cookies:" + cookies.length());
//        String dd = RSAmethod.rsaEncrypt(context, cookies);
//        Y.y("RSAmethod.rsaEncrypt:" + dd);
//        String ee = RSAmethod.rsaDecrypt(context, dd);
//        Y.y("RSAmethod.rsaDecrypt:" + ee);
    }

    public String decodeCookie(Context context, String cookies) {
        if (!TextUtils.isEmpty(cookies)) {
            String tm = AesUtils.getInstance().decrypt(Base64Coder.decodeString(PreferencesHelper.getInstance()
                    .getStringData(ConstantsME.aesKey)), cookies);
            if (!TextUtils.isEmpty(tm)) {
                StringBuilder sb = new StringBuilder();
                for (String s : tm.split(",,,,")) {
                    String cookiesBase64decode = Base64Coder.decodeString(s);
                    if (!TextUtils.isEmpty(cookiesBase64decode)) {
                        sb.append(RSAmethodInRaw.rsaDecrypt(context, cookiesBase64decode));
                    }
                }
                return sb.toString();
            }
        }
        return null;
    }
}
