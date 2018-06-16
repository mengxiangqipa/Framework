package com.framework.security;

import android.content.Context;
import android.util.Log;

import com.framework.R;
import com.framework.utils.Y;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAmethodInRaw {
    /**
     * RSA数据加密
     *
     * @param data
     */
    public static String rsaEncrypt(Context context, String data) {
        try {
//			InputStream inPublic = context.getResources().getAssets()
//					.open("rsa_public_key.pem");
            InputStream inPublic = context.getResources().openRawResource(R.raw.rsa_public_key);
            PublicKey publicKey = RSAutils.loadPublicKey(inPublic);
            // 加密
            byte[] encryptByte = RSAutils.encryptData(data.getBytes(),
                    publicKey);
            return new String(Base64Coder.encode(encryptByte));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("yy", "Exception:" + e.getMessage());
            return "";
        }
    }

    /**
     * RSA数据解密
     *
     * @param data
     * @return
     */
    public static String rsaDecrypt(Context context, String data) {
        String phone = "";
        try {
//			InputStream inPrivate = context.getResources().getAssets()
//					.open("pkcs8_rsa_private_key.pem");
            InputStream inPrivate = context.getResources().openRawResource(R.raw.pkcs8_rsa_private_key);
            PrivateKey privateKey = RSAutils.loadPrivateKey(inPrivate);
            byte[] decryptByte1 = RSAutils.decryptData(
                    Base64Coder.decode(data), privateKey);
            phone = new String(decryptByte1);
        } catch (Exception e) {
            e.printStackTrace();
            Y.y("Exception:" + e.getMessage());
            return "";
        }
        return phone;
    }
}
