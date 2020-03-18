package com.framework.security;

import android.content.Context;
import android.text.TextUtils;

import com.framework.config.FrameworkConstant;
import com.framework.util.multyprocessprovider.provider.PreferencesUtil;

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
        if (null == context || TextUtils.isEmpty(keyInSharedPreferences)) {
            return null;
        }
        return decode(context, PreferencesUtil.getInstance().getString(keyInSharedPreferences));
    }

    public final void put(Context context, String keyInSharedPreferences, String value) {
        if (null == context || TextUtils.isEmpty(keyInSharedPreferences)) {
            return;
        }
        if (!TextUtils.isEmpty(value)) {
            if (TextUtils.isEmpty(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY))) {
                PreferencesUtil.getInstance().putString(FrameworkConstant.AES_KEY,
                        Base64Coder.encodeString(AesUtil.getInstance().generateKey()));
            }
            if (value.length() > 128) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i * 128 < value.length(); i++) {
                    String cookiesWithRSA =
                            RSAutil.getInstance().encryptData(value.substring(i * 128, Math.min((i +
                                    1) * 128, value.length())));
                    if (i > 0) {
                        sb.append(",,,,");
                    }
                    sb.append(Base64Coder.encodeString(cookiesWithRSA));
                }
                PreferencesUtil.getInstance().putString(keyInSharedPreferences,
                        AesUtil.getInstance().encrypt(Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY))
                                        , sb.toString()));
            } else {
//                String cookiesWithRSA = RSAmethodInRaw.getInstance().rsaEncrypt(context, value);
                String cookiesWithRSA = RSAutil.getInstance().encryptData(value);
                PreferencesUtil.getInstance().putString(keyInSharedPreferences,
                        AesUtil.getInstance().encrypt
                                (Base64Coder.decodeString(PreferencesUtil.getInstance().getString(FrameworkConstant.AES_KEY)),
                                        Base64Coder.encodeString(cookiesWithRSA)));
            }
        }
    }

    private String decode(Context context, String str) {
        if (null == context || TextUtils.isEmpty(str)) {
            return null;
        }
        if (!TextUtils.isEmpty(str)) {
            String tm =
                    AesUtil.getInstance().decrypt(Base64Coder.decodeString(PreferencesUtil.getInstance()
                            .getString(FrameworkConstant.AES_KEY)), str);
            if (!TextUtils.isEmpty(tm)) {
                StringBuilder sb = new StringBuilder();
                for (String s : tm.split(",,,,")) {
                    String cookiesBase64decode = Base64Coder.decodeString(s);
                    if (!TextUtils.isEmpty(cookiesBase64decode)) {
//                        sb.append(RSAmethodInRaw.getInstance().rsaDecrypt(context,
//                        cookiesBase64decode));
                        sb.append(RSAutil.getInstance().decryptData(cookiesBase64decode));
                    }
                }
                return sb.toString();
            }
        }
        return "";
    }
}
