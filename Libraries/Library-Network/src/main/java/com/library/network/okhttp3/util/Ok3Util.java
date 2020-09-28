package com.library.network.okhttp3.util;

import androidx.annotation.NonNull;

import com.library.network.okhttp3.request.DownloadRequest;
import com.library.network.okhttp3.request.StringRequest;
import com.library.network.okhttp3.request.UploadFileRequest;
import com.library.network.okhttp3.upload_dowload.ProgressResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OK3的封装类
 *
 * @author Yobert Jomi
 * className Ok3Util
 * created at  2016/10/17  15:30
 */
@SuppressWarnings("unused")
public final class Ok3Util {

    private final Object object = new Object();// 加互斥锁对象
    private static volatile Ok3Util singleton;
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
        if (null != builder) {
            hasBuilder = true;
            defaultBuilder = null;//清空builder
            mOkHttpClient = builder.build();
        }
        return singleton;
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
            //每次设置builder后，会产生新的mOkHttpClient对象，导致CollectionPool变多,慎用setBuilder方法
            //，YobertJomi 2018.04.02
            if (null == mOkHttpClient) {
                mOkHttpClient = builder.build();
            }
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
//            X509TrustManager xtm = new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws
//                CertificateException {
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws
//                CertificateException {
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    X509Certificate[] x509Certificates = new X509Certificate[0];
//                    return x509Certificates;
//                }
//            };
//            SSLContext sslContext = null;
//            try {
//                sslContext = SSLContext.getProxyApplication("SSL");
//                sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            }
//            HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            };
//
//            File sdcache = application.getExternalCacheDir();
//            int cacheSize = 10 * 1024 * 1024;
            defaultBuilder = new OkHttpClient.Builder();
            defaultBuilder.connectTimeout(15000, TimeUnit.MILLISECONDS)
                    .readTimeout(8000, TimeUnit.MILLISECONDS)
                    .writeTimeout(8000, TimeUnit.MILLISECONDS)
//                    .sslSocketFactory(sslContext.getSocketFactory())
//                    .hostnameVerifier(DO_NOT_VERIFY)
//                .sslSocketFactory(createSSLSocketFactory())
//                .hostnameVerifier(new TrustAllHostnameVerifier())
//             .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
            ;
            mOkHttpClient = defaultBuilder.build();
        }
        hasBuilder = false;
        return mOkHttpClient;
    }

    /**
     * 同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     *
     * @param request Request
     */
    public ResponseBody addToRequestQueueSynchronized(boolean concurrence,
                                                      @NonNull Request request) {
        try {
            mOkHttpClient = getOkHttpClient();
            mOkHttpClient.dispatcher().setMaxRequestsPerHost(
                    concurrence ? 5 : 1);
            mOkHttpClient.dispatcher().setMaxRequests(concurrence ? 64 : 1);
            return mOkHttpClient.newCall(request).execute().body();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * *同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     * {@link #addToRequestQueueSynchronized(boolean, Request)}
     *
     * @param request Request
     * @return ResponseBody
     * @see #addToRequestQueueSynchronized(boolean, Request)
     */
    public ResponseBody addToRequestQueueSynchronized(@NonNull Request request) {
        return addToRequestQueueSynchronized(true, request);
    }

    /**
     * 异步调用
     *
     * @param request Request
     */
    public void addToRequestQueueAsynchoronous(boolean concurrence, @NonNull Request request,
                                               Callback callback) {
        try {
            mOkHttpClient = getOkHttpClient();
            mOkHttpClient.dispatcher().setMaxRequestsPerHost(
                    concurrence ? 5 : 1);
            mOkHttpClient.dispatcher().setMaxRequests(concurrence ? 64 : 1);
            mOkHttpClient.newCall(request).enqueue(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步调用
     * {@link #addToRequestQueueAsynchoronous(boolean, Request, Callback)}
     *
     * @param request  Request
     * @param callback Callback
     * @see #addToRequestQueueAsynchoronous(boolean, Request, Callback)
     */
    public void addToRequestQueueAsynchoronous(@NonNull Request request, Callback callback) {
        addToRequestQueueAsynchoronous(true, request, callback);
    }

    /**
     * 异步调用--下载文件
     *
     * @param concurrence 并发设置，true允许并发
     * @param request     Request
     * @param callback    Callback
     */
    public void downloadFile(boolean concurrence,
                             @NonNull Request request, Callback callback,
                             final ProgressResponseBody.ProgressListener progressListener) {
        try {
            // 拦截器，用上ProgressResponseBody
            Interceptor interceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse
                            .newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(),
                                    progressListener))
                            .build();
                }
            };
            setBuilder(
                    new OkHttpClient.Builder()
                            .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                            .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                            .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                            .retryOnConnectionFailure(true), interceptor);
            OkHttpClient mOkHttpClient = getOkHttpClient();

            mOkHttpClient.dispatcher().setMaxRequestsPerHost(
                    concurrence ? 5 : 1);
            mOkHttpClient.dispatcher().setMaxRequests(concurrence ? 64 : 1);
            mOkHttpClient.newCall(request).enqueue(callback);
            defaultBuilder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * {@link #downloadFile(boolean, Request, Callback, ProgressResponseBody.ProgressListener)} )}
     *
     * @param request  Request
     * @param callback Callback
     * @see #downloadFile(boolean, Request, Callback, ProgressResponseBody.ProgressListener)
     */
    public void downloadFile(@NonNull Request request, Callback callback,
                             final ProgressResponseBody.ProgressListener progressListener) {
        downloadFile(true, request, callback, progressListener);
    }
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/
    /*=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>=====>>*/

    /**
     * 同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     *
     * @param stringRequest 自己封装的Ok3 的Request
     */
    public ResponseBody addToRequestQueueSynchronized(boolean concurrence,
                                                      StringRequest stringRequest) {
        return addToRequestQueueSynchronized(concurrence, stringRequest.getRequest());
    }

    /**
     * *同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     * {@link #addToRequestQueueSynchronized(boolean, StringRequest)}
     *
     * @param stringRequest stringRequest
     * @return ResponseBody
     * @see #addToRequestQueueSynchronized(boolean, StringRequest)
     */
    public ResponseBody addToRequestQueueSynchronized(StringRequest stringRequest) {
        return addToRequestQueueSynchronized(true, stringRequest);
    }

    /**
     * 异步调用
     *
     * @param stringRequest 自己封装的Ok3 的Request
     */
    public void addToRequestQueueAsynchoronous(boolean concurrence, StringRequest stringRequest) {
        addToRequestQueueAsynchoronous(concurrence, stringRequest.getRequest(), stringRequest);
    }

    /**
     * 异步调用
     * {@link #addToRequestQueueAsynchoronous(boolean, StringRequest)}
     *
     * @param stringRequest stringRequest
     * @see #addToRequestQueueAsynchoronous(boolean, StringRequest)
     */
    public void addToRequestQueueAsynchoronous(StringRequest stringRequest) {
        addToRequestQueueAsynchoronous(true, stringRequest);
    }

    /**
     * 异步调用--下载文件
     *
     * @param concurrence     并发设置，true允许并发
     * @param downloadRequest 自己封装的Ok3 的Request
     */
    public void downloadFile(boolean concurrence,
                             DownloadRequest downloadRequest,
                             final ProgressResponseBody.ProgressListener progressListener) {

        downloadFile(concurrence, downloadRequest.getRequest(), downloadRequest, progressListener);
    }

    /**
     * 下载文件
     * {@link #downloadFile(boolean, DownloadRequest, ProgressResponseBody.ProgressListener)} )}
     *
     * @param downloadRequest downloadRequest
     * @see #downloadFile(boolean, DownloadRequest, ProgressResponseBody.ProgressListener)
     */
    public void downloadFile(DownloadRequest downloadRequest,
                             final ProgressResponseBody.ProgressListener progressListener) {
        downloadFile(true, downloadRequest, progressListener);
    }

    /**
     * 同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     *
     * @param uploadFileRequest UploadFileRequest
     */
    public ResponseBody addToRequestQueueSynchronized(boolean concurrence,
                                                      UploadFileRequest uploadFileRequest) {
        return addToRequestQueueSynchronized(concurrence, uploadFileRequest.getRequest());
    }

    /**
     * *同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     * {@link #addToRequestQueueSynchronized(boolean, UploadFileRequest)}
     *
     * @param uploadFileRequest UploadFileRequest
     * @return ResponseBody
     * @see #addToRequestQueueSynchronized(boolean, UploadFileRequest)
     */
    public ResponseBody addToRequestQueueSynchronized(UploadFileRequest uploadFileRequest) {
        return addToRequestQueueSynchronized(true, uploadFileRequest);
    }

    /**
     * 异步调用
     *
     * @param uploadFileRequest UploadFileRequest
     */
    public void addToRequestQueueAsynchoronous(boolean concurrence,
                                               UploadFileRequest uploadFileRequest) {
        addToRequestQueueAsynchoronous(concurrence, uploadFileRequest.getRequest(),
                uploadFileRequest);
    }

    /**
     * 异步调用
     * {@link #addToRequestQueueAsynchoronous(boolean, UploadFileRequest)}
     *
     * @param uploadFileRequest UploadFileRequest
     * @see #addToRequestQueueAsynchoronous(boolean, UploadFileRequest)
     */
    public void addToRequestQueueAsynchoronous(UploadFileRequest uploadFileRequest) {
        addToRequestQueueAsynchoronous(true, uploadFileRequest);
    }

    /**
     * 同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     *
     * @param downloadRequest DownloadRequest
     */
    public ResponseBody addToRequestQueueSynchronized(boolean concurrence,
                                                      DownloadRequest downloadRequest) {
        return addToRequestQueueSynchronized(concurrence, downloadRequest.getRequest());
    }

    /**
     * *同步调用---这个是在主线程里面执行网络请求，调用这个方法时要在子线程`
     * {@link #addToRequestQueueSynchronized(boolean, DownloadRequest)}
     *
     * @param downloadRequest DownloadRequest
     * @return ResponseBody
     * @see #addToRequestQueueSynchronized(boolean, DownloadRequest)
     */
    public ResponseBody addToRequestQueueSynchronized(DownloadRequest downloadRequest) {
        return addToRequestQueueSynchronized(true, downloadRequest);
    }

    /**
     * 异步调用
     *
     * @param downloadRequest DownloadRequest
     */
    public void addToRequestQueueAsynchoronous(boolean concurrence,
                                               DownloadRequest downloadRequest) {
        addToRequestQueueAsynchoronous(concurrence, downloadRequest.getRequest(), downloadRequest);
    }

    /**
     * 异步调用
     * {@link #addToRequestQueueAsynchoronous(boolean, DownloadRequest)}
     *
     * @param downloadRequest DownloadRequest
     * @see #addToRequestQueueAsynchoronous(boolean, DownloadRequest)
     */
    public void addToRequestQueueAsynchoronous(DownloadRequest downloadRequest) {
        addToRequestQueueAsynchoronous(true, downloadRequest);
    }

    /**
     * 取消OKHTTP3的请求，网络请求已发出，调用可以取消
     *
     * @param request request
     */
    public void cancel(Request request) {
        if (null != request) {
            List<Call> calls = getOkHttpClient().dispatcher().runningCalls();
            for (Call call : calls) {
                if (call.request().toString().equals(request.toString())) {
                    call.cancel();
                }
            }
        }
    }

    /**
     * 取消所有请求
     */
    public void cancelAllRequest() {
        try {
            List<Call> calls = getOkHttpClient().dispatcher().runningCalls();
            for (Call call : calls) {
                call.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}