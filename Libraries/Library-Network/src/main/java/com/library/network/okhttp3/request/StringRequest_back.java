//package com.library.network.okhttp3.request;
//
//import android.text.TextUtils;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.Map;
//
//import okhttp3.Callback;
//import okhttp3.FormBody;
//import okhttp3.MediaType;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//
///**
// * 封装ok3的string请求
// *
// * @author Yobert Jomi
// * className StringRequest
// * created at  2016/10/17  16:02
// */
//
//public class StringRequest_back {
//    public static volatile StringRequest_back.Builder builder;
//    private Request request;
//    private Callback callBack;
//
//    public static StringRequest_back.Builder getBuilder(boolean forceBuild) {
//        if (forceBuild)
//            builder = null;
//        if (builder == null) {
//            synchronized (StringRequest_back.class) {
//                if (builder == null) {
//                    builder = new StringRequest_back.Builder();
//                }
//            }
//        }
//        return builder;
//    }
//
//    public static StringRequest_back.Builder getBuilder(boolean forceBuild, Map<String, String> headers) {
//        if (forceBuild)
//            builder = null;
//        if (builder == null) {
//            synchronized (StringRequest_back.class) {
//                if (builder == null) {
//                    builder = new StringRequest_back.Builder()
//                            .addHeaders(headers)
//                            .addHeader("accept", "application/json")
//                            .addHeader("content-type", "application/json");
//                }
//            }
//        }
//        return builder;
//    }
//
//    public StringRequest_back(Request request, Callback callBack) {
//        this.request = request;
//        this.callBack = callBack;
//    }
//
//    public Request getRequest() {
//        return request;
//    }
//
//    public Callback getCallBack() {
//        return callBack;
//    }
//
//    public static class Builder {
//        public String baseUrl;
//        public String params;
//        Request.Builder requstBuilder = new Request.Builder();
//
//        public Builder addHeader(String name, String value) {
//            requstBuilder.addHeader(name, value);
//            return this;
//        }
//
//        public Builder addHeaders(Map<String, String> headers) {
//            if (null != headers) {
//                for (String key : headers.keySet()) {
//                    try {
//                        requstBuilder.addHeader(key, headers.get(key));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return this;
//        }
//
//        public Builder url(String baseUrl) {
//            if (!TextUtils.isEmpty(baseUrl)) {
//                this.baseUrl = baseUrl;
//                requstBuilder.url(baseUrl);
//            }
//            return this;
//        }
//
//        /**
//         * 如果第一步的传递的url包含完整的路径就调用这个
//         */
//        public Builder get() {
//            requstBuilder.get();
//            return this;
//        }
//
//        public Builder get(JSONObject jsonObject) {
//            if (null != jsonObject) {
//                this.params = jsonObject.toString();
//                JSONArray names = jsonObject.names();
//                if (names != null && names.length() > 0) {
//                    int lenth = names.length();
//                    for (int i = 0; i < lenth; i++) {
//                        String key = names.optString(i);
//                        if (i == 0) {
//                            baseUrl += "?";
//                        } else {
//                            baseUrl += "&";
//                        }
//                        baseUrl += (key + "=" + jsonObject.optString(key));
//                    }
//                }
//            }
//            requstBuilder.url(baseUrl);
//            requstBuilder.get();
//            return this;
//        }
//
//        public Builder delete() {
//            requstBuilder.delete();
//            return this;
//        }
//
//        public Builder put_json(String json) {
//            if (!TextUtils.isEmpty(json)) {
//                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//                requstBuilder.put(body);
//            }
//            return this;
//        }
//
//        public Builder put_x_www(String json) {
//            if (!TextUtils.isEmpty(json)) {
//                this.params = json;
//                RequestBody body = RequestBody.create(
//                        MediaType.parse("application/x-www-form-urlencoded"),
//                        json);
//                requstBuilder.put(body);
//            }
//            return this;
//        }
//
//        public Builder postString_json(String json) {
//            if (!TextUtils.isEmpty(json)) {
//                this.params = json;
//                RequestBody body = RequestBody.create(
//                        MediaType.parse("application/json; charset=utf-8"),
//                        json);
//                requstBuilder.post(body);
//            }
//            return this;
//        }
//
//        public Builder postString_x_www(String json) {
//            if (!TextUtils.isEmpty(json)) {
//                this.params = json;
//                RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), json);
//                requstBuilder.post(body);
//            }
//            return this;
//        }
//
//        /**
//         * 文件上传时使用
//         */
//        public Builder postRequestBody(RequestBody requestBody, String json) {
//            if (null != requestBody) {
//                this.params = json;
//                requstBuilder.post(requestBody);
//            }
//            return this;
//        }
//
//        public Builder postRequestBody(RequestBody requestBody) {
//            return postRequestBody(requestBody, null);
//        }
//
//        public Builder postFormBody(FormBody formBody) {
//            if (null != formBody) {
//                requstBuilder.post(formBody);
//            }
//            return this;
//        }
//
//        public Builder post(Map<String, String> mapParams) {
//            FormBody.Builder builder = new FormBody.Builder();
//            if (null != mapParams) {
//                for (String key : mapParams.keySet()) {
//                    try {
//                        builder.add(key, mapParams.get(key));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            FormBody formBody = builder.build();
//            requstBuilder.post(formBody);
//            return this;
//        }
//
//        public StringRequest_back build(Callback callBack) {
//            Request request = requstBuilder.build();
//            return new StringRequest_back(request, callBack);
//        }
//    }
//}
