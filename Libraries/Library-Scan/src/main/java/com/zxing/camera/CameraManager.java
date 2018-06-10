/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.List;


/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 */
public final class CameraManager {

    private static final String TAG = CameraManager.class.getSimpleName();

    private static int MIN_FRAME_WIDTH = 240;
    private static int MIN_FRAME_HEIGHT = 240;
    private static int MAX_FRAME_WIDTH = 480;
    private static int MAX_FRAME_HEIGHT = 360;
    private static int FRAME_WIDTH = 0;
    private static int FRAME_HEIGHT = 0;
    private static int VIEWFINDER_SCREEN_WIDTH = 0;
    private static int VIEWFINDER_SCREEN_HEIGHT = 0;

    private static boolean OrientationPotrait = true;//竖屏
    private static boolean DecodeMultiple = false;//解析多个码

    public static int getMaxFrameHeight() {
        return MAX_FRAME_HEIGHT;
    }

    public static int getMinFrameWidth() {
        return MIN_FRAME_WIDTH;
    }

    public static void setMinFrameWidth(int minFrameWidth) {
        MIN_FRAME_WIDTH = minFrameWidth;
        if (MIN_FRAME_WIDTH > MAX_FRAME_WIDTH)
            MAX_FRAME_WIDTH = MIN_FRAME_WIDTH;
    }

    public static int getMinFrameHeight() {
        return MIN_FRAME_HEIGHT;
    }

    public static void setMinFrameHeight(int minFrameHeight) {
        MIN_FRAME_HEIGHT = minFrameHeight;
        if (MIN_FRAME_HEIGHT > MAX_FRAME_HEIGHT)
            MAX_FRAME_HEIGHT = MIN_FRAME_HEIGHT;
    }

    public static int getFrameWidth() {
        return FRAME_WIDTH;
    }

    public static void setFrameWidth(int frameWidth) {
        FRAME_WIDTH = frameWidth;
    }

    public static int getFrameHeight() {
        return FRAME_HEIGHT;
    }

    public static void setFrameHeight(int framHeight) {
        FRAME_HEIGHT = framHeight;
    }

    public static int getMaxFrameWidth() {
        return MAX_FRAME_WIDTH;
    }

    public static void setMaxFrameWidth(@IntRange(from = 50, to = 1000) int maxFrameWidth) {
        MAX_FRAME_WIDTH = maxFrameWidth;
    }

    public static void setMaxFrameHeight(@IntRange(from = 50, to = 1000) int maxFrameHeight) {
        MAX_FRAME_HEIGHT = maxFrameHeight;
    }

    public static void setOrientationPotrait(boolean portrait) {
        OrientationPotrait = portrait;
    }

    public static boolean isOrientationPotrait() {
        return OrientationPotrait;
    }

    public static int getViewfinderScreenWidth() {
        return VIEWFINDER_SCREEN_WIDTH;
    }
    public static boolean isDecodeMultiple() {
        return DecodeMultiple;
    }

    public static void setDecodeMultiple(boolean decodeMultiple) {
        DecodeMultiple = decodeMultiple;
    }
    /**
     * 设置屏幕取景的宽高//设置屏幕取景面积越大（最后会根据宽高使用系统推荐值），扫描成功概率越大，单次解析速度越慢
     * @param viewfinderScreenWidth 设置屏幕取景的宽
     */
    public static void setViewfinderScreenWidth(int viewfinderScreenWidth) {
        VIEWFINDER_SCREEN_WIDTH = viewfinderScreenWidth;
    }

    public static int getViewfinderScreenHeight() {
        return VIEWFINDER_SCREEN_HEIGHT;
    }
    /**
     * 设置屏幕取景的宽高//设置屏幕取景面积越大（最后会根据宽高使用系统推荐值），扫描成功概率越大，单次解析速度越慢
     * @param viewfinderScreenHeight 设置屏幕取景的高
     */
    public static void setViewfinderScreenHeight(int viewfinderScreenHeight) {
        VIEWFINDER_SCREEN_HEIGHT = viewfinderScreenHeight;
    }

    private static CameraManager cameraManager;

