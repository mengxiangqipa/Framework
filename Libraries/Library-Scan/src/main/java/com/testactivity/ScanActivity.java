package com.testactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.framework.util.RequestPermissionsUtil;
import com.framework.util.ScreenUtil;
import com.framework.util.ToastUtil;
import com.framework.util.Y;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.library.scan.R;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

/**
 * 扫描页/销售
 *
 * @author YobertJomi
 * className ScanActivity
 * created at  2017/3/16  14:44
 */
public class ScanActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private boolean hasCameraPermission = false;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.getInstance().setTranslucentStatus(this, true);
        ScreenUtil.getInstance().setStatusBarTintColor(this,
                getResources().getColor(android.R.color.white));
        setContentView(R.layout.activity_scan);
//        initView();
        hasCameraPermission = !(RequestPermissionsUtil.getInstance().checkPermissionsThenRequest(this, new
                String[]{Manifest.permission.CAMERA}, RequestPermissionsUtil.PERMISSION_CAMERA));
        if (hasCameraPermission) {
            initParametersThenAutoFocus();
        }
    }

    private void initParametersThenAutoFocus() {
        initParameters();
        /*以下2句--自动扫描*/
        viewfinderView.setShowRandomCircle(true);//viewfinderView显示扫描随机点 //自动对焦扫描
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionsUtil.PERMISSION_CAMERA) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initParametersThenAutoFocus();
                if (null != viewfinderView)
                    viewfinderView.setShowRandomCircle(true);
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preSurfaceView);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                initCamera(surfaceHolder);
            } else {
                ToastUtil.getInstance().showToast("相机权限未开启，请手动开启");
                RequestPermissionsUtil.getInstance().showInstalledAppDetailSettingIntent(this, getPackageName());
            }
        }
    }

    /*以下QR
    扫描相关************************************************************************************************************************************/
    //扫描相关参数*********************************//
    private CaptureActivityHandler captureActivityHandler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    //扫描相关参数*********************************//

    /**
     * 扫描相关
     */
    private void initParameters() {
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinderView);
        viewfinderView.setCornerLenth(ScreenUtil.getInstance().dip2px(this, 17));
        viewfinderView.setCornerThickness(ScreenUtil.getInstance().dip2px(this, 3));
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
//        CameraManager.setMaxFrameWidth(ScreenUtils.getProxyApplication().getScreenWidthPx(this) - 2 * ScreenUtils
// .getProxyApplication().dip2px(this, 50));//设置取景框最大宽度
//        CameraManager.setMaxFrameHeight(ScreenUtils.getProxyApplication().dip2px(this, 40));//设置取景框最大高度
//        CameraManager.setViewfinderScreenWidth(CameraManager.getMaxFrameWidth());
//        CameraManager.setViewfinderScreenHeight(CameraManager.getMaxFrameHeight());

        int topOffset = (int) (ScreenUtil.getInstance().getScreenHeightPx(this) * 0.18);//TODO 0.18=ConstantsME
        // .scanMarginTopFloat
        int leftOffset = ScreenUtil.getInstance().dip2px(this, 40);
        int width =
                ScreenUtil.getInstance().getScreenWidthPx(this) - 2 * ScreenUtil.getInstance().dip2px(this, 50);
        //设置取景框最大宽度
        int height = width / 3;
        Rect framingRect = new Rect(leftOffset, topOffset, ScreenUtil.getInstance().getScreenWidthPx(this) -
                leftOffset,
                topOffset + height);
        CameraManager.setDecodeMultiple(false);
        CameraManager.get().setRectInPreview(framingRect);
        CameraManager.setFrameWidth(width);
        CameraManager.setFrameHeight(height);
        viewfinderView.postInvalidate();

//        initInputTopMargin();//改变<手动输入>topMargin
    }

    /**
     * 开始/重新 扫描并解码
     */
    private void startScan() {
        if (null != captureActivityHandler) {
            captureActivityHandler.startScan();
            viewfinderView.setShowRandomCircle(true);
        } else {
            onResume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != viewfinderView)
            viewfinderView.setShowRandomCircle(true);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preSurfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (!hasCameraPermission) {
            initParametersThenAutoFocus();
        }
        if (hasSurface) {
            if (!hasCameraPermission) {//// TODO: 2017/4/24 我添加的
                initParametersThenAutoFocus();
            }
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
//        decodeFormats = null;
//        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
        //*********************************以上为二维码
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewfinderView.setShowRandomCircle(false);
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            captureActivityHandler = null;
        }
        if (null != CameraManager.get())
            CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        if (null != inactivityTimer) {
            inactivityTimer.shutdown();
        }
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param results       result
     * @param barcodeBitmap barcode
     */
    public void handleDecode(Result[] results, Bitmap barcodeBitmap) {
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            captureActivityHandler = null;
        }
        if (results != null && results[0] != null) {
            ToastUtil.getInstance().showToast("" + results[0].getText());
            CameraManager.get().closeDriver();
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();

            if (!TextUtils.isEmpty(results[0].getText())) {
                Intent intent = getIntent();
                if (null != intent) {
                    intent.putExtra("result", results[0].getText());
                    setResult(Activity.RESULT_OK, intent);
                }
            }
        }
    }

    private boolean initCamera(SurfaceHolder surfaceHolder) {
        try {
            Y.y("initCamera:" + 1);
            CameraManager.get().openDriver(surfaceHolder, viewfinderView);
        } catch (IOException ioe) {
            Y.y("initCamera_IOException:" + ioe.getMessage());
            return false;
        } catch (RuntimeException e) {
            Y.y("initCamera_RuntimeException:" + e.getMessage());
            return false;
        }
//        if (decodeFormats == null || decodeFormats.isEmpty()) {
//            decodeFormats = new Vector<BarcodeFormat>();
//            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);//只解析条形码
//        }

        if (captureActivityHandler == null) {
            captureActivityHandler = new CaptureActivityHandler(this, decodeFormats, characterSet, true);
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getCaptureActivityHandler() {
        return captureActivityHandler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    /*以上QR
    扫描相关************************************************************************************************************************************/
}
