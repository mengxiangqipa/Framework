//package com.framework2.okhttp3_bak;
//
//import android.os.Handler;
//import android.os.Looper;
//
//import com.framework.utils.Y;
//
//import java.io.IOException;
//
//import okhttp3.Call;
//import okhttp3.Response;
//
///**
// * @author YobertJomi
// *         className UICallback
// *         created at  2017/4/6  17:14
// */
//
//public abstract class UICallback implements okhttp3.Callback {
//    private Handler handler;
//
//    public UICallback() {
//        handler = new Handler(Looper.getMainLooper());
//    }
//
//    public abstract void onResponseMainThread(Call call, String result) throws IOException;
//
//    public abstract void onFailureMainThread(Call call, IOException e);
//
//    @Override
//    public void onFailure(final Call call, final IOException e) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                onFailureMainThread(call, e);
//            }
//        });
//    }
//
//    @Override
//    public void onResponse(final Call call, final Response reponse) throws IOException {
//        final String result = reponse.body().string();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Y.y("网络返回结果："+result);
//                    onResponseMainThread(call, result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//}
