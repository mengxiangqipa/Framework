package com.framework.security;

import android.content.Context;

import com.framework.R;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAmethodInRaw {

    private static volatile RSAmethodInRaw singleton;

    private RSAmethodInRaw() {
    }

    public static RSAmethodInRaw getInstance() {
        if (singleton == null) {
            synchronized (RSAmethodInRaw.class) {
                if (singleton == null) {
                    singleton = new RSAmethodInRaw();
                }
            }
        }
        return singleton;
    }

    /**
     * RSA数据加密
     *
     * @param data
     */
    public String rsaEncrypt(Context context, String data) {
        try {
            InputStream inPublic = context.getResources().openRawResource(R.raw.rsa_public_key);
            PublicKey publicKey = RSAutil.getInstance().loadPublicKey(inPublic);
            // 加密
            byte[] encryptByte = RSAutil.getInstance().encryptData(data.getBytes(), publicKey,true);
            return new String(Base64Coder.encode(encryptByte));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * RSA数据解密
     *
     * @param data
     * @return
     */
    public String rsaDecrypt(Context context, String data) {
        String phone;
        try {
            InputStream inPrivate =
                    context.getResources().openRawResource(R.raw.pkcs8_rsa_private_key);
            PrivateKey privateKey = RSAutil.getInstance().loadPrivateKey(inPrivate);
            byte[] decryptByte1 = RSAutil.getInstance().decryptData(
                    Base64Coder.decode(data), privateKey,true);
            phone = new String(decryptByte1);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return phone;
    }
}
