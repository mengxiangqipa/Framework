package com.library.network.okhttp3.api;

import android.content.Context;

import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;

import org.json.JSONObject;

import java.util.Map;

/**
 * 基础请求接口封装
 */
public interface BaseHttpAPI {
    enum HttpMethodEnum {
        /**
         * HTTP POST请求方式
         */
        HTTP_METHOD_POST("POST"),
        /**
         * HTTP GET请求方式
         */
        HTTP_METHOD_GET("GET");
        String method;

        HttpMethodEnum(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }
    }

    /**
     * 简单封装结合业务通用post请求
     *
     * @param context  Context
     * @param url      String
     * @param method   String POST/GET
     * @param data     JSONObject
     * @param callback ICallback
     */
    void doStringRequest(final Context context, final String url, final String method,
                         final Map<String, String> headers,
                         final JSONObject data, final boolean callBackOnUiThread,
                         final ICallback callback);

    /**
     * @see #doStringRequest(Context, String, String, Map, JSONObject, boolean, ICallback)
     */
    void doStringRequest(final Context context, final String url, final String method,
                         final Map<String, String> headers,
                         final JSONObject data, final ICallback callback);

    /**
     * 上传文件
     *
     * @param context             Context
     * @param filePaths           String[]
     * @param url                 String
     * @param method              String POST/GET
     * @param method              String
     * @param jsonObject          JSONObject
     * @param uploadFilesCallback UploadFilesCallback
     */
    void doUploadFilesRequest(final Context context, final String url, final String method,
                              final Map<String,
                                      String> headers,
                              final JSONObject jsonObject, final boolean callBackOnUiThread,
                              final String[] filePaths, final String[] addFormDataPartNames,
                              final long filesMaxLenth,
                              final UploadFilesCallback uploadFilesCallback);

    /**
     * @see #doUploadFilesRequest(Context, String, String, Map, JSONObject, boolean, String[], String[], long,
     * UploadFilesCallback)
     */
    void doUploadFilesRequest(final Context context, final String url, final String method,
                              final Map<String,
                                      String> headers,
                              final JSONObject jsonObject, final String[] filePaths,
                              final String[] addFormDataPartNames, final long filesMaxLenth,
                              final UploadFilesCallback uploadFilesCallback);

    /**
     * 断点下载文件
     *
     * @param url                  String url
     * @param method               String POST/GET
     * @param jsonObject           JSONObject
     * @param destinationFilePath  保存的文件路径
     * @param fileName             String
     * @param offsetBytes          断点偏移量，默认为0
     * @param downloadFileCallback DownloadFilesResponse
     */
    void doDownloadFileRequest(final Context context, final String url, final String method,
                               final Map<String,
                                       String> headers,
                               final JSONObject jsonObject, final String destinationFilePath,
                               final String fileName, final long offsetBytes,
                               final boolean callBackOnUiThread, final DownloadFileCallback
                                       downloadFileCallback);

    /**
     * @see #doDownloadFileRequest(Context, String, String, Map, JSONObject, String, String, long, boolean,
     * DownloadFileCallback)
     */
    void doDownloadFileRequest(final Context context, final String url, final String method,
                               final Map<String, String> headers,
                               final JSONObject jsonObject,
                               final String destinationFilePath, final
                               String fileName, final long offsetBytes,
                               final DownloadFileCallback downloadFileCallback);
}
