package com.framework.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则匹配util
 *
 * @author YobertJomi
 * className RegularUtil
 * created at  2017/4/12  10:42
 */
public class RegularUtil {

    private static volatile RegularUtil singleton;

    private RegularUtil() {
    }

    public static RegularUtil getInstance() {
        if (singleton == null) {
            synchronized (RegularUtil.class) {
                if (singleton == null) {
                    singleton = new RegularUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 匹配邮箱
     *
     * @param email email
     * @return boolean
     */
    public boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
//        String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\" +
//                ".]*[a-zA-Z]$";
        //这个是一个企业级的程序里copy出来的
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证号码的有效性
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186、166</p>
     * <p>电信：133、153、173、177、180、181、189、199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     *
     * @param mobile mobile
     * @return boolean
     */
    public boolean isMobileNO(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[0,3,5-8])|(18[0-9])|166|198|199|147)\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * 验证手机号(1开头的15位数字)
     *
     * @param number number
     * @return boolean
     */
    public boolean isMobileNOSimple(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^1\\d{10}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 验证数字
     *
     * @param number number
     * @return boolean
     */
    public boolean isNumberAtLeast15bit(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^[0-9]{15,}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 验证数字
     *
     * @param number number
     * @return boolean
     */
    public boolean isNumberAtLeast6bit(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^[0-9]{6,}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public boolean isNumberAtLeast1bit(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern p = Pattern
                .compile("^[0-9]+$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 验证数字字母
     *
     * @param str str
     * @return boolean
     */
    public boolean isCharOrNumber(String str) {
        Pattern p = Pattern
                .compile("^[_a-zA-Z0-9]{6,}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证中文字符
     *
     * @param str str
     * @return boolean
     */
    public boolean isChineseChar(String str) {
        Pattern p = Pattern
                .compile("\"[\\u4e00-\\u9fa5]\"");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 验证小数点后3位
     *
     * @param str str
     * @return boolean
     */
    public boolean isAtleast3bitNumberAfterDigit(String str) {
        Pattern p = Pattern
                .compile("^[0-9]*[\\.][0-9]{3,}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 从短信字符串提取验证码
     *
     * @param body      短信内容
     * @param YZMLENGTH 验证码的长度 一般6位或者4位
     * @return 接取出来的验证码
     */
    public String getCaptcha(String body, int YZMLENGTH) {
        // 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
        // (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
        // (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现
        //  获得数字字母组合
        //    Pattern p = Pattern   .compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");

        //  获得纯数字
        Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + YZMLENGTH + "})(?![0-9])");

        Matcher m = p.matcher(body);
        if (m.find()) {
            System.out.println(m.group());
            return m.group(0);
        }
        return null;
    }

    /**
     * 验证强密码
     *
     * @param pwd pwd
     */
    public int isPwdStrong(String pwd) {
        try {
            // 弱：纯数字，纯字母，纯特殊字符
            Pattern p1 = Pattern.compile("^(?:\\d+|[a-zA-Z]+|[!@#$%^&*]+)$");
            // 中：字母+数字，字母+特殊字符，数字+特殊字符
            Pattern p2 = Pattern
                    .compile("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$");
            // 强：字母+数字+特殊字符
            Pattern p3 = Pattern
                    .compile("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*]+$)" +
                            "(?![\\d!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$");
            Matcher m1 = p1.matcher(pwd);
            Matcher m2 = p2.matcher(pwd);
            Matcher m3 = p3.matcher(pwd);
            if (m1.matches()) {
                return 0;
            } else if (m2.matches()) {
                return 1;
            } else if (m3.matches()) {
                return 2;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * @param str 用户名（汉字、字母、数字的组合）
     * @return boolean
     */
    public boolean isCommonUserName(String str) {
        Pattern p = Pattern
                .compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param str 密码（8-24位数字和字母的组合）
     * @return boolean
     */
    public boolean isCommonPwd(String str) {
        Pattern p = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,24}$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param str 特殊密码（8-24位数字和字母的组合）
     * @return boolean
     */
    public boolean isSpecialPwd(String str) {
        Pattern p1 = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,24}$");
        // 中：字母+数字，字母+特殊字符，数字+特殊字符
        Pattern p2 = Pattern
                .compile("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$");
        // 强：字母+数字+特殊字符
        Pattern p3 = Pattern
                .compile("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)(?![a-zA-z\\d]+$)(?![a-zA-z!@#$%^&*]+$)" +
                        "(?![\\d!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$");
        Matcher m = p1.matcher(str);
        Matcher m1 = p1.matcher(str);
        Matcher m2 = p2.matcher(str);
        Matcher m3 = p3.matcher(str);
        return m1.matches() || m2.matches();
    }

    /**
     * @param str 年龄（1岁--120岁）
     * @return boolean
     */
    public boolean isCommonAge(String str) {
        Pattern p = Pattern
                .compile("^(?:[1-9][0-9]?|1[01][0-9]|120)$$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @return 是否含特殊字符
     */
    public boolean containsSpecialChar(String str) {
        Pattern p = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？/：；\\\"]");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * （汉字、字母组合）
     *
     * @return boolean
     */
    public boolean isChineseCharactersAndLetter(String str) {
        Pattern p = Pattern
                .compile("^(?!_)(?!.*?_$)[a-zA-Z_\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * （汉字）
     *
     * @return boolean
     */
    public boolean isChineseCharacters(String str) {
        Pattern p = Pattern
                .compile("^(?!_)(?!.*?_$)[\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * （汉字、字母组合）
     *
     * @return boolean
     */
    public boolean isLetter(String str) {
        Pattern p = Pattern
                .compile("^(?!_)(?!.*?_$)[a-zA-Z_]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param str 用户名（汉字、字母组合）
     * @return boolean
     */
    public boolean isChineseCharactersOrLetter(String str) {
        Pattern p = Pattern
                .compile("^(?!_)(?!.*?_$)[a-zA-Z_\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @param str 用户名（汉字、字母组合）
     * @return boolean
     */
    public boolean isChineseCharactersOrLetterOrNumber(String str) {
        Pattern p = Pattern
                .compile("^[a-zA-Z\\d\\u4e00-\\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public String removeChineseAndSpace(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * @param str 含数字，大写字母，小写字母，特殊字符其中之三
     * @return boolean
     */
    public boolean isSpecial(String str) {
        // 必填字母数字特殊字符(数字+大写或小写+特殊)
        String regAll = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z0-9\\S]+$";
        //小写字母+大写字母+数字
        String reg1 = "^(?![0-9]+$)(?![^0-9]+$)(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-z0-9]+$)(?![A-Z0-9]+$)[a-zA-Z0-9]+$";
        //特殊字符+大写字母+数字
        String reg2 = "^(?![0-9]+$)(?![^0-9]+$)(?![A-Z]+$)(?![^A-Z]+$)(?![A-Z0-9]+$)(?![a-zA-Z0-9]+$)[A-Z0-9\\S]+$";
        //特殊字符+小写字母+数字
        String reg3 = "^(?![0-9]+$)(?![^0-9]+$)(?![a-z]+$)(?![^a-z]+$)(?![a-z0-9]+$)(?![a-zA-Z0-9]+$)[a-z0-9\\S]+$";
        //特殊字符+小写字母+大写字母
        String reg4 = "^(?![a-zA-Z]+$)(?![^a-zA-Z]+$)(?![a-zA-Z0-9]+$)[a-zA-Z\\S]+$";
        Pattern p01 = Pattern.compile(reg1);
        Pattern p02 = Pattern.compile(reg2);
        Pattern p03 = Pattern.compile(reg3);
        Pattern p04 = Pattern.compile(reg4);
        Pattern pAll = Pattern.compile(regAll);
        Matcher matcher01 = p01.matcher(str);
        Matcher matcher02 = p02.matcher(str);
        Matcher matcher03 = p03.matcher(str);
        Matcher matcher04 = p04.matcher(str);
        Matcher matcherAll = pAll.matcher(str);
//        Y.y("isIOVpwd--" + "小写字母+大写字母+数字：" + matcher01.matches());
//        Y.y("isIOVpwd--" + "特殊字符+大写字母+数字：" + matcher02.matches());
//        Y.y("isIOVpwd--" + "特殊字符+小写字母+数字：" + matcher03.matches());
//        Y.y("isIOVpwd--" + "特殊字符+小写字母+大写字母：" + matcher04.matches());
//        Y.y("isIOVpwd--" + "特殊字符+小写字母或者大写字母+数字：              " + matcherAll.matches());
        return matcher01.matches() || matcher02.matches() || matcher03.matches() || matcher04.matches() || matcherAll
                .matches();
    }

    public boolean isIDNumber(String IDNumber) {
        if (TextUtils.isEmpty(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)" +
                "\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾
        boolean matches = IDNumber.matches(regularExpression);
        //判断第18位校验值
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
//                        System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +
//                                "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    System.out.println("异常:" + IDNumber);
                    return false;
                }
            }
        }
        return matches;
    }

    /**
     * 校验银行卡卡号
     * <p>
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public boolean isBankCard(String bankCard) {
        if (TextUtils.isEmpty(bankCard) || bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard nonCheckCodeBankCard
     */
    private char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
}