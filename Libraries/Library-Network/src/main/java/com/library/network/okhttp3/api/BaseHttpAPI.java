package com.library.network.okhttp3.api;

import android.content.Context;

import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;

import org.json.JSONObject;

import java.util.Map;

public interface BaseHttpAPI {

    /**
     * 简单封装结合业务通用post请求
     *
     * @param context  Context
     * @param url      String
     * @param data     JSONObject
     * @param callback ICallback
     */
    void doPostStringRequest(final Context context, final String url, final Map<String, String> headers,
                             final JSONObject data, final boolean callBackOnUiThread, final ICallback callback);

    /**
     * @see #doPostStringRequest(Context, String, Map, JSONObject, boolean, ICallback)
     */
    void doPostStringRequest(final Context context, final String url, final Map<String, String> headers,
                             final JSONObject data, final ICallback callback);

    /**
     * 上传文件
     *
     * @param context             Context
     * @param filePaths           String[]
     * @param url                 String
     * @param jsonObject          JSONObject
     * @param uploadFilesCallback UploadFilesCallback
     */
    void doPostUploadFilesRequest(final Context context, final String url, final Map<String, String> headers,
                                  final JSONObject jsonObject, final boolean callBackOnUiThread,
                                  final String[] filePaths, final String[] addFormDataPartNames,
                                  final long filesMaxLenth, final UploadFilesCallback uploadFilesCallback);

    /**
     * @see #doPostUploadFilesRequest(Context, String, Map, JSONObject, boolean, String[], String[], long,
     * UploadFilesCallback)
     */
    void doPostUploadFilesRequest(final Context context, final String url, final Map<String, String> headers,
                                  final JSONObject jsonObject, final String[] filePaths,
                                  final String[] addFormDataPartNames, final long filesMaxLenth,
                                  final UploadFilesCallback uploadFilesCallback);

    /**
     * 断点下载文件
     *
     * @param url                  url
     * @param jsonObject           JSONObject
     * @param destinationFilePath  保存的文件路径
     * @param fileName             String
     * @param offsetBytes          断点偏移量，默认为0
     * @param downloadFileCallback DownloadFilesResponse
     */
    void doPostDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                   final JSONObject jsonObject, final String destinationFilePath,
                                   final String fileName, final long offsetBytes,
                                   final boolean callBackOnUiThread, final DownloadFileCallback
                                           downloadFileCallback);

    /**
     * @see #doPostDownloadFileRequest(Context, String, Map, JSONObject, String, String, long, boolean,
     * DownloadFileCallback)
     */
    void doPostDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                   final JSONObject jsonObject,
                                   final String destinationFilePath, final
                                   String fileName, final long offsetBytes, final DownloadFileCallback
                                           downloadFileCallback);
}
