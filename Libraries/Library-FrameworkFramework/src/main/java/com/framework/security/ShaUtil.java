package com.framework.security;

import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * Sha2工具
 */
public class ShaUtil {

    public static byte[] sha256(byte[] dataBytes) {
        if (dataBytes == null) {
            return null;
        }
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // 计算SHA1摘要
        return sha.digest(dataBytes);
    }

    /**
     * 计算SHA256摘要
     *
     * @param data String 原始数据
     * @return SHA256摘要
     */
    public static String getSha256(String data) {
        return getSha(data, "SHA-256");
    }

    /**
     * 计算SHA256摘要
     *
     * @param data      String 原始数据
     * @param algorithm String 加密因子（SHA-256  SHA-344）
     * @return SHA摘要
     */
    public static String getSha(String data, String algorithm) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(algorithm)) {
            return null;
        }
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes("UTF-8"));
            return byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
