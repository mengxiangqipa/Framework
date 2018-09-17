package com.framework.security;

import android.content.Context;
import android.text.TextUtils;

import com.framework.config.FrameworkConstant;
import com.framework.utils.multyprocessprovider.provider.PreferencesUtil;

/**
 * @author YobertJomi
 * className SecurityManagerUtil
 * created at  2017/10/17  10:38
 */
public class SecurityManagerUtil {
    private static volatile SecurityManagerUtil singleton;

    private SecurityManagerUtil() {
    }

    public static SecurityManagerUtil getInstance() {
        if (singleton == null) {
            synchronized (SecurityManagerUtil.class) {
                if (singleton == null) {
                    singleton = new SecurityManagerUtil();
                }
            }
        }
        return singleton;
    }

    public final String get(Context context, String keyInSharedPreferences) {
        if (null == context || TextUtils.isEmpty(keyInSharedPreferences))
            return null;
        return decode(context, PreferencesUtil.getInstance().getString(keyInSharedPreferences));
    }

    public final void put(Context context, String keyInSharedPreferences, String value) {
        if (null == context || TextUtils.isEmpty(keyInSharedPreferences))
            return;
        if (!TextUtils.isEmpty(value)) {
            if (TextUtils.isEmpty(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY))) {
                PreferencesUtil.getInstance().putString(FrameworkConstant.AES_KEY, Base64Coder.encodeString(AesUtils
                        .getInstance().generateKey()));
            }
            if (value.length() > 128) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i * 128 < value.length(); i++) {
                    String cookiesWithRSA = RSAmethodInRaw.rsaEncrypt(context, value.substring(i * 128, Math.min((i +
                            1) * 128, value.length())));
                    if (i > 0)
                        sb.append(",,,,");
                    sb.append(Base64Coder.encodeString(cookiesWithRSA));
                }
                PreferencesUtil.getInstance().putString(keyInSharedPreferences, AesUtils.getInstance().encrypt
                        (Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY))
                                , sb
                                        .toString()));
            } else {
                String cookiesWithRSA = RSAmethodInRaw.rsaEncrypt(context, value);
                PreferencesUtil.getInstance().putString(keyInSharedPreferences, AesUtils.getInstance().encrypt
                        (Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY)),
                                Base64Coder.encodeString(cookiesWithRSA)));
            }
        }
    }

    private final String decode(Context context, String str) {
        if (null == context || TextUtils.isEmpty(str))
            return null;
        if (!TextUtils.isEmpty(str)) {
            String tm = AesUtils.getInstance().decrypt(Base64Coder.decodeString(PreferencesUtil.getInstance()
                    .getString(FrameworkConstant.AES_KEY)), str);
            if (!TextUtils.isEmpty(tm)) {
                String cookie[] = tm.split(",,,,");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < cookie.length; i++) {
                    String cookiesBase64decode = Base64Coder.decodeString(cookie[i]);
                    if (!TextUtils.isEmpty(cookiesBase64decode)) {
                        sb.append(RSAmethodInRaw.rsaDecrypt(context, cookiesBase64decode));
                    }
                }
                return sb.toString();
            }
        }
        return "";
    }
}