    static final int SDK_INT; // Later we can use Build.VERSION.SDK_INT

    static {
        int sdkInt;
        try {
            sdkInt = Integer.parseInt(Build.VERSION.SDK);
        } catch (NumberFormatException nfe) {
            // Just to be safe
            sdkInt = 10000;
        }
        SDK_INT = sdkInt;
    }

    private final Context context;
    private final Display display;
    private ViewfinderView viewfinderView;
    private final CameraConfigurationManager configManager;
    private Camera camera;
    private Rect framingRect;
    private Rect framingRectInPreview;
    private boolean initialized;
    private boolean previewing;
    private final boolean useOneShotPreviewCallback;
    /**
     * Preview frames are delivered here, which we pass on to the registered
     * handler. Make sure to clear the handler so it will only receive one
     * message.
     */
    private final PreviewCallback previewCallback;
    /**
     * Autofocus callbacks arrive here, and are dispatched to the Handler which
     * requested them.
     */
    private final AutoFocusCallback autoFocusCallback;

    /**
     * Initializes this static object with the Context of the calling Activity.
     *
     * @param context The Activity which wants to use the camera.
     */
    public static void init(Context context) {
        if (cameraManager == null) {
            cameraManager = new CameraManager(context);
        }
    }

    /**
     * Gets the CameraManager singleton instance.
     *
     * @return A reference to the CameraManager singleton.
     */
    public static CameraManager get() {
        return cameraManager;
    }

