package com.framework.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import java.security.MessageDigest;

/**
 * 签名工具
 *
 * @author YobertJomi
 * className SignUtil
 * created at  2020/10/13  18:30
 */
public class SignUtil {

    private static volatile SignUtil singleTonInstance;

    public static SignUtil getInstance() {
        if (null == singleTonInstance) {
            synchronized (SignUtil.class) {
                if (null == singleTonInstance) {
                    singleTonInstance = new SignUtil();
                }
            }
        }
        return singleTonInstance;
    }

    /**
     * 检测当前应用的签名信息，若不相同则自动退出
     */
    public boolean checkSignature(final Context context, final String relSign) {
        if (null == context || TextUtils.isEmpty(relSign)) {
            return false;
        }
        String sign = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            sign = getSignature(context);
        }
        return relSign.equals(sign);
    }

    /**
     * 获取应用的签名信息
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public String getSignature(Context context) {
        if (null == context) {
            return null;
        }
        String packageName = getPackageName(context);
        return getSign(context, packageName);
    }

    /**
     * 获取acitivty所在的应用包名
     */
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    public String getPackageName(Context context) {
        if (null == context) {
            return null;
        }
        ApplicationInfo appInfo = context.getApplicationInfo();
        return appInfo.packageName; // 获取当前应用包名
    }

    /**
     * 获取包名对应应用的签名信息
     */
    public String getSign(Context paramContext, String packageName) {
        String sign;
        try {
            byte[] array = null;
            @SuppressLint("PackageManagerGetSignatures") PackageInfo localPackageInfo =
                    paramContext.getPackageManager().getPackageInfo(packageName,
                            PackageManager.GET_SIGNATURES);
            for (int i = 0; i < localPackageInfo.signatures.length; i++) {
                array = localPackageInfo.signatures[i].toByteArray();
                if (array != null) {
                    break;
                }
            }
            sign = MD5(array);
        } catch (Exception ex) {
            return null;
        }
        return sign;
    }

    /**
     * 计算MD5值
     */
    public String MD5(String data) {
        try {
            return MD5(data.getBytes());
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 计算MD5值
     */
    public String MD5(byte[] data) {
        try {
            // 获取data的MD5摘要
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // mdInst.update(content.getBytes());
            mdInst.update(data);
            byte[] md = mdInst.digest();

            // 转换为十六进制的字符串形式
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < md.length; i++) {
                String shaHex = Integer.toHexString(md[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
