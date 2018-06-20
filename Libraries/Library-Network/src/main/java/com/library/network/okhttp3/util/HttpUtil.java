package com.library.network.okhttp3.util;

import android.content.Context;

import com.library.network.okhttp3.api.BaseHttpAPI;
import com.library.network.okhttp3.api.HttpAPIFactory;
import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;

import org.json.JSONObject;

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

    public void doPostStringRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final boolean callBackOnUiThread, final ICallback callback) {
        httpAPI.doPostStringRequest(context, url, headers, data, callBackOnUiThread, callback);
    }

    public void doPostStringRequest(final Context context, final String url, final Map<String, String> headers,
                                    final JSONObject data, final ICallback callback) {
        doPostStringRequest(context, url, headers, data, true, callback);
    }

    public void doPostUploadFilesRequest(final Context context, final String url, final Map<String, String> headers,
                                         final JSONObject jsonObject, final boolean callBackOnUiThread,
                                         final String[] filePaths, final String[] addFormDataPartNames,
                                         final long filesMaxLenth, final UploadFilesCallback uploadFilesCallback) {
        httpAPI.doPostUploadFilesRequest(context, url, headers, jsonObject, callBackOnUiThread, filePaths,
                addFormDataPartNames, filesMaxLenth, uploadFilesCallback);
    }

    public void doPostUploadFilesRequest(final Context context, final String url, final Map<String, String> headers,
                                         final JSONObject jsonObject, final String[] filePaths,
                                         final String[] addFormDataPartNames, final long filesMaxLenth,
                                         final UploadFilesCallback uploadFilesCallback) {
        doPostUploadFilesRequest(context, url, headers, jsonObject, true, filePaths,
                addFormDataPartNames, filesMaxLenth, uploadFilesCallback);
    }

    public void doPostDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                          final JSONObject jsonObject, final String destinationFilePath,
                                          final String fileName, final long offsetBytes,
                                          final boolean callBackOnUiThread, final DownloadFileCallback
                                                  downloadFileCallback) {
        httpAPI.doPostDownloadFileRequest(context, url, headers, jsonObject, destinationFilePath, fileName, offsetBytes,
                callBackOnUiThread, downloadFileCallback);
    }

    public void doPostDownloadFileRequest(final Context context, final String url, final Map<String, String> headers,
                                          final JSONObject jsonObject, final String destinationFilePath,
                                          final String fileName, final long offsetBytes,
                                          final DownloadFileCallback
                                                  downloadFileCallback) {
        doPostDownloadFileRequest(context, url, headers, jsonObject, destinationFilePath, fileName, offsetBytes,
                true, downloadFileCallback);
    }
}