package com.framework2.okhttp3.upload_dowload;

import com.framework.util.Y;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 带进度的输出流
 */
class ProgressOutputStream extends OutputStream {
    private final OutputStream stream;
    private final ProgressRequestBody.ProgressListener listener;

    private long total;
    private long totalWritten;

    ProgressOutputStream(OutputStream stream, ProgressRequestBody.ProgressListener listener,
                         long total) {
        this.stream = stream;
        this.listener = listener;
        this.total = total;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        this.stream.write(b, off, len);
        Y.y("ProgressOutputStream-write:" + off + "    " + len + "  " + b.toString() + "   " + b.hashCode());
        if (this.total < 0) {
            this.listener.update(-1, total, false);
            return;
        }
        if (len < b.length) {
            this.totalWritten += len;
        } else {
            this.totalWritten += b.length;
        }
        this.listener.update(this.totalWritten, total, total <= totalWritten);
    }

    @Override
    public void write(int b) throws IOException {
        this.stream.write(b);
        if (this.total < 0) {
            this.listener.update(-1, total, false);
            return;
        }
        this.totalWritten++;
        this.listener.update(this.totalWritten, total, total <= totalWritten);
    }

    @Override
    public void close() throws IOException {
        if (this.stream != null) {
            this.stream.close();
        }
    }

    @Override
    public void flush() throws IOException {
        if (this.stream != null) {
            this.stream.flush();
        }
    }
}
