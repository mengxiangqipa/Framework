package com.zxing.decoding;//package com.zxing.decoding;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.AssetFileDescriptor;
//import android.graphics.Bitmap;
//import android.media.AudioManager;
//import android.media.MediaPlayer;
//import android.media.MediaPlayer.OnCompletionListener;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.view.KeyEvent;
//import android.view.SurfaceHolder;
//import android.view.SurfaceHolder.Callback;
//import android.view.SurfaceView;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.Result;
//import com.yaxin.yyt.R;
//import com.zxing.camera.CameraManager;
//import com.zxing.view.ViewfinderView;
//
//import java.io.IOException;
//import java.util.Vector;
//
//
//public class BaseCaptureActivity extends Activity implements Callback {
//
//    private CaptureActivityHandler handler;
//    private ViewfinderView viewfinderView;
//    private boolean hasSurface;
//    private boolean cameraAutoFocus;//自动对焦扫描
//    private Vector<BarcodeFormat> decodeFormats;
//    private String characterSet;
//    private InactivityTimer inactivityTimer;
//    private MediaPlayer mediaPlayer;
//    private boolean playBeep;
//    private static final float BEEP_VOLUME = 0.10f;
//    private boolean vibrate;
//
//    /**
//     * Called when the activity is first created.
//     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager
//        .LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.zxing_base_scan);
//        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
//        viewfinderView.setShowRandomCircle(true);
//        cameraAutoFocus = true;
//
//
//        // init CameraManager
//        CameraManager.init(getApplication());
//        CameraManager.setFrameHeight(200);
//        CameraManager.setFrameWidth(800);
////        CameraManager.get().setRectInPreview(new Rect(0,0,0,0));
//
//
//        hasSurface = false;
//        inactivityTimer = new InactivityTimer(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//        SurfaceHolder surfaceHolder = surfaceView.getHolder();
//        if (hasSurface) {
//            initCamera(surfaceHolder,viewfinderView, cameraAutoFocus);
//        } else {
//            surfaceHolder.addCallback(this);
//            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        }
////        decodeFormats = null;
////        characterSet = null;
//
//        playBeep = true;
//        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//            playBeep = false;
//        }
//        initBeepSound();
//        vibrate = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        CameraManager.get().closeDriver();
//    }
//
//    @Override
//    protected void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder, @NonNull ViewfinderView
//    viewfinderView, boolean
//    cameraAutoFocus) {
//        try {
//            CameraManager.get().openDriver(surfaceHolder,viewfinderView);
//        } catch (IOException ioe) {
//            return;
//        } catch (RuntimeException e) {
//            return;
//        }
//        if (handler == null) {
//            //            decodeFormats  init
//            handler = new CaptureActivityHandler(this, decodeFormats,
//                    characterSet, cameraAutoFocus);
//        }
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width,
//                               int height) {
//
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder,viewfinderView, cameraAutoFocus);
//        }
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        hasSurface = false;
//
//    }
//
//    public ViewfinderView getViewfinderView() {
//        return viewfinderView;
//    }
//
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public void drawViewfinder() {
//        viewfinderView.drawViewfinder();
//
//    }
//
//    public void handleDecode(Result obj, Bitmap barcode) {
//        inactivityTimer.onActivity();
//        viewfinderView.drawResultBitmap(barcode);
//        playBeepSoundAndVibrate();
//
//        Intent result = new Intent();
//        result.putExtra("SCAN_RESULT", obj.getText().toString());
//        setResult(RESULT_OK, result);
//        finish();
//    }
//
//    private void initBeepSound() {
//        if (playBeep && mediaPlayer == null) {
//            // The volume on STREAM_SYSTEM is not adjustable, and users found it
//            // too loud,
//            // so we now play on the music stream.
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setOnCompletionListener(beepListener);
//
//            AssetFileDescriptor file = getResources().openRawResourceFd(
//                    R.raw.beep);
//            try {
//                mediaPlayer.setDataSource(file.getFileDescriptor(),
//                        file.getStartOffset(), file.getLength());
//                file.close();
//                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                mediaPlayer = null;
//            }
//        }
//    }
//
//    private static final long VIBRATE_DURATION = 200L;
//
//    private void playBeepSoundAndVibrate() {
////        if (playBeep && mediaPlayer != null) {
////            mediaPlayer.start();
////        }
////        if (vibrate) {
////            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
////            vibrator.vibrate(VIBRATE_DURATION);
////        }
//    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent result = new Intent();
//            setResult(RESULT_CANCELED, result);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    /**
//     * When the beep has finished playing, rewind to queue up another one.
//     */
//    private final OnCompletionListener beepListener = new OnCompletionListener() {
//        public void onCompletion(MediaPlayer mediaPlayer) {
//            mediaPlayer.seekTo(0);
//        }
//    };
//
//}