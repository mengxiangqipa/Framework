package com.framework.security;

import android.annotation.SuppressLint;
import android.os.Build;
import androidx.annotation.IntDef;
import android.text.TextUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author YobertJomi
 * className AesUtil
 * created at  2019/05/31  15:03
 * ① AesUtil.getProxyApplication().generateKey();
 * ② AesUtil.getProxyApplication().encrypt
 * ③ AesUtil.getProxyApplication().decrypt
 */
public class AesUtil {

    private final static String HEX = "0123456789ABCDEF";
    /**
     * AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    /**
     * AES 加密
     */
    private static final String AES = "AES";
    /**
     * SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
     */
    private static final String SHA1PRNG = "SHA1PRNG";
    private static final int KEY_SIZE = 32;
    private static volatile AesUtil singleton;

    private AesUtil() {
    }

    public static AesUtil getInstance() {
        if (singleton == null) {
            synchronized (AesUtil.class) {
                if (singleton == null) {
                    singleton = new AesUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 对密钥进行处理
     */
    @SuppressLint("DeletedProvider")
    private byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        //for android
        SecureRandom secureRandom;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        //android 9.0及以上版本废弃CryptoProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(new String(seed,
//            StandardCharsets.ISO_8859_1)
//            .getBytes(StandardCharsets.ISO_8859_1), KEY_SIZE);
            return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(seed, KEY_SIZE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            secureRandom = SecureRandom.getInstance(SHA1PRNG, new CryptoProvider());
        } else if (Build.VERSION.SDK_INT >= 17) {
            secureRandom = SecureRandom.getInstance(SHA1PRNG, "Crypto");
        } else {
            secureRandom = SecureRandom.getInstance(SHA1PRNG);
        }
        // for Java
        // secureRandom = SecureRandom.getProxyApplication(SHA1PRNG);
        secureRandom.setSeed(seed);
        keyGenerator.init(128, secureRandom); //256 bits or 128 bits,192bits
        //AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
        SecretKey skey = keyGenerator.generateKey();
        return skey.getEncoded();
    }

    /**
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     */
    public String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            return toHex(bytes_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     */
    public String encrypt(String key, String originalText) {
        if (TextUtils.isEmpty(originalText)) {
            return originalText;
        }
        try {
            byte[] result = encrypt(key, originalText.getBytes());
            return Base64Coder.encodeLines(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     */
    private byte[] encrypt(String key, byte[] original) throws Exception {
        return encryptDecrypt(key, original, Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     */
    public String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64Coder.decodeLines(encrypted);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    private byte[] decrypt(String key, byte[] encrypted) throws Exception {
        return encryptDecrypt(key, encrypted, Cipher.DECRYPT_MODE);
    }

    /**
     * 加密/解密
     */
    private byte[] encryptDecrypt(String key, byte[] originalOrEncrypted,
                                  @AESType int encryptDecryptType) throws Exception {
        byte[] raw;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            raw = getRawKey(key.getBytes(StandardCharsets.ISO_8859_1));
        } else {
            raw = getRawKey(key.getBytes((Charset.forName("ISO-8859-1"))));
        }
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(encryptDecryptType, skeySpec,
                new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(originalOrEncrypted);
    }

    /**
     * 二进制转字符
     */
    private String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte b : buf) {
            appendHex(result, b);
        }
        return result.toString();
    }

    private void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    @IntDef({Cipher.ENCRYPT_MODE, Cipher.DECRYPT_MODE})
    @interface AESType {
    }

    private static final class CryptoProvider extends Provider {
        CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto" +
                    ".SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    public void test() {
//        List<Person> personList = new ArrayList<>();
//        int testMaxCount = 1000;//测试的最大数据条数
//        //添加测试数据
//        for (int i = 0; i < testMaxCount; i++) {
//            Person person = new Person();
//            person.setAge(i);
//            person.setName(String.valueOf(i));
//            personList.add(person);
//        }
//        //FastJson生成json数据
//        String jsonData = JsonUtils.objectToJsonForFastJson(personList);
//        Log.e("MainActivity", "AES加密前json数据 ---->" + jsonData);
//        Log.e("MainActivity", "AES加密前json数据长度 ---->" + jsonData.length());
//
//        //生成一个动态key
//        String secretKey = AesUtil.generateKey();
//        Log.e("MainActivity", "AES动态secretKey ---->" + secretKey);
//
//        //AES加密
//        long start = System.currentTimeMillis();
//        String encryStr = AesUtil.encrypt(secretKey, jsonData);
//        long end = System.currentTimeMillis();
//        Log.e("MainActivity", "AES加密耗时 cost time---->" + (end - start));
//        Log.e("MainActivity", "AES加密后json数据 ---->" + encryStr);
//        Log.e("MainActivity", "AES加密后json数据长度 ---->" + encryStr.length());
//
//        //AES解密
//        start = System.currentTimeMillis();
//        String decryStr = AesUtil.decrypt(secretKey, encryStr);
//        end = System.currentTimeMillis();
//        Log.e("MainActivity", "AES解密耗时 cost time---->" + (end - start));
//        Log.e("MainActivity", "AES解密后json数据 ---->" + decryStr);
    }
}