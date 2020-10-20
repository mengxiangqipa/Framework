package com.library.network.okhttp3.util;

import android.content.Context;

import com.library.network.okhttp3.api.BaseHttpAPI;
import com.library.network.okhttp3.api.HttpAPIFactory;
import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private static volatile BaseHttpAPI httpAPI;

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
        if (httpAPI == null) {
            synchronized (HttpUtil.class) {
                if (httpAPI == null) {
                    httpAPI = HttpAPIFactory.creatHttpAPI();
                }
            }
        }
        return singleton;
    }

    public void postStringHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                  final String data, final boolean callBackOnUiThread, final ICallback callback) {
        httpAPI.doStringHttpRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_POST.getMethod(), headers, data, callBackOnUiThread, callback);
    }

    public void postStringHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                  final String data, final ICallback callback) {
        postStringHttpRequest(context, url, headers, data, true, callback);
    }

    public void postJsonHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final boolean callBackOnUiThread, final ICallback callback) {
        httpAPI.doJsonHttpRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_POST.getMethod(), headers, data, callBackOnUiThread, callback);
    }

    public void postJsonHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final ICallback callback) {
        postJsonHttpRequest(context, url, headers, data, true, callback);
    }

    public void getStringHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                      final String data, final boolean callBackOnUiThread, final ICallback callback) {
        httpAPI.doStringHttpRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_GET.getMethod(), headers, data, callBackOnUiThread, callback);
    }

    public void getStringHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                      final String data, final ICallback callback) {
        postStringHttpRequest(context, url, headers, data, true, callback);
    }

    public void getJsonHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final boolean callBackOnUiThread, final ICallback callback) {
        httpAPI.doJsonHttpRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_GET.getMethod(), headers, data, callBackOnUiThread, callback);
    }

    public void getJsonHttpRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final ICallback callback) {
        getJsonHttpRequest(context, url, headers, data, true, callback);
    }

    public void postUploadFilesRequest(final Context context, final String url, final String method,
                                       final HashMap<String, String> headers,
                                       final HashMap<String, Object> fileMap,
                                       final boolean callBackOnUiThread,
                                       final long filesMaxLenth,
                                       final UploadFilesCallback uploadFilesCallback) {
        httpAPI.doUploadFilesRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_POST.getMethod(), headers, fileMap, callBackOnUiThread,
                filesMaxLenth, uploadFilesCallback);
    }

    public void postUploadFilesRequest(final Context context, final String url, final String method,
                                       final HashMap<String, String> headers,
                                       final HashMap<String, Object> fileMap,
                                       final long filesMaxLenth,
                                       final UploadFilesCallback uploadFilesCallback) {
        httpAPI.doUploadFilesRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_POST.getMethod(), headers, fileMap, true,
                filesMaxLenth, uploadFilesCallback);
    }

    public void postDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                          final JSONObject jsonObject, final String destinationFilePath,
                                          final String fileName, final long offsetBytes,
                                          final boolean callBackOnUiThread, final DownloadFileCallback
                                                  downloadFileCallback) {
        httpAPI.doDownloadFileRequest(context, url,BaseHttpAPI.HttpMethodEnum.HTTP_METHOD_POST.getMethod(), headers, jsonObject, destinationFilePath, fileName, offsetBytes,
                callBackOnUiThread, downloadFileCallback);
    }

    public void postDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                          final JSONObject jsonObject, final String destinationFilePath,
                                          final String fileName, final long offsetBytes,
                                          final DownloadFileCallback
                                                  downloadFileCallback) {
        postDownloadFileRequest(context, url, headers, jsonObject, destinationFilePath, fileName, offsetBytes,
                true, downloadFileCallback);
    }
}