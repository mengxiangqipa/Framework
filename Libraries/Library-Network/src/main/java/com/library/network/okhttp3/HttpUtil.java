package com.library.network.okhttp3;

/**
 * HttpUtil--封装的网络请求工具(基于Okhttp3)
 *
 * @author YobertJomi
 * className HttpUtil
 * created at  2018/6/19  21:26
 */
public class HttpUtil {

    private final Object object = new Object();// 加互斥锁对象
    private static volatile HttpUtil singleton;

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (singleton == null) {
            synchronized (HttpUtil.class) {
                if (singleton == null) {
                    singleton = new HttpUtil();
                }
            }
        }
        return singleton;
    }


}