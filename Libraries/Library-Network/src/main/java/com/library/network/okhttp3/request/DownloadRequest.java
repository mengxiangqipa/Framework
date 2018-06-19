package com.library.network.okhttp3.request;

import android.text.TextUtils;

import com.library.network.okhttp3.callback.DownloadFileCallback;
import com.library.network.okhttp3.callback.HttpCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * DownloadRequest-下载文件
 *
 * @author YobertJomi
 * className DownloadRequest
 * created at  2018/6/19  14:49
 */
@SuppressWarnings("unused")
public final class DownloadRequest extends HttpCallback {
    private static DownloadRequest.Builder builder;
    private DownloadFileCallback callback;
    private Request request;
    private String baseUrl;
    private String params;
    private boolean callBackOnUiThread;
    private boolean isReturnBody;
    /*===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>===>>*/
    private String destinationFile;
    private long offsetBytes;

    public DownloadRequest(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.params = builder.params;
        this.callBackOnUiThread = builder.callBackOnUiThread;
        this.isReturnBody = builder.isReturnBody;
        this.callback = builder.callback;
        this.request = builder.request;
    }

    public DownloadRequest.Builder newBuilder() {
        return new DownloadRequest.Builder(this);
    }

    public Request getRequest() {
        return request;
    }

    public String getRequestType() {
        return "DOWNLOAD";
    }

    public DownloadFileCallback getCallBack() {
        return callback;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public void setOffsetBytes(long offsetBytes) {
        this.offsetBytes = offsetBytes;
    }

    @Override
    public void onSuccess(Call call, ResponseBody reponseBody) {
        if (builder != null && builder.isReturnBody) {
            InputStream in = reponseBody.byteStream();
            FileChannel channelOut = null;
            // 随机访问文件，可以指定断点续传的起始位置
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(destinationFile,
                        "rwd");
                // Chanel
                // NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
                channelOut = randomAccessFile.getChannel();
                // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
                MappedByteBuffer mappedBuffer = channelOut.map(
                        FileChannel.MapMode.READ_WRITE, offsetBytes,
                        reponseBody.contentLength());
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    mappedBuffer.put(buffer, 0, len);
                }
                this.onSuccess(call, "下载完成");
            } catch (IOException e) {
                this.onFail(call, e);
            } finally {
                try {
                    if (null != in) {
                        in.close();
                    }
                    if (channelOut != null) {
                        channelOut.close();
                    }
                    if (randomAccessFile != null) {
                        randomAccessFile.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        boolean isReturnBody = true;
        public DownloadFileCallback callback;
        Request request;
        Request.Builder requstBuilder = new Request.Builder();

        public Builder() {
            this.callBackOnUiThread = false;
            this.isReturnBody = true;
        }

        Builder(DownloadRequest request) {
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

        public DownloadRequest build(DownloadFileCallback callBack) {
            Builder.this.callback = callBack;
            Builder.this.request = requstBuilder.build();
            DownloadRequest downloadFileCallback = new DownloadRequest(this);
            downloadFileCallback.setCallBackOnUiThread(callBackOnUiThread);
            downloadFileCallback.setReturnBody(isReturnBody);
            builder = this;
            return downloadFileCallback;
        }
    }
}