    private CameraManager(Context context) {

        this.context = context;
        this.configManager = new CameraConfigurationManager(context);

        // Camera.setOneShotPreviewCallback() has a race condition in Cupcake, so we use the older
        // Camera.setPreviewCallback() on 1.5 and earlier. For Donut and later, we need to use
        // the more efficient one shot callback, as the older one can swamp the system and cause it
        // to run out of memory. We can't use SDK_INT because it was introduced in the Donut SDK.
        //useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.CUPCAKE;
        useOneShotPreviewCallback = Integer.parseInt(Build.VERSION.SDK) > 3; // 3 = Cupcake

        previewCallback = new PreviewCallback(configManager, useOneShotPreviewCallback);
        autoFocusCallback = new AutoFocusCallback();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = manager.getDefaultDisplay();
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Opens the camera driver and initializes the hardware parameters.
     *
     * @param holder The surface object which the camera will draw preview frames into.
     * @throws IOException Indicates the camera driver failed to open.
     */
    public void openDriver(SurfaceHolder holder, @NonNull ViewfinderView viewfinderView) throws IOException {
        this.viewfinderView = viewfinderView;
        if (camera == null) {
            camera = Camera.open();
            if (camera == null) {
                throw new IOException();
            }
            camera.setPreviewDisplay(holder);

            if (!initialized) {
                initialized = true;
                configManager.initFromCameraParameters(camera, viewfinderView);
            }
            configManager.setDesiredCameraParameters(camera);

            // FIXME
            // SharedPreferences prefs =
            // PreferenceManager.getDefaultSharedPreferences(context);
            // 是否使用前灯
            // if (prefs.getBoolean(PreferencesActivity.KEY_FRONT_LIGHT, false))
            // {
            // FlashlightManager.enableFlashlight();
            // }
            FlashlightManager.enableFlashlight();
        }
    }

    public void openLigth() {
        if (camera == null) {
            return;
        }
        Parameters parameters = camera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }
        }
    }

    public void stopLigth() {

        if (camera == null) {
            System.out.println("camera==null");
            return;
        }
        System.out.println("parameters");
        Parameters parameters = camera.getParameters();
        if (parameters == null) {
            System.out.println("parameters == null");
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();

        if (flashModes == null) {
            return;
        }
//        Log.i(TAG, "Flash mode: " + flashMode);
//        Log.i(TAG, "Flash modes: " + flashModes);
        if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
            } else {
//                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
    }

    /**
     * Closes the camera driver if still in use.
     */
    public void closeDriver() {
        if (camera != null) {
            FlashlightManager.disableFlashlight();
            camera.release();
            camera = null;
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public void startPreview() {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public void stopPreview() {
        if (camera != null && previewing) {
            if (!useOneShotPreviewCallback) {
                camera.setPreviewCallback(null);
            }
            camera.stopPreview();
            previewCallback.setHandler(null, 0);
            autoFocusCallback.setHandler(null, 0);
            previewing = false;
        }
    }

    /**
     * A single preview frame will be returned to the handler supplied. The data will arrive as byte[]
     * in the message.obj field, with width and height encoded as message.arg1 and message.arg2,
     * respectively.
     *
     * @param handler The handler to send the message to.
     * @param message The what field of the message to be sent.
     */
    public void requestPreviewFrame(Handler handler, int message) {
        if (camera != null && previewing) {
            previewCallback.setHandler(handler, message);
            if (useOneShotPreviewCallback) {
                camera.setOneShotPreviewCallback(previewCallback);
            } else {
                camera.setPreviewCallback(previewCallback);
            }
        }
    }

    /**
     * Asks the camera hardware to perform an autofocus.
     *
     * @param handler The Handler to notify when the autofocus completes.
     * @param message The message to deliver.
     */
    public void requestAutoFocus(Handler handler, int message) {
        if (camera != null && previewing) {
            autoFocusCallback.setHandler(handler, message);
            //Log.d(TAG, "Requesting auto-focus callback");
            camera.autoFocus(autoFocusCallback);
        }
    }

    /**
     * 在screen上预览的框
     * @param framingRect 在screen上预览的框
     */
    public void setRectInPreview(Rect framingRect) {
        this.framingRect = framingRect;
    }
    /**
     * Calculates the framing rect which the UI should draw to show the user where to place the
     * barcode. This target helps with alignment as well as forces the user to hold the device
     * far enough away to ensure the image will be in focus.
     *
     * @return The rectangle to draw on screen in window coordinates.
     */
    public Rect getFramingRect() {
        if (framingRect == null) {
            Point screenResolution = configManager.getScreenResolution();
            if (camera == null) {
                return null;
            }
            int width;
            if (getFrameWidth() > 0) {
                width = getFrameWidth();
            } else {
                width = screenResolution.x * 3 / 4;
                if (width < MIN_FRAME_WIDTH) {
                    width = MIN_FRAME_WIDTH;
                } else if (width > MAX_FRAME_WIDTH) {
                    width = MAX_FRAME_WIDTH;
                }
            }
            int height;
            if (getFrameHeight() > 0) {
                height = getFrameHeight();
            } else {
                height = screenResolution.y * 3 / 4;
                if (height < MIN_FRAME_HEIGHT) {
                    height = MIN_FRAME_HEIGHT;
                } else if (height > MAX_FRAME_HEIGHT) {
                    height = MAX_FRAME_HEIGHT;
                }
            }
            int leftOffset;
            int topOffset;
            if (null != viewfinderView) {
                leftOffset = Math.max((viewfinderView.getWidth() - width) / 2, (screenResolution.x - width) / 2);
                topOffset = Math.max((viewfinderView.getHeight() - height) / 2, (screenResolution.y - height) / 2);
            } else {
                leftOffset = (screenResolution.x - width) / 2;
                topOffset = (screenResolution.y - height) / 2;
            }
            framingRect = new Rect(leftOffset, topOffset, viewfinderView != null ? (viewfinderView.getWidth() - leftOffset) : leftOffset + width, viewfinderView != null ? (viewfinderView.getHeight() - topOffset) : topOffset + height);
//            framingRect = new Rect(leftOffset, topOffset, leftOffset + width, topOffset + height);
//            framingRect = new Rect(360, 720, 720, 1080);
//            framingRect = new Rect(180, 720, 900, 1080);
//            framingRect = new Rect(180, 420, 900, 780);
//            framingRect = new Rect(360, 500, 720, 700);
//            framingRect = new Rect(300, 500, 780, 700);
//            Log.e("yy", "Calculated framing rect: " + framingRect);
        }
        return framingRect;
    }

    /**
     * Like {@link #getFramingRect} but coordinates are in terms of the preview frame,
     * not UI / screen.
     */

    public Rect getFramingRectInPreview() {
        if (framingRectInPreview == null) {
            Rect rect = new Rect(getFramingRect());
//            Log.e("yy", "getFramingRectInPreview:" + rect.left);
//            Log.e("yy", "getFramingRectInPreview:" + rect.top);
//            Log.e("yy", "getFramingRectInPreview:" + rect.right);
//            Log.e("yy", "getFramingRectInPreview:" + rect.bottom);
            rect.left = rect.left * (configManager.getScreenResolution().x) / viewfinderView.getWidth();
            rect.top = rect.top * (configManager.getScreenResolution().y) / viewfinderView.getHeight();
            rect.right = rect.right * (configManager.getScreenResolution().x) / viewfinderView.getWidth();
            rect.bottom = rect.bottom * (configManager.getScreenResolution().y) / viewfinderView.getHeight();
//            Log.e("yy", "getFramingRectInPreview--2:" + rect.left);
//            Log.e("yy", "getFramingRectInPreview--2:" + rect.top);
//            Log.e("yy", "getFramingRectInPreview--2:" + rect.right);
//            Log.e("yy", "getFramingRectInPreview--2:" + rect.bottom);
//            rect.left=60;
//            rect.top=249;
//            rect.right=420;
//            rect.bottom=613;
            Point cameraResolution = configManager.getCameraResolution();
            Point screenResolution = configManager.getScreenResolution();
            if (OrientationPotrait) {//我修改 。竖屏
                rect.left = rect.left * cameraResolution.y / screenResolution.x;
                rect.right = rect.right * cameraResolution.y / screenResolution.x;
                rect.top = rect.top * cameraResolution.x / screenResolution.y;
                rect.bottom = rect.bottom * cameraResolution.x / screenResolution.y;
            } else {
                rect.left = rect.left * cameraResolution.x / screenResolution.x;
                rect.right = rect.right * cameraResolution.x / screenResolution.x;
                rect.top = rect.top * cameraResolution.y / screenResolution.y;
                rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
            }
            framingRectInPreview = rect;
        }
//        Log.e("yy", "framingRectInPreview: " + framingRectInPreview);
        return framingRectInPreview;
    }

    /**
     * Converts the result points from still resolution coordinates to screen coordinates.
     *
     * @param points The points returned by the Reader subclass through Result.getResultPoints().
     * @return An array of Points scaled to the size of the framing rect and offset appropriately
     *         so they can be drawn in screen coordinates.
     */
  /*
  public Point[] convertResultPoints(ResultPoint[] points) {
    Rect frame = getFramingRectInPreview();
    int count = points.length;
    Point[] output = new Point[count];
    for (int x = 0; x < count; x++) {
      output[x] = new Point();
      output[x].x = frame.left + (int) (points[x].getX() + 0.5f);
      output[x].y = frame.top + (int) (points[x].getY() + 0.5f);
    }
    return output;
  }
   */

    /**
     * A factory method to build the appropriate LuminanceSource object based on the format
     * of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   A preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return A PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
        int previewFormat = configManager.getPreviewFormat();
        String previewFormatString = configManager.getPreviewFormatString();
        switch (previewFormat) {
            // This is the standard Android format which all devices are REQUIRED to support.
            // In theory, it's the only one we should ever care about.
            case PixelFormat.YCbCr_420_SP:
                // This format has never been seen in the wild, but is compatible as we only care
                // about the Y channel, so allow it.
            case PixelFormat.YCbCr_422_SP:
                return new PlanarYUVLuminanceSource(data, width, height, rect.left,
                        rect.top, rect.width(), rect.height());
            default:
                // The Samsung Moment incorrectly uses this variant instead of the 'sp' version.
                // Fortunately, it too has all the Y data up front, so we can read it.
                if ("yuv420p".equals(previewFormatString)) {
                    return new PlanarYUVLuminanceSource(data, width, height,
                            rect.left, rect.top, rect.width(), rect.height());
                }
        }
        throw new IllegalArgumentException("Unsupported picture format: "
                + previewFormat + '/' + previewFormatString);
    }

    public Context getContext() {
        return context;
    }

}
