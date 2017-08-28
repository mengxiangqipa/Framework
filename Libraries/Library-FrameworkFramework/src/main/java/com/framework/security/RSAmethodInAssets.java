package com.framework.security;

import android.content.Context;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;


public class RSAmethodInAssets {
	/**
	 * RSA数据加密
	 *
	 * @param phone
	 */
	public static String rsaEncrypt(Context context, String phone) {
		try {
			InputStream inPublic = context.getResources().getAssets()
					.open("rsa_public_key.pem");
			PublicKey publicKey = RSAutils.loadPublicKey(inPublic);
			// 加密
			byte[] encryptByte = RSAutils.encryptData(phone.getBytes(),
					publicKey);
			return new String(Base64Coder.encode(encryptByte));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * RSA数据解密
	 *
	 * @param user
	 * @return
	 */
	public static String rsaDecrypt(Context context, String user) {
		String phone = "";
		try {
			InputStream inPrivate = context.getResources().getAssets()
					.open("pkcs8_rsa_private_key.pem");
			PrivateKey privateKey = RSAutils.loadPrivateKey(inPrivate);
			byte[] decryptByte1 = RSAutils.decryptData(
					Base64Coder.decode(user), privateKey);
			phone = new String(decryptByte1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return phone;
	}
}
