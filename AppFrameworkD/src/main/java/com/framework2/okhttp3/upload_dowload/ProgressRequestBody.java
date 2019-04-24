package com.framework2.okhttp3.upload_dowload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * @author YobertJomi
 * className ProgressRequestBody
 * created at  2017/9/20  15:53
 */
public class ProgressRequestBody extends RequestBody {

    private final RequestBody mRequestBody;
    private final ProgressListener progressListener;
    public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener) {
        this.mRequestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (progressListener == null) {
            mRequestBody.writeTo(sink);
            return;
        }
//        BufferedSink progressSink = Okio.buffer(Okio.sink(((BufferedSink)writeTo1(sink)).outputStream()));
//        BufferedSink progressSink = Okio.buffer(writeTo1(sink));

        ProgressOutputStream progressOutputStream = new ProgressOutputStream(sink.outputStream(), progressListener,
                contentLength());
        BufferedSink progressSink = Okio.buffer(Okio.sink(progressOutputStream));
        mRequestBody.writeTo(progressSink);
        progressSink.flush();
    }

    private Sink writeTo1(Sink sink) {
        return new ForwardingSink(sink) {
            long totalWritedBytes = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                totalWritedBytes += byteCount;
                progressListener.update(totalWritedBytes, contentLength(), totalWritedBytes == contentLength());
            }
        };
    }

    public interface ProgressListener {
        void update(long totalWritedBytes, long lenth, boolean done);
    }
}