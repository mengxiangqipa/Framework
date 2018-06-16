package com.framework.security;

import android.content.Context;
import android.text.TextUtils;

import com.framework.configs.FrameConstant;
import com.framework.utils.multyprocessprovider.provider.PreferencesUtil;

/**
 * @author YobertJomi
 * className SessionManagerUtil
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
        return decode(context, PreferencesUtil.getInstance().getString(keyInSharedPreferences));
    }

    public final void put(Context context, String keyInSharedPreferences, String value) {
        if (!TextUtils.isEmpty(value)) {
            if (TextUtils.isEmpty(PreferencesUtil.getInstance().getString(FrameConstant.AES_KEY))) {
                PreferencesUtil.getInstance().putString(FrameConstant.AES_KEY, Base64Coder.encodeString(AesUtils
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
                        (Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameConstant.AES_KEY)), sb
                                .toString()));
            } else {
                String cookiesWithRSA = RSAmethodInRaw.rsaEncrypt(context, value);
                PreferencesUtil.getInstance().putString(keyInSharedPreferences, AesUtils.getInstance().encrypt
                        (Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameConstant.AES_KEY)),
                                Base64Coder.encodeString(cookiesWithRSA)));
            }
        }
    }

    private final String decode(Context context, String str) {
        if (!TextUtils.isEmpty(str)) {
            String tm = AesUtils.getInstance().decrypt(Base64Coder.decodeString(PreferencesUtil.getInstance()
                    .getString(FrameConstant.AES_KEY)), str);
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
