package com.library.network.okhttp3.other;

public interface ResultCode {
    //常规错误
    int ERROR_NORMAL = 0;
    //无网络异常
    int ERROR_NETWORK_NONE = 1000;
    //参数异常
    int ERROR_PARAMS = 1001;
    //上传文件不存在
    int ERROR_UPLOAD_FILE_DO_NOT_EXIST = 1002;
    //上传文件大小限制
    int ERROR_UPLOAD_FILE_LIMIT = 1003;
    //下载文件存储路径
    int ERROR_DOWNLOAD_FILEPATH_DESTINATION = 1004;
    //下载文件名
    int ERROR_DOWNLOAD_FILE_NAME = 1005;
}
