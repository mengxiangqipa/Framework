package com.library.network.okhttp3.callback;

import com.library.network.okhttp3.upload_dowload.ProgressRequestBody;

/**
 * 上传文件监听接口
 */
public interface UploadFilesCallback extends ProgressRequestBody.ProgressListener {
    void onSuccess(String string);

    void onFail(int errno, Exception e);
}

