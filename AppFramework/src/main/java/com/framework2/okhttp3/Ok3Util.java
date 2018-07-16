package com.framework2.okhttp3;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

/**
 * OK3的封装类
 *
 * @author Yobert Jomi
 * className Ok3Util
 * created at  2016/10/17  15:30
 */
public class Ok3Util {

    private static volatile Ok3Util singleton;
    private final Object object = new Object();//加互斥锁对象
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder defaultBuilder;//默认builer
    private boolean hasBuilder;//是否传递builder

    private Ok3Util() {
    }

    public static Ok3Util getInstance() {
        if (singleton == null) {
            synchronized (Ok3Util.class) {
                if (singleton == null) {
                    singleton = new Ok3Util();
                }
            }
        }
        return singleton;
    }

    public Ok3Util setBuilder(OkHttpClient.Builder builder) {
        return setBuilder(builder, null);
    }

    public Ok3Util setBuilder(OkHttpClient.Builder builder,
                              Interceptor interceptor) {
        if (null != builder) {
            hasBuilder = true;

            synchronized (object) {
                defaultBuilder = null;// 清空builder
            }
            if (null != interceptor) {
                builder.addNetworkInterceptor(interceptor);
            }

            mOkHttpClient = builder.build();
        }
        return singleton;
    }

    /**
     * 默认的OkHttpClient（默认超时8000ms）
     *
     * @return OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        if (null == defaultBuilder && !hasBuilder) {
            synchronized (object) {
                defaultBuilder = new OkHttpClient.Builder();
                defaultBuilder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                        .readTimeout(8000, TimeUnit.MILLISECONDS)
                        .writeTimeout(8000, TimeUnit.MILLISECONDS);
                mOkHttpClient = defaultBuilder.build();
            }
        }
        hasBuilder = false;
        return mOkHttpClient;
    }

    /**
     * 同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     *
     * @param stringRequest 自己封装的Ok3 的Request
     */
    public ResponseBody addToRequestQueueSynchronized(StringRequest stringRequest) {
        if (stringRequest != null) {
            try {
                return getOkHttpClient().newCall(stringRequest.getRequest()).execute().body();
            } catch (Exception e) {
                Log.e("yy", "Ok3Util_exception:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 异步调用
     *
     * @param concurrence   并发设置，true允许并发
     * @param stringRequest 自己封装的Ok3 的Request
     */
    public void addToRequestQueueAsynchoronous(boolean concurrence, StringRequest stringRequest) {
        if (stringRequest != null) {
            getOkHttpClient().newCall(stringRequest.getRequest()).cancel();
            mOkHttpClient = getOkHttpClient();
            mOkHttpClient.dispatcher().setMaxRequestsPerHost(concurrence ? 5 : 1);
            mOkHttpClient.dispatcher().setMaxRequests(concurrence ? 64 : 1);
            mOkHttpClient.newCall(stringRequest.getRequest()).enqueue(stringRequest.getCallBack());
        }
    }

    public void cancel(StringRequest request) {
        if (null != request) {
            List<Call> calls = getOkHttpClient().dispatcher().runningCalls();
            for (Call call : calls) {
                if (call.toString().equals(request.toString()))
                    call.cancel();
            }
        }
    }
}