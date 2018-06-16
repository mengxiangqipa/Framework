package com.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.framework.security.Base64Coder;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author YobertJomi
 * className BitmapUtil
 * created at  2017/8/4  11:29
 */
public class BitmapUtil {
    private static volatile BitmapUtil singleton;

    private BitmapUtil() {
    }

    public static BitmapUtil getInstance() {
        if (singleton == null) {
            synchronized (BitmapUtil.class) {
                if (singleton == null) {
                    singleton = new BitmapUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * Get the size in bytes of a bitmap.
     *
     * @param bitmap bitmap
     * @return size in bytes
     */

    public int getBitmapSize(Bitmap bitmap) {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        // return bitmap.getByteCount();
        // }
        // Pre HC-MR1
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * 从view 得到图片
     *
     * @param view view
     * @return Bitmap
     */
    public Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    public Bitmap getBitmapScale(Bitmap bitmapSrc, float xScale, float yScale) {
        Bitmap bitmap;
        int dstWidth = bitmapSrc.getWidth();
        int dstHeight = bitmapSrc.getHeight();
        bitmap = Bitmap.createScaledBitmap(bitmapSrc, (int) (dstWidth * xScale), (int) (dstHeight * yScale), false);
        return bitmap;
    }

    public Bitmap getBitmapFromSDcard(String path) {
        Y.y("11");
        Bitmap bitmap = null;
        Y.y("12");
        if (null == path || "".equals(path)) {
            Y.y("13");
            return bitmap;
        }
        Y.y("14");
        try {
            Y.y("15");
            bitmap = BitmapFactory.decodeFile(path);
            Y.y("16");
        } catch (Exception e) {
            Y.y("17");
            return bitmap;
        }
        Y.y("18");
        return bitmap;
    }

    /**
     * 将file转成bitmap
     *
     * @param file   src文件
     * @param width
     * @param height
     * @return
     */
    public Bitmap getBitmapFromFile(File file, int width, int height) {
        Bitmap bitmap = null;
        // Y.y("getBitmapFromFile/1");
        // Y.y("flie==null:" + (file == null));
        // Y.y("flie=getAbsolutePath:" + (file.getAbsolutePath()));
        // Y.y("flie=getPath:" + (file.getPath()));
        // Y.y("file.exists():" + (file.exists()));
        if (null != file && file.exists()) {
            // Y.y("getBitmapFromFile/2");
            BitmapFactory.Options opts = null;
            // Y.y("getBitmapFromFile/3");
            if (width > 0 && height > 0) {
                // Y.y("getBitmapFromFile/4");
                opts = new BitmapFactory.Options();
                // Y.y("getBitmapFromFile/5");
                opts.inJustDecodeBounds = true;
                // Y.y("getBitmapFromFile/6");
                BitmapFactory.decodeFile(file.getPath(), opts);
                // Y.y("getBitmapFromFile/7");
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                // Y.y("minSideLength：" + minSideLength);
                // Y.y("width:" + width);
                // Y.y("height:" + height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                // Y.y("computeSampleSize:" + opts.inSampleSize);
                // Y.y("getBitmapFromFile/9");
                opts.inJustDecodeBounds = false;
                // Y.y("getBitmapFromFile/10");
                opts.inInputShareable = true;
                // Y.y("getBitmapFromFile/11");
                opts.inPurgeable = true;
                // Y.y("getBitmapFromFile/12");
            }
            try {
                // Y.y("getBitmapFromFile/start");
                bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
                // Y.y("bitmap==null:" + (bitmap == null));
            } catch (OutOfMemoryError e) {
                // Y.y("getBitmapFromFile/OutOfMemoryError:" +
                // e.getMessage());
                e.printStackTrace();
            }
        }
        // Y.y("getBitmapFromFile//null");
        // Y.y("bitmap==null" + (bitmap == null));
        return bitmap;
    }

    /**
     * 图片压缩
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private int computeSampleSize(BitmapFactory.Options options,
                                  int minSideLength, int maxNumOfPixels) {
        Y.y("computeSampleSize/1");
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        Y.y("initialSize:" + initialSize);
        Y.y("computeSampleSize/2");
        int roundedSize;
        if (initialSize <= 8) {
            Y.y("computeSampleSize/3");
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
            Y.y("computeSampleSize/4");
        } else {
            Y.y("computeSampleSize/5");
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        Y.y("computeSampleSize/6");
        return roundedSize;
    }

    private int computeInitialSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        Y.y("computeInitialSampleSize/1");
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        Y.y("computeInitialSampleSize/2");
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        Y.y("minSideLength:" + minSideLength);
        Y.y("maxNumOfPixels:" + maxNumOfPixels);
        Y.y("lowerBound:" + lowerBound);
        Y.y("upperBound:" + upperBound);
        Y.y("computeInitialSampleSize/3");
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        Y.y("computeInitialSampleSize/4");
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            Y.y("computeInitialSampleSize/5");
            return 1;
        } else if (minSideLength == -1) {
            Y.y("computeInitialSampleSize/6");
            return lowerBound;
        } else {
            Y.y("computeInitialSampleSize/7");
            return upperBound;
        }
    }

    /**
     * 将uri转成bitmap
     *
     * @param uri
     * @return
     */
    public Bitmap decodeUriAsBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            return null;
        }
        return bitmap;
    }

    /**
     * 从Android手机屏幕截图
     *
     * @param activity
     * @return 返回一个bitmap
     */
    private Bitmap getScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        Bitmap bitmapScreenShot = Bitmap.createBitmap(bitmap, 0,
                statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmapScreenShot;
    }

    /**
     * 从Android手机屏幕截图后保存
     *
     * @return 返回一个bitmap
     */
    private void savePicAfterShotScreen(Bitmap bitmap, String filePath,
                                        String fileName) {
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, fileName + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            if (null != fos) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            // e.printStackTrace();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }

    /**
     * 从Android手机屏幕截图成功后并保存
     *
     * @param activity
     * @return 返回一个bitmap
     */
    public void getScreenShotThenSave(Activity activity,
                                      String filePath, String fileName) {
        if (filePath == null) {
            return;
        }
        // if (!filePath.getParentFile().exists()) {
        // filePath.getParentFile().mkdirs();
        // }
        savePicAfterShotScreen(getScreenShot(activity), filePath, fileName);
    }

    /**
     * Android 读取Assets中图片 String path = "file:///android_asset/文件名";
     *
     * @param filePath 为文件名 ，不加"file:///android_asset/"
     * @return Bitmap
     */
    public Bitmap getBitmapFromAssetsFile(Context context,
                                          String filePath) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(filePath);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 从一个bitmap获取它转成的base64code
     *
     * @param bitmap
     * @return
     */
    public String getPicBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] by = baos.toByteArray(); // 将图片流以字符串形式存储下来
        // String pic =Base64.encodeToString(by, Base64.DEFAULT);//android自带
        String pic = Base64Coder.encodeLines(by);// 自定义base64code
        if (!TextUtils.isEmpty(pic)) {
            return pic;
        } else {
            return "";
        }
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromNetwork(String url) {
        return getBitmapFromNetwork(url, 0, 0);
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromNetwork(String url, int bitmapWidth,
                                       int bitmapHeight) {
        Y.y("getHttpBitmap1");
        URL myFileURL = null;
        Bitmap bitmap = null;
        Y.y("getHttpBitmap2");
        if (null == url || "".equals(url)) {
            return null;
        }
        try {
            myFileURL = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 获得连接
        Y.y("getHttpBitmap3");
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            Y.y("getHttpBitmap4");
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            Y.y("getHttpBitmap5");
            // 连接设置获得数据流
            // conn.setDoInput(true);
            // conn.setDoOutput(true);
            Y.y("getHttpBitmap6");
            // 不使用缓存
            conn.setUseCaches(false);
            conn.connect();
            Y.y("getHttpBitmap7");
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // byte[] buffer=new byte[1024];
            // while(is.read(buffer)!=-1){
            Y.y("getHttpBitmap8");
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // }
            Y.y("getHttpBitmap9");
            if (bitmapWidth != 0 && bitmapHeight != 0) {
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth,
                        bitmapHeight, true);
            }
            // 关闭数据流
            is.close();
            Y.y("getHttpBitmap10");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Y.y("getHttpBitmap11");
            Y.y("e:" + e.getMessage() + e.getLocalizedMessage());
            // return null;
        }
        Y.y("bitmap==null:" + (null == bitmap));
        return bitmap;
    }

    /**
     * 获取网落图片资源
     *
     * @param url
     * @return
     */
    public File getBitmapFromNetwork(String url, String path,
                                     String fileName) {
        return getBitmapFromNetwork(url, path, fileName, 0, 0);
    }

    /**
     * 获取网落图片资源
     *
     * @param url          图片url
     * @param path         保存file在path中
     * @param fileName     文件名
     * @param bitmapWidth  设置保存图片的width
     * @param bitmapHeight 设置保存图片的height
     * @return
     */
    public File getBitmapFromNetwork(String url, String path,
                                     String fileName, int bitmapWidth, int bitmapHeight) {
        Y.y("getHttpBitmap1");
        URL myFileURL = null;
        Bitmap bitmap = null;
        Y.y("getHttpBitmap2");
        if (null == url || "".equals(url) || null == path || "".equals(path)
                || null == fileName || "".equals(fileName)) {
            return null;
        }
        try {
            myFileURL = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 获得连接
        Y.y("getHttpBitmap3");
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            Y.y("getHttpBitmap4");
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            Y.y("getHttpBitmap5");
            // 连接设置获得数据流
            // conn.setDoInput(true);
            // conn.setDoOutput(true);
            Y.y("getHttpBitmap6");
            // 不使用缓存
            conn.setUseCaches(false);
            conn.connect();
            Y.y("getHttpBitmap7");
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // byte[] buffer=new byte[1024];
            // while(is.read(buffer)!=-1){
            Y.y("getHttpBitmap8");
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // }
            Y.y("getHttpBitmap9");
            if (bitmapWidth != 0 && bitmapHeight != 0) {
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmapWidth,
                        bitmapHeight, true);
            }
            // 关闭数据流
            is.close();
            Y.y("getHttpBitmap10");
        } catch (IOException e) {
            e.printStackTrace();
            Y.y("getHttpBitmap11");
            Y.y("e:" + e.getMessage() + e.getLocalizedMessage());
            // return null;
        }
        Y.y("bitmap==null:" + (null == bitmap));
        if (null == bitmap) {
            return null;
        } else {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
            f = new File(path, fileName);
            BufferedOutputStream bos;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(f));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return f;
        }
    }

    /**
     * 添加文字到图片，类似水印文字。
     */
    public Bitmap getBitmapWatermark(Context mContext, int gResId,
                                     String text, String text2) {
        Resources resources = mContext.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
        return getBitmapWatermark(mContext, bitmap, text, text2);
    }

    /**
     * 图片加水印
     *
     * @param mContext
     * @param bitmap
     * @param text     字体尺寸12sp
     * @return
     */
    public Bitmap getBitmapWatermark(Context mContext, Bitmap bitmap,
                                     String text, String text2) {
        return getBitmapWatermark(mContext, bitmap, text, text2, 18);
    }

    /**
     * 图片加水印
     *
     * @param mContext
     * @param bitmap
     * @param text
     * @param textSize 字体尺寸12sp
     * @return
     */
    public Bitmap getBitmapWatermark(Context mContext, Bitmap bitmap,
                                     String text, String text2, int textSize) {
        if (null == bitmap) {
            return null;
        }
        if (null == text || "".equals(text)) {
            text = "";
        }
        if (null == text2 || "".equals(text2)) {
            text2 = "";
        }
        Resources resources = mContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(255, 255, 255));
        // text size in pixels
        paint.setTextSize((int) (textSize));
        // paint.setTextSize((int) (14 * scale * 5));
        // text shadow
        // paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        // int x = (bitmap.getWidth() - bounds.width()) / 2;
        // int y = (bitmap.getHeight() + bounds.height()) / 2;
        // draw text to the bottom
        int x = (bitmap.getWidth() - bounds.width()) / 10 * 5;
        int y = (bitmap.getHeight() + bounds.height()) / 20 * 15;
        if (!"".equals(text)) {
            canvas.drawText(text, x, y, paint);
        } else if (!"".equals(text2)) {
            canvas.drawText(text2, x, y, paint);
        }
        if (!"".equals(text) && !"".equals(text2)) {
            int x1 = (bitmap.getWidth() - bounds.width()) / 10 * 5;
            int y1 = (bitmap.getHeight() + bounds.height()) / 20 * 17;
            canvas.drawText(text2, x1, y1, paint);
        }

        return bitmap;
    }
}


