package com.framework.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.framework.config.Config;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Date;

/**
 * 日志收集
 *
 * @author YobertJomi className LogCollectUtil created at 2018/1/2 16:58
 */
public class LogCollectUtil {
    private static String TAG = LogCollectUtil.class.getSimpleName();
    private static String FILE_NAME = "IpuSdkLog_";

    /**
     * 记录日志到sdcard上
     */
    public static void writeFileToSD(String txt) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Y.e(TAG, "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            // String pathName = Environment.getExternalStorageDirectory()
            // .getAbsolutePath()
            // + File.separator
            // + "IpuSDK"
            // + File.separator + "logs" + File.separator;
            String pathName = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                    + File.separator
                    + "IpuSDK"
                    + File.separator
                    + "logs"
                    + File.separator;
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
                    "yyyy_MM_dd_HH_mm_ss");
            String dateTime = df.format(new Date(System.currentTimeMillis()));

            String fileName = FILE_NAME + dateTime + ".txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Y.e(TAG, "Create the path:" + pathName);
                // mkdir() 只能在已经存在的目录中创建创建文件夹。
                // mkdirs() 可以在不存在的目录中创建文件夹。
                path.mkdirs();
            } else {
                Y.e(TAG, "FOLDER-PATH:" + pathName + "   isDirectory:"
                        + path.isDirectory());
                if (path.isDirectory()) {
                    File[] files = path.listFiles();
                    if (null != files && files.length > 0) {
                        Y.e(TAG, "files.length:" + files.length);
                        // for (int i = 0; i < files.length; i++) {
                        // Y.e(TAG, "files"+"["+i+"]:"+files[i].exists());
                        // }
                        if (files[files.length - 1].exists()) {
                            try {// Log文件超过24小时就先删除再创建
                                Y.e(TAG, "name");
                                String name = files[0].getName().substring(
                                        FILE_NAME.length(),
                                        files[0].getName().length()
                                                - ".txt".length());
                                Y.e(TAG, "name:" + name);
                                Date createDate = df.parse(name);
                                Y.e(
                                        TAG,
                                        "时间差:"
                                                + (System.currentTimeMillis() - createDate
                                                .getTime()));
                                if ((System.currentTimeMillis() - createDate
                                        .getTime()) >= 24 * 60 * 60 * 1000) {
                                    for (int i = 0; i < files.length; i++) {
                                        files[i].delete();
                                    }
                                } else {
                                    file = files[files.length - 1];
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {

                    }
                }
            }
            if (!file.exists()) {
                Y.e(TAG, "Create the file:" + fileName);
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(file.length());
            raf.write(txt.getBytes());
            raf.close();
            // 注释的也是写文件..但是每次写入都会把之前的覆盖..
            /*
             * String pathName = "/sdcard/"; String fileName = "log.txt"; File
             * path = new File(pathName); File file = new File(pathName +
             * fileName); if (!path.exists()) { Log.d("TestFile",
             * "Create the path:" + pathName); path.mkdir(); } if
             * (!file.exists()) { Log.d("TestFile", "Create the file:" +
             * fileName); file.createNewFile(); } FileOutputStream stream = new
             * FileOutputStream(file); String s = context; byte[] buf =
             * s.getBytes(); stream.write(buf); stream.close();
             */
        } catch (Exception e) {
            Log.e(TAG, "Error on writeFilToSD.", e);
        }
    }

    /**
     * 在特殊位置记录log
     *
     * @param txt
     */
    public static void writeFileToSDSimple(Context context, String txt) {
        if (!Config.ENABLE_LOG)
            return;
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
                "yyyy_MM_dd HH:mm:ss:SSS");
        String dateTime = df.format(new Date(System.currentTimeMillis()));
        String start = "记录开始时间:" + dateTime + "\n" + "    当前网络类型:" + NetworkUtil.getInstance().getNetworkState
                (context) + "       ";
        String end = "\n";
        writeFileToSD(start + txt + end);
    }
}
