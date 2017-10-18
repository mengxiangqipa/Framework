package com.framework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static android.graphics.PixelFormat.getPixelFormatInfo;

/**
 * Created by  作者：Administrator on 2016/5/12 0012 11:14.
 */
public class ScreenShotUtil {
  private  static  volatile ScreenShotUtil instance;

    public static ScreenShotUtil getInstance() {
        if (instance == null) {
            synchronized (ScreenShotUtil.class) {
                if (instance == null) {
                    new ScreenShotUtil();
                }
            }
        }
        return instance;
    }

    public boolean getScreenShotThenSave(String filePath, String fileName, Context context) {
        //执行adb命令，把framebuffer中内容保存到fb1文件中
        try {
            File file = null;
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(file, fileName + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            Y.y("ScreenShotUtil:" + "1");
            //读取文件中的数据
//            Runtime.getRuntime().exec("adb pull /dev/graphics/fb0 " + file.getAbsolutePath());
            try {

                Runtime.getRuntime().exec(

                        new String[]{"/system/bin/su", "-c",

                                "chmod 777 /dev/graphics/fb0" });
            } catch (IOException e) {
                e.printStackTrace();
                Y.y("runtime:" + e.getMessage());
            }
            Y.y("ScreenShotUtil:" + "2");
            Thread.sleep(5000);
            file = new File("/dev/graphics/fb0");
            test(context, file, filePath, fileName);

        } catch (IOException e) {
            e.printStackTrace();
            Y.y("ScreenShotUtil_IOException:" + e.getMessage());
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Y.y("ScreenShotUtil_InterruptedException:" + e.getMessage());
        }
        Y.y("ScreenShotUtil:" + "3");
        return true;
    }

    static DataInputStream dStream = null;
    static byte[] piex = null;
    static int[] colors = null;
    static int screenWidth;
    static int screenHeight;
    static File fbFile;

    public Bitmap getScreenShotBitmap() {
        FileInputStream buf = null;
        try {
            fbFile = new File("/dev/graphics/fb0");
            buf = new FileInputStream(fbFile);// 读取文件内容
            dStream = new DataInputStream(buf);
            dStream.readFully(piex);
            dStream.close();
            // 将rgb转为色值
            for (int i = 0; i < piex.length; i += 2) {
                colors[i / 2] = (int) 0xff000000 +
                        (int) (((piex[i + 1]) << (16)) & 0x00f80000) +
                        (int) (((piex[i + 1]) << 13) & 0x0000e000) +
                        (int) (((piex[i]) << 5) & 0x00001A00) +
                        (int) (((piex[i]) << 3) & 0x000000f8);
            }
            Bitmap yy = Bitmap.createBitmap(colors, screenWidth, screenHeight,
                    Bitmap.Config.RGB_565);
//            Utils.showToast("位图为空："+(yy==null));
            // 得到屏幕bitmap
            return yy;

        } catch (FileNotFoundException e) {
            Y.y("FileNotFoundException error " + e.getMessage());
        } catch (IOException e) {
            Y.y("IOException error " + e.getMessage());
        } catch (Exception e) {
            Y.y("Exception error " + e.getMessage());
        } finally {
            if (buf != null) {
                try {
                    buf.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void test(Context context, File file, String filePath, String fileName) {
//    获取屏幕大小：
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager WM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = WM.getDefaultDisplay();
        display.getMetrics(metrics);
        int height = metrics.heightPixels; //屏幕高
        int width = metrics.widthPixels;    //屏幕的宽

//				获取显示方式
        int pixelformat = display.getPixelFormat();
        PixelFormat localPixelFormat1 = new PixelFormat();
        getPixelFormatInfo(pixelformat, localPixelFormat1);
        int deepth = localPixelFormat1.bytesPerPixel;//位深

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        InputStream stream =file.getInputStream();
        byte[] piex = new byte[height * width * deepth];
        DataInputStream dStream = new DataInputStream(stream);
        int i = 0;
        try {
            while (dStream.read(piex, 0, height * width * deepth) != -1) {
                // 保存图片
                int[] colors = new int[height * width];
                for (int m = 0; m < piex.length; m++) {
                    if (m % 4 == 0) {
                        int r = (piex[m] & 0xFF);
                        int g = (piex[m + 1] & 0xFF);
                        int b = (piex[m + 2] & 0xFF);
                        int a = (piex[m + 3] & 0xFF);
                        colors[m / 4] = (a << 24) + (r << 16) + (g << 8) + b;
                    }
                }
                //		piex生成bitmap
                Bitmap bitmap = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);

                //		bitmap保存为png格式：
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                Y.y("savePicAfterShotScreen_1");
                FileOutputStream fos = null;
                File mfile = null;
                if (null == bitmap) {
                    return;
                }
                try {
                    Y.y("savePicAfterShotScreen_2");
                    file = new File(filePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    Y.y("savePicAfterShotScreen_3");
                    mfile = new File(file, fileName + ".jpg");
                    if (!mfile.exists()) {
                        mfile.createNewFile();
                    }
                    Y.y("savePicAfterShotScreen_4");
                    fos = new FileOutputStream(file);
                    if (null != fos) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    }
                    Y.y("savePicAfterShotScreen_5");
                } catch (FileNotFoundException e) {
                    // e.printStackTrace();
                    Y.y("savePicAfterShotScreen_6" + e.getMessage());
                } catch (IOException e) {
                    Y.y("savePicAfterShotScreen_7" + e.getMessage());
                    // e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private WindowManager mWindowManager1 = null;
    private MediaProjectionManager mMediaProjectionManager1 = null;
    private MediaProjection mMediaProjection = null;
    private VirtualDisplay mVirtualDisplay = null;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private ImageReader mImageReader = null;
    private DisplayMetrics metrics = null;
    private int mScreenDensity = 0;

    private void createVirtualEnvironment(Context mContext) {
        mMediaProjectionManager1 = (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mWindowManager1 = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowWidth = mWindowManager1.getDefaultDisplay().getWidth();
        windowHeight = mWindowManager1.getDefaultDisplay().getHeight();
        metrics = new DisplayMetrics();
        mWindowManager1.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2); //ImageFormat.RGB_565
        Y.y("prepared the virtual environment");
    }
    private void startVirtual(Context mContext){
        Y.y("startVirtual"+1);
        if (mMediaProjection != null) {
            Y.y("startVirtual"+2);
            virtualDisplay();
        } else {
            Y.y("startVirtual"+3);
            setUpMediaProjection(mContext);
            virtualDisplay();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpMediaProjection(Context mContext){
        Y.y("setUpMediaProjection"+1);
        mResultData = ((Activity)mContext).getIntent();
        Y.y("setUpMediaProjection"+2);
//        mResultCode = ((Activity)mContext).getResult();
//        Y.y("setUpMediaProjection"+3);
//        mMediaProjectionManager1 = mContext.getMediaProjectionManager();
        Y.y("setUpMediaProjection"+4);
        mMediaProjection = mMediaProjectionManager1.getMediaProjection(mResultCode, mResultData);
        Y.y("setUpMediaProjection"+5);
    }
    private int mResultCode = 0;
    private Intent mResultData = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {
        Y.y("virtualDisplay:"+1);
        if(mResultData==null){
            Y.y("virtualDisplay:"+2);
            mResultData = mMediaProjectionManager1.createScreenCaptureIntent();
        }
        if (null == mMediaProjection) {
            Y.y("virtualDisplay:"+3);
            mMediaProjection = mMediaProjectionManager1.getMediaProjection(mResultCode, mResultData);
        }
        Y.y("windowWidth:"+windowWidth);
        Y.y("windowHeight:"+windowHeight);
        Y.y("mScreenDensity:"+mScreenDensity);
        Y.y("mImageReader.getSurface()==null:"+(mImageReader.getSurface()==null));
        Y.y("mMediaProjection==null:"+(mMediaProjection==null));
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror1",
                windowWidth, windowHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        Y.y("virtual displayed");
    }

    public void startCaptureAndSave(Context mContext, String filePath, String fileName) {
        Y.y("startCaptureAndSave" + "1");
        createVirtualEnvironment(mContext);
        Y.y("startCaptureAndSave" + "2");
        startVirtual(mContext);
        Y.y("startCaptureAndSave" + "3");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Image image = null;
        try {
            image=mImageReader.acquireLatestImage();
        } catch (Exception e1) {
            try {
                image=mImageReader.acquireLatestImage();
            } catch (Exception e2) {
                e1.printStackTrace();
            }
        }
        if(null!=image){
            Y.y("startCaptureAndSave" + "4"+"image==null:"+(image==null));
            int width = image.getWidth();
            int height = image.getHeight();
            Y.y("startCaptureAndSave" + "5");
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Y.y("startCaptureAndSave" + "6");
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_4444);
            Y.y("startCaptureAndSave" + "7");
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            Y.y("startCaptureAndSave" + "8" + ("bitmap为空") + (bitmap == null));
            Y.y("image data captured");
            if (bitmap != null) {
                try {
                    File fileImage = new File(filePath);
                    if (!fileImage.exists()) {
                        fileImage.mkdirs();
                    }
                    Y.y("startCaptureAndSave" + "9");
                    fileImage = new File(filePath + File.separator + fileName + ".png");
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                        Y.y("image file created");
                    }
                    Y.y("startCaptureAndSave" + "10");
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fileImage);
                        media.setData(contentUri);
                        mContext.sendBroadcast(media);
                        Y.y("screen image saved");
                    }
                    Y.y("startCaptureAndSave" + "11");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Y.y("startCaptureAndSave_FileNotFoundException" + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Y.y("IOException_FileNotFoundException" + e.getMessage());
                }
            }
        }

    }
}
