package com.framework.util;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

/**
 * @author YobertJomi
 * className DownLoadObserver
 * created at  2016/9/26  16:13
 * 在下载之后调用
 * getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new
 * DownLoadObserver(new
 * Handler(),VodActivity.this,id));
 * 用了这个就可以不用广播监听完成了
 */
public class DownLoadObserver extends ContentObserver {
    public static int MESSAGE_PROGRESS = 0X00ff;
    private long downId;
    private Context context;
    private Handler handler;

    public DownLoadObserver(Handler handler, Context context, long downId) {
        super(handler);
        this.handler = handler;
        this.downId = downId;
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        //每当/data/data/com.android.providers.download/database/database.db变化后，触发onCHANGE，开始具体查询
//		String name = PreferenceHelper.getStringData(DownLoadManagerUtils.DownLoad_FileName);
        super.onChange(selfChange);
        //实例化查询类，这里需要一个刚刚的downid
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downId);
        DownloadManager downloadManager =
                (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //这个就是数据库查询啦
        Cursor cursor = downloadManager.query(query);
        int mDownload_so_far = 0;
        int mDownload_all = 0;
        String path = null;
        while (cursor.moveToNext()) {
            mDownload_so_far = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager
                    .COLUMN_BYTES_DOWNLOADED_SO_FAR));
            mDownload_all =
                    cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
            path = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
        }
        int mProgress = 0;
        try {
            mProgress = mDownload_so_far * 100 / mDownload_all;
        } catch (Exception e) {
            Y.y("DownLoadObserver_error:" + e.getMessage());
        }
        if (null != path && path.startsWith("content:")) {
            cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);
            if (null == cursor) {
                return;
            }
            while (cursor.moveToNext()) {
                path = cursor.getString(cursor.getColumnIndex("_data"));
            }
            cursor.close();
        }
        //TODO：handler.sendMessage(xxxx),这样就可以更新UI了
        if (handler != null) {
            Message message = handler.obtainMessage();
            message.arg1 = mProgress;
            message.obj = path;
            message.what = MESSAGE_PROGRESS;
            handler.sendMessage(message);
        }
        cursor.close();
    }
}
