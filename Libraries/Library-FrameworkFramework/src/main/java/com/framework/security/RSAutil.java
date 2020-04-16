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
import java.security.Key;
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
 * className RSAutil
 * created at  2017/3/13  12:04
 */
public final class RSAutil {
    private final static String RSA = "RSA";
    private final static String RSA_ECB_PKCS1PADDING = "RSA/ECB/PKCS1Padding";

    private static volatile RSAutil singleton;

    private static RSAPublicKey rsaPublicKey;

    private static RSAPrivateKey rsaPrivateKey;

    private RSAutil() {
    }

    public static RSAutil getInstance() {
        if (singleton == null) {
            synchronized (RSAutil.class) {
                if (singleton == null) {
                    singleton = new RSAutil();
                }
            }
        }
        return singleton;
    }

    private KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    private KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(keyLength);
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            KeyPair keyPair = keyPairGenerator.genKeyPair();
            rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
            rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
            return keyPair;
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    /**
     * {{@link #encryptData(byte[], Key, boolean)}}
     */
    public byte[] encryptData(byte[] data, java.security.Key key) {
        return encryptData(data, key, true);
    }

    /**
     * 公钥加密私钥机密或私钥加密公钥解密
     *
     * @param data   待加密的  byte[] data
     * @param key    java.security.Key 公私钥
     * @param stable 加解密中间串是否是固定的，默认不固定
     * @return byte[]
     */
    public byte[] encryptData(byte[] data, java.security.Key key, boolean stable) {
        try {
            Cipher e = Cipher.getInstance(stable ? RSA : RSA_ECB_PKCS1PADDING);
            e.init(Cipher.ENCRYPT_MODE, key);
            return e.doFinal(data);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
    /**
     * {{@link #decryptData(byte[], Key, boolean)}}
     */
    public byte[] decryptData(byte[] encryptedData, java.security.Key key) {
        return decryptData(encryptedData, key, true);
    }

    /**
     * 公钥加密私钥机密或私钥加密公钥解密
     * <p>
     *
     * @param encryptedData 已加密的数据  byte[] encryptedData
     * @param key           java.security.Key 公私钥
     * @param stable        加解密中间串是否是固定的，默认不固定
     * @return byte[]
     */
    public byte[] decryptData(byte[] encryptedData, java.security.Key key, boolean stable) {
        try {
            Cipher e = Cipher.getInstance(stable ? RSA : RSA_ECB_PKCS1PADDING);
            e.init(Cipher.DECRYPT_MODE, key);
            return e.doFinal(encryptedData);
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 简单加密字符串（公钥传入）
     */
    public String encryptData(String data, String publicKey) {
        try {
            byte[] encodeByte = encryptData(data.getBytes(),
                    getPublicKey(Base64Utils.decode(publicKey)));
            if (null != encodeByte) {
                return Base64Coder.encodeLines(encodeByte);
            } else {
                return null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 简单解密密字符串（私钥传入）
     */
    public String decryptData(String data, String privateKey) {
        try {
            byte[] decryptByte = decryptData(Base64Coder.decodeLines(data),
                    getPrivateKey(Base64Utils.decode(privateKey)));
            if (null != decryptByte) {
                return new String(decryptByte);
            } else {
                return null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 简单加密字符串（公私钥自动生成）
     */
    public String encryptData(String data) {
        try {
            if (rsaPublicKey == null) {
                generateRSAKeyPair();
            }
            byte[] encodeByte = encryptData(data.getBytes(), rsaPublicKey);
            if (null != encodeByte) {
                return Base64Coder.encodeString(encodeByte);
            } else {
                return null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    /**
     * 简单解密密字符串（公私钥自动生成）
     */
    public String decryptData(String data) {
        try {
            if (rsaPrivateKey == null) {
                generateRSAKeyPair();
            }
            byte[] decryptByte = decryptData(Base64Coder.decode(data), rsaPrivateKey);
            if (null != decryptByte) {
                return new String(decryptByte);
            } else {
                return null;
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public PublicKey getPublicKey(byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey getPrivateKey(byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public PublicKey getPublicKey(String modulus, String publicExponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey getPrivateKey(String modulus,
                                    String privateExponent) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        BigInteger bigIntModulus = new BigInteger(modulus);
        BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus,
                bigIntPrivateExponent);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    public PublicKey loadPublicKey(String publicKeyStr) throws Exception {
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

    public PrivateKey loadPrivateKey(String privateKeyStr)
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

    public PublicKey loadPublicKey(InputStream in) throws Exception {
        try {
            return loadPublicKey(readKey(in));
        } catch (IOException var2) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException var3) {
            throw new Exception("公钥输入流为空");
        }
    }

    public PrivateKey loadPrivateKey(InputStream in) throws Exception {
        try {
            return loadPrivateKey(readKey(in));
        } catch (IOException var2) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException var3) {
            throw new Exception("私钥输入流为空");
        }
    }

    private String readKey(InputStream in) throws IOException {
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

    public void printPublicKeyInfo(PublicKey publicKey) {
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

    public void printPrivateKeyInfo(PrivateKey privateKey) {
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
