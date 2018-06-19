package com.library.network.okhttp3.request;

import android.text.TextUtils;

import com.library.network.okhttp3.callback.HttpCallback;
import com.library.network.okhttp3.callback.ICallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * StringRequest
 *
 * @author YobertJomi
 * className StringRequest
 * created at  2018/6/19  14:49
 */
@SuppressWarnings("unused")
public final class StringRequest extends HttpCallback {
    private static StringRequest.Builder builder;
    private ICallback callback;
    private Request request;
    private String baseUrl;
    private String params;
    private boolean callBackOnUiThread;
    private boolean isReturnBody;

    public StringRequest(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.params = builder.params;
        this.callBackOnUiThread = builder.callBackOnUiThread;
        this.isReturnBody = builder.isReturnBody;
        this.callback = builder.callback;
        this.request = builder.request;
    }

    public StringRequest.Builder newBuilder() {
        return new StringRequest.Builder(this);
    }

    public Request getRequest() {
        return request;
    }

    public ICallback getCallBack() {
        return callback;
    }

    public String getRequestType() {
        return "STRING";
    }

    @Override
    public void onSuccess(Call call, ResponseBody reponseBody) {
        if (builder != null && builder.isReturnBody) {

        }
    }

    @Override
    public void onSuccess(Call call, String string) {
        if (callback != null) {
            callback.onSuccess(string);
        }
    }

    @Override
    public void onFail(Call call, Exception e) {
        if (callback != null) {
            callback.onFail(0, e);
        }
    }

    @Override
    public void onCancel() {

    }

    public static class Builder {
        String baseUrl;
        String params;
        boolean callBackOnUiThread;
        boolean isReturnBody;
        ICallback callback;
        Request request;
        Request.Builder requstBuilder = new Request.Builder();

        public Builder() {
            this.callBackOnUiThread = false;
            this.isReturnBody = false;
        }

        Builder(StringRequest request) {
            this.baseUrl = request.baseUrl;
            this.params = request.params;
            this.callBackOnUiThread = request.callBackOnUiThread;
            this.isReturnBody = request.isReturnBody;
        }

        public Builder callBackOnUiThread(boolean callBackOnUiThread) {
            this.callBackOnUiThread = callBackOnUiThread;
            return this;
        }

        public Builder isReturnBody(boolean isReturnBody) {
            this.isReturnBody = isReturnBody;
            return this;
        }

        public Builder header(String name, String value) {
            requstBuilder.header(name, value);
            return this;
        }

        public Builder addHeader(String name, String value) {
            requstBuilder.addHeader(name, value);
            return this;
        }

        public Builder addHeaders(Map<String, String> headers) {
            addHeader("accept", "application/json");
            addHeader("content-type", "application/json");
            if (null != headers) {
                for (String key : headers.keySet()) {
                    try {
                        requstBuilder.addHeader(key, headers.get(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return this;
        }

        public Builder url(String baseUrl) {
            if (!TextUtils.isEmpty(baseUrl)) {
                this.baseUrl = baseUrl;
                requstBuilder.url(baseUrl);
            }
            return this;
        }

        /**
         * 如果第一步的传递的url包含完整的路径就调用这个
         */
        public Builder get() {
            requstBuilder.get();
            return this;
        }

        public Builder get(JSONObject jsonObject) {
            if (null != jsonObject) {
                this.params = jsonObject.toString();
                JSONArray names = jsonObject.names();
                if (names != null && names.length() > 0) {
                    int lenth = names.length();
                    for (int i = 0; i < lenth; i++) {
                        String key = names.optString(i);
                        if (i == 0) {
                            baseUrl += "?";
                        } else {
                            baseUrl += "&";
                        }
                        baseUrl += (key + "=" + jsonObject.optString(key));
                    }
                }
            }
            requstBuilder.url(baseUrl);
            requstBuilder.get();
            return this;
        }

        public Builder delete() {
            requstBuilder.delete();
            return this;
        }

        public Builder put_json(String json) {
            if (!TextUtils.isEmpty(json)) {
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                requstBuilder.put(body);
            }
            return this;
        }

        public Builder put_x_www(String json) {
            if (!TextUtils.isEmpty(json)) {
                this.params = json;
                RequestBody body = RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded"),
                        json);
                requstBuilder.put(body);
            }
            return this;
        }

        public Builder postString_json(String json) {
            if (!TextUtils.isEmpty(json)) {
                this.params = json;
                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json; charset=utf-8"),
                        json);
                requstBuilder.post(body);
            }
            return this;
        }

        public Builder postString_x_www(String json) {
            if (!TextUtils.isEmpty(json)) {
                this.params = json;
                RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), json);
                requstBuilder.post(body);
            }
            return this;
        }

        /**
         * 文件上传时使用
         */
        public Builder postRequestBody(RequestBody requestBody, String json) {
            if (null != requestBody) {
                this.params = json;
                requstBuilder.post(requestBody);
            }
            return this;
        }

        public Builder postRequestBody(RequestBody requestBody) {
            return postRequestBody(requestBody, null);
        }

        public Builder postFormBody(FormBody formBody) {
            if (null != formBody) {
                requstBuilder.post(formBody);
            }
            return this;
        }

        public Builder post(Map<String, String> mapParams) {
            FormBody.Builder builder = new FormBody.Builder();
            if (null != mapParams) {
                for (String key : mapParams.keySet()) {
                    try {
                        builder.add(key, mapParams.get(key));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            FormBody formBody = builder.build();
            requstBuilder.post(formBody);
            return this;
        }

        public StringRequest build(ICallback callBack) {
            Builder.this.callback = callBack;
            Builder.this.request = requstBuilder.build();
            StringRequest stringRequest = new StringRequest(this);
            stringRequest.setCallBackOnUiThread(callBackOnUiThread);
            stringRequest.setReturnBody(isReturnBody);
            builder = this;
            return stringRequest;
        }
    }
}
