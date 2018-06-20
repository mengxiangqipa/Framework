package com.library.network.okhttp3.api;

public class HttpAPIFactory {
    private HttpAPIFactory() {
    }

    public static final BaseHttpAPI creatHttpAPI() {
        return new BaseHttpApiImpl();
    }
}
