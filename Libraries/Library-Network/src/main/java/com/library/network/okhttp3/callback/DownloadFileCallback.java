package com.library.network.okhttp3.callback;

import com.library.network.okhttp3.upload_dowload.ProgressResponseBody;

/**
 * 文件下载回调方法
 */
public interface DownloadFileCallback extends ProgressResponseBody.ProgressListener {
    void onSuccess(String string);

    void onFail(int errno, Exception e);
}
