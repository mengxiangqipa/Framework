package com.library.network.okhttp3.callback;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author YobertJomi
 * className HttpCallback
 * created at  2017/4/6  17:14
 */
@SuppressWarnings("unused")
public abstract class AbstractCallback implements Callback {

    private Handler handler;
    private boolean callBackOnUiThread;
    private boolean returnBody = false;

    private String tag;
    private String action;

    public abstract void onSuccess(Call call, ResponseBody reponseBody);//子线程返回

    public abstract void onSuccess(Call call, String string);//主线程返回/子线程返回

    public abstract void onFail(Call call, Exception e);

    public abstract void onCancel();

    public abstract Request getRequest();

    public abstract String getRequestType();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 设置结果返回到主线程
     *
     * @param callBackOnUiThread callBackOnUiThread
     */
    public void setCallBackOnUiThread(boolean callBackOnUiThread) {
        this.callBackOnUiThread = callBackOnUiThread;
        if (callBackOnUiThread) {
            handler = new Handler(Looper.getMainLooper());
        }
    }

    public boolean isReturnBody() {
        return returnBody;
    }

    /**
     * @param returnBody 网络返回是否是 reponse.body 下载文件时使用
     */
    public void setReturnBody(boolean returnBody) {
        this.returnBody = returnBody;
    }

    @Override
    public void onFailure(final Call call, final IOException e) {
        if (callBackOnUiThread) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onFail(call, e);
                }
            });
        } else {
            onFail(call, e);
        }
    }

    @Override
    public void onResponse(final Call call, final Response reponse) throws IOException {
        try {
            if (callBackOnUiThread&&!isReturnBody()) {
                final String result = reponse.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onSuccess(call, result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else if (!isReturnBody()) {
                try {
                    final String result = reponse.body().string();
                    onSuccess(call, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                onSuccess(call, reponse.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
