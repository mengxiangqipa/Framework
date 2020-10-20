package com.library.network.okhttp3.api;

import android.content.Context;
import android.text.TextUtils;

import com.framework.util.NetworkUtil;
import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;
import com.library.network.okhttp3.other.ResultCode;
import com.library.network.okhttp3.request.DownloadRequest;
import com.library.network.okhttp3.request.StringRequest;
import com.library.network.okhttp3.request.UploadFileRequest;
import com.library.network.okhttp3.upload_dowload.ProgressRequestBody;
import com.library.network.okhttp3.util.Ok3Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * HttpImpl-->okhttp3的简单封装实现
 *
 * @author YobertJomi
 * className BaseHttpApiImpl
 * created at  2018/6/20  0:28
 */
public final class BaseHttpApiImpl implements BaseHttpAPI {

    private AtomicInteger autoTryCount = new AtomicInteger(0);

    /**
     * 简单封装结合业务通用post请求
     *
     * @param context  Context
     * @param url      String
     * @param method   String  POST/GET
     * @param data     JSONObject
     * @param callback ICallback
     */
    @Override
    public void doStringHttpRequest(final Context context, final String url, final String method,
                                    final Map<String, String> headers,
                                    final String data, final boolean callBackOnUiThread,
                                    final ICallback callback) {
        if (!NetworkUtil.getInstance().isNetworkAvailable(context)) {
            autoTryCount.set(0);
            if (null != callback) {
                callback.onFail(ResultCode.ERROR_NETWORK_NONE, new Exception("网络未连接，请检查你的网络"));
            }
            return;
        }
        if (TextUtils.isEmpty(url)) {
            if (null != callback) {
                callback.onFail(ResultCode.ERROR_PARAMS, new Exception("参数异常"));
            }
            return;
        }
        try {
            StringRequest stringRequest;
            if (method.equals(HttpMethodEnum.HTTP_METHOD_GET.getMethod())) {
                stringRequest = new StringRequest.Builder()
                        .addHeaders(headers)
                        .url(url)
                        .isReturnBody(false)
                        .callBackOnUiThread(callBackOnUiThread)
                        .get(data)
                        .build(callback);
            } else {
                stringRequest = new StringRequest.Builder()
                        .addHeaders(headers)
                        .url(url)
                        .isReturnBody(false)
                        .callBackOnUiThread(callBackOnUiThread)
                        .postString_json(TextUtils.isEmpty(data) ? "" : data)
                        .build(callback);
            }
            Ok3Util.getInstance().addToRequestQueueAsynchoronous(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see #doStringHttpRequest(Context, String, String, Map, String, boolean, ICallback)
     */
    @Override
    public void doStringHttpRequest(final Context context, final String url, final String method,
                                    final Map<String, String> headers,
                                    final String data, final ICallback callback) {
        doStringHttpRequest(context, url, method, headers, data, true, callback);
    }

    @Override
    public void doJsonHttpRequest(Context context, String url, String method,
                                  Map<String, String> headers, JSONObject data,
                                  boolean callBackOnUiThread, ICallback callback) {
        doStringHttpRequest(context, url, method,
                headers, (data == null || data.length() <= 0) ? "" : data.toString(),
                callBackOnUiThread, callback);
    }

    @Override
    public void doJsonHttpRequest(Context context, String url, String method,
                                  Map<String, String> headers, JSONObject data,
                                  ICallback callback) {
        doStringHttpRequest(context, url, method,
                headers, (data == null || data.length() <= 0) ? "" : data.toString(), true,
                callback);
    }

    /**
     * 上传文件
     *
     * @param context             Context
     * @param url                 String
     * @param method              String POST/GET
     * @param headers             header
     * @param fileMap             文件
     * @param callBackOnUiThread  是否在主线程
     * @param filesMaxLenth       最大文件限制
     * @param uploadFilesCallback UploadFilesCallback
     */
    @Override
    public void doUploadFilesRequest(final Context context, final String url, final String method,
                                     final HashMap<String, String> headers,
                                     final HashMap<String, Object> fileMap,
                                     final boolean callBackOnUiThread,
                                     final long filesMaxLenth,
                                     final UploadFilesCallback uploadFilesCallback) {
        if (!NetworkUtil.getInstance().isNetworkAvailable(context)) {
            autoTryCount.set(0);
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_NETWORK_NONE, new Exception(
                        "网络未连接，请检查你的网络"));
            }
            return;
        }
        if (TextUtils.isEmpty(url)) {
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_PARAMS, new Exception("参数异常"));
            }
            return;
        }
        if (fileMap == null) {
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_UPLOAD_FILE_DO_NOT_EXIST, new Exception(
                        "文件不存在"));
            }
            return;
        }
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        long totalFileBytes = 0;
        //追加参数
        if (null != fileMap) {
            for (String key : fileMap.keySet()) {
                Object object = fileMap.get(key);
                if (object instanceof File) {
                    File file = (File) object;
                    bodyBuilder.addFormDataPart(key, file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    if (filesMaxLenth > 0) {
                        if (file.isFile() && file.length() > 1) {
                            totalFileBytes += file.length();
                        }
                    }
                } else {
                    if (null != object) {
                        bodyBuilder.addFormDataPart(key, object.toString());
                    }
                }
            }
        }

        if (filesMaxLenth > 0 && totalFileBytes > filesMaxLenth) {
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_UPLOAD_FILE_LIMIT,
                        new Exception("文件大小超过"
                                + filesMaxLenth / 1024 / 1024 + "MB"));
            }
            return;
        }

        ProgressRequestBody progressRequestBody = new ProgressRequestBody(
                bodyBuilder.build(), uploadFilesCallback);
        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest.Builder()
                    .addHeaders(headers)
                    .url(url)
                    .isReturnBody(false)
                    .callBackOnUiThread(callBackOnUiThread)
                    .postRequestBody(progressRequestBody, null)
                    .build(uploadFilesCallback);
            Ok3Util.getInstance().addToRequestQueueAsynchoronous(uploadFileRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see #doUploadFilesRequest(Context, String, String, HashMap, HashMap, boolean, long, UploadFilesCallback)
     * UploadFilesCallback)
     */
    @Override
    public void doUploadFilesRequest(final Context context, final String url, final String method,
                                     final HashMap<String, String> headers,
                                     final HashMap<String, Object> fileMap,
                                     final long filesMaxLenth,
                                     final UploadFilesCallback uploadFilesCallback) {
        doUploadFilesRequest(context, url, method, headers, fileMap, true, filesMaxLenth,
                uploadFilesCallback);
    }

    /**
     * 断点下载文件
     *
     * @param url                  url
     * @param method               String POST/GET
     * @param jsonObject           JSONObject
     * @param destinationFilePath  保存的文件路径
     * @param fileName             String
     * @param offsetBytes          断点偏移量，默认为0
     * @param downloadFileCallback DownloadFilesResponse
     */
    @Override
    public void doDownloadFileRequest(final Context context, final String url,
                                      final String method,
                                      final Map<String, String> headers,
                                      final JSONObject jsonObject,
                                      final String destinationFilePath,
                                      final String fileName, final long offsetBytes,
                                      final boolean callBackOnUiThread,
                                      final DownloadFileCallback
                                              downloadFileCallback) {
        if (!NetworkUtil.getInstance().isNetworkAvailable(context)) {
            autoTryCount.set(0);
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_NETWORK_NONE, new Exception(
                        "网络未连接，请检查你的网络"));
            }
            return;
        }
        if (TextUtils.isEmpty(url)) {
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_PARAMS, new Exception("参数异常"));
            }
            return;
        }
        if (TextUtils.isEmpty(destinationFilePath)) {
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILEPATH_DESTINATION,
                        new Exception
                                ("存储文件路径为空"));
            }
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILE_NAME, new Exception(
                        "存储文件名为空"));
            }
            return;
        }
        File dirFile = new File(destinationFilePath);
        try {
            dirFile.mkdirs();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (!dirFile.exists()) {
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILEPATH_DESTINATION,
                        new Exception
                                ("目标文件夹不存在"));
            }
            return;
        }
        DownloadRequest downloadRequest = new DownloadRequest.Builder()
                .addHeaders(headers)
                .url(url)
                .isReturnBody(true)
                .callBackOnUiThread(callBackOnUiThread)
                .postString_json(null == jsonObject ? null : jsonObject.toString())
                .header("RANGE", "bytes=" + offsetBytes + "-")// 断点续传要用到的，指示下载的区间
                .build(downloadFileCallback);
        downloadRequest.setDestinationFile(destinationFilePath);
        downloadRequest.setOffsetBytes(offsetBytes);
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(downloadRequest);
    }

    /**
     * @see #doDownloadFileRequest(Context, String, String, Map, JSONObject, String, String, long, boolean,
     * DownloadFileCallback)
     */
    @Override
    public void doDownloadFileRequest(final Context context, final String url, final String method,
                                      final Map<String, String> headers,
                                      final JSONObject jsonObject,
                                      final String destinationFilePath, final
                                      String fileName, final long offsetBytes,
                                      final DownloadFileCallback downloadFileCallback) {
        doDownloadFileRequest(context, url, method, headers, jsonObject, destinationFilePath,
                fileName, offsetBytes, true, downloadFileCallback);
    }
}
