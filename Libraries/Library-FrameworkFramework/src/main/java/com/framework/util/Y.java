package com.framework.util;

import android.os.Environment;
import android.util.Log;

import com.framework.config.Config;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * @author Yobert Jomi
 * className Y
 * created at  2016/10/15  16:57
 */
public class Y {

    private static String prefix = "yy--:";

    /**
     * 记录日志到sdcard上
     */
    public static void writeFileToSD(String txt) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Y.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                    "AppFramework" + File.separator + "logs"
                    + File.separator;
            String fileName = "log.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                Y.d("TestFile", "Create the path:" + pathName);
                // mkdir() 只能在已经存在的目录中创建创建文件夹。
                // mkdirs() 可以在不存在的目录中创建文件夹。
                path.mkdirs();
            }
            if (!file.exists()) {
                Y.d("TestFile", "Create the file:" + fileName);
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
            Y.e("TestFile", "Error on writeFilToSD.", e);
        }
    }

    /**
     * debug
     *
     * @param tag tag
     * @param msg msg
     */
    public static void d(String tag, String msg) {
        if (Config.ENABLE_LOG) {
            Log.d(tag, prefix + msg);
        }
    }

    /**
     * debug
     *
     * @param tag tag
     * @param msg msg
     * @param tr  tr
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (Config.ENABLE_LOG) {
            Log.d(tag, prefix + msg, tr);
        }
    }

    /**
     * error
     *
     * @param tag tag
     * @param msg msg
     */
    public static void e(String tag, String msg) {
        if (Config.ENABLE_LOG) {
            Log.e(tag, prefix + msg);
        }
    }

    /**
     * error
     *
     * @param tag tag
     * @param msg msg
     * @param tr  tr
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (Config.ENABLE_LOG) {
            Log.e(tag, prefix + msg, tr);
        }
    }

    /**
     * verbose
     *
     * @param tag tag
     * @param msg msg
     */
    public static void v(String tag, String msg) {
        if (Config.ENABLE_LOG) {
            Log.v(tag, prefix + msg);
        }
    }

    /**
     * verbose
     *
     * @param tag tag
     * @param msg msg
     * @param tr  tr
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (Config.ENABLE_LOG) {
            Log.v(tag, prefix + msg, tr);
        }
    }

    public static void i(String msg) {
        if (Config.ENABLE_LOG) {
            Log.i("yy", prefix + msg);
        }
    }

    public static void i(int msg) {
        if (Config.ENABLE_LOG) {
            Log.i("yy", prefix + msg + "");
        }
    }

    public static void i(String tag, String msg) {
        if (Config.ENABLE_LOG) {
            Log.i(tag, prefix + msg);
        }
    }

    public static void y(String msg) {
        if (Config.ENABLE_LOG) {
            Log.e("yy", prefix + msg);
        }
    }

    public static void y(int msg) {
        if (Config.ENABLE_LOG) {
            Log.e("yy", prefix + msg + "");
        }
    }
}
