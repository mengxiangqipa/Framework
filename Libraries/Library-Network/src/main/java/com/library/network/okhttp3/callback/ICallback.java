package com.library.network.okhttp3.callback;

public interface ICallback {
    void onSuccess(String string);

    void onFail(int errCode, Exception e);
}
