package com.framework.security;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author YobertJomi
 * className RSAutils
 * created at  2017/3/13  12:04
 */
public final class RSAutils {
    private static String RSA = "RSA";

    public RSAutils() {
    }

    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator e = KeyPairGenerator.getInstance(RSA);
            e.initialize(keyLength);
            return e.genKeyPair();
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher e = Cipher.getInstance(RSA);
            e.init(1, publicKey);
            return e.doFinal(data);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher e = Cipher.getInstance(RSA);
            e.init(2, privateKey);
            return e.doFinal(encryptedData);
        } catch (Exception var3) {
            return null;
        }
    }

    public static PublicKey getPublicKey(byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String modulus,
                                           String privateExponent) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] e = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(e);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException var5) {
            throw new Exception("公钥非法");
        } catch (NullPointerException var6) {
            throw new Exception("公钥数据为空");
        }
    }

    public static PrivateKey loadPrivateKey(String privateKeyStr)
            throws Exception {
        try {
            byte[] e = Base64Utils.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(e);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException var4) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException var5) {
            throw new Exception("私钥非法");
        } catch (NullPointerException var6) {
            throw new Exception("私钥数据为空");
        }
    }

    public static PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException var2) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException var3) {
            throw new Exception("公钥输入流为空");
        }
    }

    public static PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException var2) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException var3) {
            throw new Exception("私钥输入流为空");
        }
    }

    private static String readKey(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();

        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) != 45) {
                sb.append(readLine);
                sb.append('\r');
            }
        }

        return sb.toString();
    }

    public static void printPublicKeyInfo(PublicKey publicKey) {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
        System.out.println("----------RSAPublicKey----------");
        System.out.println("Modulus.length="
                + rsaPublicKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPublicKey.getModulus().toString());
        System.out.println("PublicExponent.length="
                + rsaPublicKey.getPublicExponent().bitLength());
        System.out.println("PublicExponent="
                + rsaPublicKey.getPublicExponent().toString());
    }

    public static void printPrivateKeyInfo(PrivateKey privateKey) {
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
        System.out.println("----------RSAPrivateKey ----------");
        System.out.println("Modulus.length="
                + rsaPrivateKey.getModulus().bitLength());
        System.out.println("Modulus=" + rsaPrivateKey.getModulus().toString());
        System.out.println("PrivateExponent.length="
                + rsaPrivateKey.getPrivateExponent().bitLength());
        System.out.println("PrivatecExponent="
                + rsaPrivateKey.getPrivateExponent().toString());
    }
}
