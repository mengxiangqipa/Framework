package com.library.network.okhttp3;

import android.content.Context;

import com.library.network.okhttp3.callback.ICallback;

import org.json.JSONObject;

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
    private static volatile HttpImpl httpImpl;

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
        if (httpImpl == null) {
            synchronized (HttpUtil.class) {
                if (httpImpl == null) {
                    httpImpl = new HttpImpl();
                }
            }
        }
        return singleton;
    }

    public void doPostStringRequest(final Context context, final String url,
                                     final JSONObject data, final ICallback callback) {
        httpImpl.doPostStringRequest(context, url, data, callback);
    }
    public void doPostStringRequest1(final Context context, final String url,
                                    final JSONObject data, final ICallback callback) {
        httpImpl.doPostStringRequest(context, url, data, callback);
    }
}