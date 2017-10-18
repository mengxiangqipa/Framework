package com.framework.security;


import java.io.UnsupportedEncodingException;


public class Base64Util {
    private static final int trimLength = 32;

    /**
     * 交换字符串位置<br>
     * 规则：分为4段<br>
     * 加密时：1与4交换<br>
     * 解密时：1与4交换<br>
     *
     * @param data
     * @return
     */
    public static String exchangePalce(String data) {
        int remainder = data.length() % 4;
        int length = data.length() / 4;

        String data1 = "", data2 = "", data3 = "", data4 = "", data5 = "";

        if (remainder != 0) {
            data5 = data.substring(length * 4);
        }

        //按顺序截取4段
        data1 = data.substring(0, length);
        data2 = data.substring(length, length * 2);
        data3 = data.substring(length * 2, length * 3);
        data4 = data.substring(length * 3, length * 4);

        return data4 + data2 + data3 + data1 + data5;
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        String baseStr = new String(Base64Utils.encode(str.getBytes("UTF-8")));
        baseStr = exchangePalce(baseStr);
        String tempStr = digest(str, "UTF-8").toUpperCase();
        return new String(Base64Coder.encode((tempStr + baseStr).getBytes("UTF-8")));
    }

    public static String decode(String cryptoStr)
            throws UnsupportedEncodingException {
        if (cryptoStr.length() < trimLength)
            return "";
        try {
            String tempStr = new String(Base64Utils.decode(cryptoStr));
            String result = tempStr.substring(trimLength, tempStr.length());
            result = exchangePalce(result);
            return new String(Base64Utils.decode(result));
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
        return "";
    }

    public static String digest(String aValue) {
        return digest(aValue, "UTF-8");
    }

    private static String digest(String aValue, String encoding) {
        aValue = aValue.trim();
        byte[] value;
        try {
            value = aValue.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            value = aValue.getBytes();
        }
//		value = Md5Util.MD5Normal(value).getBytes();
        try {
            value = MD5.md5(new String(value)).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
        return new String(value);
    }
}
