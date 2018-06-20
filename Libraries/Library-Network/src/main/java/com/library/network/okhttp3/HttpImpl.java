package com.library.network.okhttp3;

import android.content.Context;
import android.text.TextUtils;

import com.framework.utils.NetworkUtil;
import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.ICallback;
import com.library.network.okhttp3.callback.UploadFilesCallback;
import com.library.network.okhttp3.other.ResultCode;
import com.library.network.okhttp3.request.DownloadRequest;
import com.library.network.okhttp3.request.StringRequest;
import com.library.network.okhttp3.request.UploadFileRequest;
import com.library.network.okhttp3.upload_dowload.ProgressRequestBody;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * HttpImpl-->okhttp3的简单封装实现
 *
 * @author YobertJomi
 * className HttpImpl
 * created at  2018/6/20  0:28
 */
final class HttpImpl {

    private AtomicInteger autoTryCount = new AtomicInteger(0);

    /**
     * 简单封装结合业务通用post请求
     *
     * @param context  Context
     * @param url      String
     * @param data     JSONObject
     * @param callback ICallback
     */
    protected void doPostStringRequest(final Context context, final String url,
                                     final JSONObject data, final ICallback callback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(context)) {
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
            StringRequest stringRequest = new StringRequest.Builder()
                    .url(url)
                    .isReturnBody(false)
                    .callBackOnUiThread(true)
                    .postString_json(data == null ? "" : data.toString())
                    .build(callback);
            Ok3Util.getInstance().addToRequestQueueAsynchoronous(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param context             Context
     * @param filePaths           String[]
     * @param url                 String
     * @param jsonObject          JSONObject
     * @param uploadFilesCallback UploadFilesCallback
     */
    protected void doPostUploadFilesRequest(Context context, String[] filePaths, String[] addFormDataPartNames,
                                          String url, long filesMaxLenth, JSONObject jsonObject,
                                          final UploadFilesCallback uploadFilesCallback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(context)) {
            autoTryCount.set(0);
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_NETWORK_NONE, new Exception("网络未连接，请检查你的网络"));
            }
            return;
        }
        if (TextUtils.isEmpty(url)) {
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_PARAMS, new Exception("参数异常"));
            }
            return;
        }
        if (filePaths == null) {
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_UPLOAD_FILE_DIR, new Exception("文件路径异常"));
            }
            return;
        }
        if (addFormDataPartNames == null || (addFormDataPartNames.length != filePaths.length)) {
            //保证了 filePaths 与 addFormDataPartNames 不为空，且长度相等
            if (null != uploadFilesCallback) {
                uploadFilesCallback.onFail(ResultCode.ERROR_PARAMS, new Exception("参数异常"));
            }
            return;
        }
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        if (null != jsonObject && null != jsonObject.names()) {
            JSONArray jsonArray = jsonObject.names();
            for (int i = 0; i < jsonArray.length(); i++) {
                String key = jsonArray.optString(i);
                bodyBuilder.addFormDataPart(key, jsonObject.optString(key));
            }
        }
        long upload_filesize_limit = filesMaxLenth;
        long totalFileBytes = 0;
        if (upload_filesize_limit > 0) {
            for (int i = 0; i < filePaths.length; i++) {
                File file = new File(filePaths[i]);
                if (file.isFile() && file.length() >= 50 * 1024) {// 这里过滤<500kb的
                    totalFileBytes += file.length();
                }
                if (totalFileBytes > upload_filesize_limit) {
                    if (null != uploadFilesCallback) {
                        uploadFilesCallback.onFail(ResultCode.ERROR_UPLOAD_FILE_LIMIT, new Exception("文件大小超过"
                                + upload_filesize_limit / 1024 / 1024 + "MB"));
                    }
                    return;
                }
            }
        }
        for (int i = 0; i < filePaths.length; i++) {
            File file = new File(filePaths[i]);
            bodyBuilder.addFormDataPart(
                    TextUtils.isEmpty(addFormDataPartNames[i]) ? "file" + (i + 1) : addFormDataPartNames[i],
                    file.getName(),
                    RequestBody.create(
                            MediaType.parse("application/octet-stream"), file));
        }
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(
                bodyBuilder.build(), uploadFilesCallback);
        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest.Builder()
                    .url(url)
                    .isReturnBody(true)
                    .callBackOnUiThread(false)
                    .postRequestBody(progressRequestBody,
                            null == jsonObject ? null : jsonObject.toString())
                    .build(uploadFilesCallback);
            Ok3Util.getInstance().addToRequestQueueAsynchoronous(uploadFileRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    protected void doPostDownloadFileRequest(Context context, String url, JSONObject jsonObject,
                                           String destinationFilePath, String fileName, long offsetBytes,
                                           final DownloadFileCallback downloadFileCallback) {
        if (NetworkUtil.getInstance().isNetworkAvailable(context)) {
            autoTryCount.set(0);
            if (null != downloadFileCallback) {
                downloadFileCallback.onFail(ResultCode.ERROR_NETWORK_NONE, new Exception("网络未连接，请检查你的网络"));
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
            if (null != downloadFileCallback)
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILEPATH_DESTINATION, new Exception("存储文件路径为空"));
            return;
        }
        if (TextUtils.isEmpty(fileName)) {
            if (null != downloadFileCallback)
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILE_NAME, new Exception("存储文件名为空"));
            return;
        }
        File dirFile = new File(destinationFilePath);
        try {
            dirFile.mkdirs();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (!dirFile.exists()) {
            if (null != downloadFileCallback)
                downloadFileCallback.onFail(ResultCode.ERROR_DOWNLOAD_FILEPATH_DESTINATION, new Exception("目标文件夹不存在"));
            return;
        }
        DownloadRequest downloadRequest = new DownloadRequest.Builder()
                .url(url)
                .isReturnBody(true)
                .callBackOnUiThread(false)
                .postString_json(null == jsonObject ? null : jsonObject.toString())
                .header("RANGE", "bytes=" + offsetBytes + "-")// 断点续传要用到的，指示下载的区间
                .build(downloadFileCallback);
        downloadRequest.setDestinationFile(destinationFilePath);
        downloadRequest.setOffsetBytes(offsetBytes);
        Ok3Util.getInstance().addToRequestQueueAsynchoronous(downloadRequest);
    }
}
