package com.library.videoview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.library.videoview.utils.AudioManagerUtil;
import com.library.videoview.utils.ScreenBrightnessUtil;
import com.library.videoview.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author YobertJomi
 * className CustomVideoViewWithControl
 * created at  2016/9/30  17:13
 * csdn博客:http://blog.csdn.net/yissan
 * 左侧上下改变亮度 需要系统writeSetting权限，6.0后先申请
 * 哈哈
 */
public class CustomVideoViewWithUI extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer
        .OnInfoListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, View.OnTouchListener, View.OnClickListener, Animator.AnimatorListener, SeekBar
                .OnSeekBarChangeListener {
    private final int UPDATE_VIDEO_SEEKBAR = 1000;
    private String TAG = "yy";
    private Context context;
    private CustomFullScreenVideoView customFullScreenVideoView;//videoView
    private RelativeLayout videoError;//播放出错group
    private LinearLayout videoPauseBtn;//暂停group
    private LinearLayout screenSwitchBtn;//全屏切换group
    private ImageView screen_status_img;//全屏切换 全屏状态
    private LinearLayout touchStatusView;//中间快进/快退的状态view
    private LinearLayout videoControllerLayout;//底部控制栏
    private ImageView touchStatusImg;//中间快进/快退的状态ImageView
    private ProgressBar touchStatusProgressBar;//中间快进/快退的状态progressBar
    private ImageView videoPlayImg;//播放img
    private ImageView videoPauseImg;//暂停img
    private TextView touchStatusTime;//中间快进/快退的状态  --时间
    private TextView videoCurTimeText;//底部控制栏显示 当前播放时间
    private TextView videoTotalTimeText;//底部状态栏显示 总时间
    private SeekBar seekBarVideo; //底部状态栏 拖动控件
    private FrameLayout videoTitleController;//顶部栏 title控制器
    private ImageView backImg; //顶部栏 返回
    private CustomFocusTextView titleView; //顶部栏 标题
    private TextView titleViewTime; //顶部栏 标题右边的 当前系统时间
    private ProgressBar progressBar;//加载时的转圈progress

    private int screenWidth;//屏幕宽度
    private int screenHeight;//屏幕高度
    private int selfHeight;//CustomVideoViewWithUI自身高度
    private boolean firstLayout = true;//CustomVideoViewWithUI第一次调用onLayout()

    private int duration;
    private String formatTotalTime = "00:00";

    private Timer timer;//定时器
    private TimerTask timerTask;//任务
    private boolean isTimerStarted = false;
    private boolean isACTION_UP = true;//滑动时的手指松开，默认进来就是true
    private boolean onStartTrackingTouch = false;//是否是手指按在seekbar上
    private int lastProgess;//自然播放（不触摸seekbar）的最近一次进度，用于判断有触摸seekbar时的进度变化方向
    private long timeMillisACTION_UP = System.currentTimeMillis();//记录手指释放的时间，默认值为进入的时间
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    Runnable runSystemTime = new Runnable() {
        @Override
        public void run() {
            titleViewTime.setText(simpleDateFormat.format(new Date()));
        }
    };
    private float touchLastX, touchDownX;
    private float touchLastY, touchDownY;
    //定义用seekBar当前的位置，触摸快进的时候显示时间
    private int position;
    Runnable runSeekBar = new Runnable() {
        @Override
        public void run() {
            if (customFullScreenVideoView.isPlaying()) {
                seekBarVideo.setProgress(customFullScreenVideoView.getCurrentPosition());
                seekBarVideo.setSecondaryProgress(customFullScreenVideoView.getBufferPercentage() *
                        customFullScreenVideoView.getDuration() / 100);
            } else if (position == 0) {
                seekBarVideo.setProgress(0);
                seekBarVideo.setSecondaryProgress(0);
            }
        }
    };
    private int touchStep = 1000;//快进的时间，1秒
    private int touchStepVolume = 5;//上下滑动5px增减一点音量
    private int touchStepBrightness = 3;//上下滑动5px增减一点亮度
    private int touchPosition = -1;
    private boolean videoControllerShow = true;//底部状态栏的显示状态
    private boolean animation = false;
    private Direction direction = Direction.None;//手指滑动方向
    private int initVolume;//当前音量
    private int maximumVolume;//最大音量
    private int lastVolume;//调整后的音量
    private int initBrightness = -1;//当前亮度
    private int maximumBrightness;//最大亮度
    private int lastBrightness;//调整后的亮度
    private AlertDialog dialog;//修改设置的权限弹窗
    private boolean isAutomaticBrightness;//存储是否是自动亮度
    private boolean isBack = false;//本实例是否是已经存在，返回过来
    private Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VIDEO_SEEKBAR:
                    updateVideoSeekBar();
                    break;
            }
        }
    };

    public CustomVideoViewWithUI(Context context) {
        this(context, null);
    }

    public CustomVideoViewWithUI(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoViewWithUI(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();//初始化UI
        getScreenWidth();//获取屏幕宽高
        initVolumeAndBrightness();//初始化音量和亮度
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {
        final ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.videoview_with_ui, viewGroup);
        FrameLayout viewBox = (FrameLayout) view.findViewById(R.id.viewBox);//frameLayout--root
        customFullScreenVideoView = (CustomFullScreenVideoView) view.findViewById(R.id.videoView);
        videoError = (RelativeLayout) view.findViewById(R.id.videoError);
        videoPauseBtn = (LinearLayout) view.findViewById(R.id.videoPauseBtn);
        screenSwitchBtn = (LinearLayout) view.findViewById(R.id.screen_status_btn);
        screen_status_img = (ImageView) view.findViewById(R.id.screen_status_img);
        videoControllerLayout = (LinearLayout) view.findViewById(R.id.videoControllerLayout);
        touchStatusView = (LinearLayout) view.findViewById(R.id.touch_view);
        touchStatusImg = (ImageView) view.findViewById(R.id.iv_touchStatusImg);
        touchStatusTime = (TextView) view.findViewById(R.id.tv_touch_time);
        videoCurTimeText = (TextView) view.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView) view.findViewById(R.id.videoTotalTime);
        seekBarVideo = (SeekBar) view.findViewById(R.id.seekBarVideo);
        videoPlayImg = (ImageView) view.findViewById(R.id.videoPlayImg);
        videoPlayImg.setVisibility(GONE);
        videoPauseImg = (ImageView) view.findViewById(R.id.videoPauseImg);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        touchStatusProgressBar = (ProgressBar) view.findViewById(R.id.touchStatusProgressBar);
        videoTitleController = (FrameLayout) view.findViewById(R.id.videoTitleController);
        backImg = (ImageView) view.findViewById(R.id.backImg);
        titleView = (CustomFocusTextView) view.findViewById(R.id.titleView);
        titleViewTime = (TextView) view.findViewById(R.id.titleViewTime);

        videoError.setOnClickListener(this);
        backImg.setOnClickListener(this);
        videoPauseBtn.setOnClickListener(this);
        seekBarVideo.setOnSeekBarChangeListener(this);
        videoPauseBtn.setOnClickListener(this);
        screenSwitchBtn.setOnClickListener(this);
        videoPlayImg.setOnClickListener(this);
        //注册在设置或播放过程中发生错误时调用的回调函数。如果未指定回调函数，或回调函数返回false，VideoView 会通知用户发生了错误。
        customFullScreenVideoView.setOnCompletionListener(this);
        customFullScreenVideoView.setOnErrorListener(this);
        customFullScreenVideoView.setOnInfoListener(this);
        viewBox.setOnTouchListener(this);
        viewBox.setOnClickListener(this);
        showSystemTime(true);
        addView(view);
    }

    /**
     * 获取屏幕宽高
     */
    private void getScreenWidth() {

        screenWidth = ScreenUtils.getInstance().getScreenWidthPx(context);
        screenHeight = ScreenUtils.getInstance().getScreenHeightPx(context);
    }

    /**
     * 初始化音量和亮度
     */
    private void initVolumeAndBrightness() {
        initVolume = AudioManagerUtil.getInstance().getStreamCurrentVolume(context, AudioManager.STREAM_MUSIC);
        maximumVolume = AudioManagerUtil.getInstance().getStreamMaxVolume(context, AudioManager.STREAM_MUSIC);
        maximumBrightness = 255;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (firstLayout && selfHeight == 0) {
            selfHeight = getMeasuredHeight();
            firstLayout = false;
        }
    }

    private void updateVideoSeekBar() {
        if (!onStartTrackingTouch)
            updateSeekBar();
        showSystemTime(true);//显示系统时间
        if (isACTION_UP && System.currentTimeMillis() - timeMillisACTION_UP >= 4000) {
            if (!animation && videoControllerShow)
                changeVideoControllerStatus(false);
        }
    }

    /**
     * 开始timer
     */
    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    //					Log.i(TAG, "run: 在跑");
                    videoHandler.sendEmptyMessage(UPDATE_VIDEO_SEEKBAR);
                }
            };
        }
        if (null != timer && !isTimerStarted) {
            timer.schedule(timerTask, 0, 1000);
        }
        isTimerStarted = true;
    }

    /**
     * 取消timer
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        isTimerStarted = false;
    }

    /**
     * changeVideoControllerStatus
     *
     * @param showVideoController 显示还是隐藏
     */
    private void changeVideoControllerStatus(boolean showVideoController) {
        //        Log.i(TAG, "changeVideoControllerStatus: " + showVideoController);
        if (showVideoController) {
            startTimer();
        } else {
            stopTimer();
        }
        animation = true;
        float curY = videoControllerLayout.getY();
        ObjectAnimator animator = ObjectAnimator.ofFloat(videoControllerLayout, "y", curY,
                showVideoController ? curY - videoControllerLayout.getMeasuredHeight() : curY + videoControllerLayout
                        .getMeasuredHeight());
        animator.setDuration(200);

        float curTitleY = videoTitleController.getY();
        ObjectAnimator animatorTitleController = ObjectAnimator.ofFloat(videoTitleController, "y", curTitleY,
                showVideoController ? curTitleY + videoTitleController.getMeasuredHeight() : curTitleY -
                        videoTitleController.getMeasuredHeight());
        animatorTitleController.setDuration(200);
        animator.start();
        animatorTitleController.start();
        animator.addListener(this);
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        this.animation = false;
        this.videoControllerShow = !videoControllerShow;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    /**
     * 右侧显示系统时间
     */
    private void updateSeekBar() {
        seekBarVideo.post(runSeekBar);
    }

    /**
     * 右侧显示系统时间
     */
    private void showSystemTime(boolean showSystemTime) {
        if (showSystemTime) {
            titleViewTime.post(runSystemTime);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.i(TAG, "onPrepared: ");
        duration = customFullScreenVideoView.getDuration();
        int[] time = getMinuteAndSecond(duration);
        videoTotalTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d", time[0], time[1]));
        formatTotalTime = String.format(Locale.getDefault(), "%02d:%02d", time[0], time[1]);
        seekBarVideo.setMax(duration);
        progressBar.setVisibility(View.GONE);

        //        videoHandler.removeCallbacks(updateRunable);

        if (!isBack) {
            customFullScreenVideoView.start();
            videoPauseBtn.setEnabled(true);
            seekBarVideo.setEnabled(true);
            videoPauseImg.setImageResource(R.mipmap.icon_video_playing);
            startTimer();
        } else {
            isBack = false;
            customFullScreenVideoView.seekTo(seekBarVideo.getProgress());
            customFullScreenVideoView.start();
            videoPlayImg.setVisibility(View.INVISIBLE);
            videoPauseImg.setImageResource(R.mipmap.icon_video_playing);
        }
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓存，暂停播放
                progressBar.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓存完成，继续播放
                progressBar.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion: ");
        customFullScreenVideoView.seekTo(0);
        seekBarVideo.setProgress(0);
        videoPauseImg.setImageResource(R.mipmap.icon_video_start_play);
        videoPlayImg.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (!animation && videoControllerShow) {
            changeVideoControllerStatus(false);
        }
        showErrorView();
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //未播放不做处理
                //                if (!customFullScreenVideoView.isPlaying()) {
                //                    return false;
                //                }
                isACTION_UP = false;
                touchLastX = event.getRawX();
                touchLastY = event.getRawY();
                touchDownX = event.getRawX();
                touchDownY = event.getRawY();
                position = customFullScreenVideoView.getCurrentPosition();
                break;
            case MotionEvent.ACTION_MOVE:
                //未播放不做处理
                //                if (!customFullScreenVideoView.isPlaying()) {
                //                    return false;
                //                }
                isACTION_UP = false;
                float currentX = event.getRawX();
                float deltaX = currentX - touchLastX;
                float currentY = event.getRawY();
                float deltaY = currentY - touchLastY;
                if (Math.abs(deltaX) > Math.abs(deltaY) + 10 && Math.abs(deltaX) > 10 && (direction == Direction.None
                        || direction == Direction.Horizantal)) {
                    //水平move
                    showTouchViewHorizantal(currentX, deltaX);
                } else if (Math.abs(deltaX) + 10 < Math.abs(deltaY) && Math.abs(deltaY) > 10 && (direction ==
                        Direction.None || direction == Direction.Vertical)) {
                    //垂直move
                    showTouchViewVertical(currentY, deltaY,
                            (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                                    touchDownX >= screenHeight / 2)
                                    || getResources().getConfiguration().orientation == Configuration
                                    .ORIENTATION_PORTRAIT && touchDownX >= screenWidth / 2);
                }
                break;
            case MotionEvent.ACTION_UP:
                isACTION_UP = true;
                timeMillisACTION_UP = System.currentTimeMillis();
                if (touchPosition != -1) {
                    if (direction == Direction.Horizantal) {
                        customFullScreenVideoView.seekTo(touchPosition);
                    }
                    touchStatusView.setVisibility(View.GONE);
                    direction = Direction.None;
                    if (!customFullScreenVideoView.isPlaying() && touchPosition > 0) {
                        seekBarVideo.setProgress(touchPosition);
                        startPlay();
                    }
                    touchPosition = -1;
                    if (videoControllerShow) {
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 显示左右滑动时快进/倒退的view
     */
    private void showTouchViewHorizantal(float currentX, float deltaX) {
        float deltaXAbs = Math.abs(deltaX);
        if (deltaXAbs > 1) {
            direction = Direction.Horizantal;
            touchStatusView.setVisibility(View.VISIBLE);
            touchLastX = currentX;
            if (deltaX > 1) {
                position += touchStep;
                position = Math.min(position, duration);
                touchStatusImg.setImageResource(R.mipmap.ic_fast_forward_white_24dp);
            } else if (deltaX < -1) {
                position -= touchStep;
                position = Math.max(position, 0);
                touchStatusImg.setImageResource(R.mipmap.ic_fast_rewind_white_24dp);
            }
            touchPosition = position;
            touchStatusProgressBar.setMax(duration);
            touchStatusProgressBar.setProgress(position);
            int[] time = getMinuteAndSecond(position);
            touchStatusTime.setText(String.format(Locale.getDefault(), "%02d:%02d/%s", time[0], time[1],
                    formatTotalTime));
        }
    }

    /**
     * 显示左右滑动时快进/倒退的view 这个是通过手指触摸seekbar
     */
    private void showTouchViewHorizantalByPercent(int currentProgress) {
        if (currentProgress >= 0) {
            touchStatusView.setVisibility(View.VISIBLE);
            touchStatusImg.setImageResource(currentProgress >= lastProgess ? R.mipmap.ic_fast_forward_white_24dp : R
                    .mipmap.ic_fast_rewind_white_24dp);
            touchStatusProgressBar.setMax(duration);
            touchStatusProgressBar.setProgress(currentProgress);
            int[] time = getMinuteAndSecond(currentProgress);
            touchStatusTime.setText(String.format(Locale.getDefault(), "%02d:%02d/%s", time[0], time[1],
                    formatTotalTime));
        }
    }

    /**
     * 显示上下滑动时亮度/音量的view
     *
     * @param rightScreen 是否是右边屏幕
     */
    private void showTouchViewVertical(float currentY, float deltaY, boolean rightScreen) {
        touchPosition = 0;//设置为0是为了让view隐藏
        float deltaYAbs = Math.abs(deltaY);
        if (deltaYAbs > 1) {
            direction = Direction.Vertical;
            touchStatusView.setVisibility(View.VISIBLE);
            touchLastY = currentY;
            if (rightScreen) {//右边显示音量
                touchStatusImg.setImageResource(R.mipmap.icon_volume_one);
                int currentVolume = AudioManagerUtil.getInstance().getStreamCurrentVolume(context, AudioManager
                        .STREAM_MUSIC);
                if (lastVolume == 0) {
                    lastVolume = initVolume;
                }
                boolean shouldAdd = false;
                if (deltaY <= -touchStepVolume && currentVolume < lastVolume + Math.abs(deltaY) / touchStepVolume) {
                    shouldAdd = true;
                    AudioManagerUtil.getInstance().adjustStreamVolumeRAISE(context, AudioManager.STREAM_MUSIC);
                } else if (deltaY >= touchStepVolume && currentVolume > lastVolume - Math.abs(deltaY) /
                        touchStepVolume) {
                    AudioManagerUtil.getInstance().adjustStreamVolumeLOWER(context, AudioManager.STREAM_MUSIC);
                }
                lastVolume = AudioManagerUtil.getInstance().getStreamCurrentVolume(context, AudioManager.STREAM_MUSIC);
                if (lastVolume < maximumVolume && lastVolume == currentVolume && shouldAdd) {
                    AudioManagerUtil.getInstance().simulationVolumeRAISE();
                }
                if (0 == lastVolume) {
                    touchStatusImg.setImageResource(R.mipmap.icon_volume_zero);
                } else if (lastVolume > 0 && lastVolume < maximumVolume / 3) {
                    touchStatusImg.setImageResource(R.mipmap.icon_volume_one);
                } else if (lastVolume >= maximumVolume / 3 && lastVolume < maximumVolume * 2 / 3) {
                    touchStatusImg.setImageResource(R.mipmap.icon_volume_two);
                } else if (lastVolume >= maximumVolume * 2 / 3) {
                    touchStatusImg.setImageResource(R.mipmap.icon_volume);
                }
                touchStatusProgressBar.setMax(maximumVolume);
                touchStatusProgressBar.setProgress(lastVolume);
                touchStatusTime.setText(String.format(Locale.getDefault(), "%d%%", lastVolume * 100 / maximumVolume));
            } else {//左边显示亮度
                touchStatusImg.setImageResource(R.mipmap.icon_brightness);
                boolean hasSettingPermission = ScreenBrightnessUtil.getInstance().hasSettingsPermission(context);
                if (!hasSettingPermission) {
                    //无权限，先暂停播放，再弹出提示框，授权更改设置
                    if (dialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("调整音量需要修改设置权限");
                        builder.setMessage("是否设置？");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                pausePlay();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse
                                        ("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            }
                        });
                        dialog = builder.create();
                    }
                    if (!dialog.isShowing())
                        dialog.show();
                } else {//有权限读取亮度
                    int currentBrightness = ScreenBrightnessUtil.getInstance().getCurrentBrightness((Activity) context);
                    if (-1 == initBrightness) {
                        initBrightness = currentBrightness;
                        isAutomaticBrightness = ScreenBrightnessUtil.getInstance().isAutomaticScreenBrightness(
                                (Activity) context);
                    }
                    if (lastBrightness == 0) {
                        lastBrightness = initBrightness;
                    }
                    if (deltaY <= -touchStepBrightness && currentBrightness < lastBrightness + Math.abs(deltaY) /
                            touchStepBrightness) {
                        //增加亮度
                        if (ScreenBrightnessUtil.getInstance().isAutomaticScreenBrightness((Activity) context))
                            ScreenBrightnessUtil.getInstance().closeAutomaticScreenBrightness((Activity) context, true);
                        ScreenBrightnessUtil.getInstance().setScreenBritness((Activity) context, lastBrightness +
                                (int) Math.abs(deltaY) / touchStepBrightness);
                    } else if (deltaY >= -touchStepBrightness && currentBrightness > lastBrightness - Math.abs
                            (deltaY) / touchStepBrightness) {
                        //减少亮度
                        if (ScreenBrightnessUtil.getInstance().isAutomaticScreenBrightness((Activity) context))
                            ScreenBrightnessUtil.getInstance().closeAutomaticScreenBrightness((Activity) context, true);
                        ScreenBrightnessUtil.getInstance().setScreenBritness((Activity) context, lastBrightness -
                                (int) Math.abs(deltaY) / touchStepBrightness);
                    }
                    lastBrightness = ScreenBrightnessUtil.getInstance().getCurrentBrightness((Activity) context);
                    touchStatusProgressBar.setMax(maximumBrightness);
                    touchStatusProgressBar.setProgress(lastBrightness);
                    touchStatusTime.setText(String.format(Locale.getDefault(), "%d%%", lastBrightness * 100 /
                            maximumBrightness));
                }
            }
        }
    }

    /**
     * 将timeMillis转换成int[] 分，秒
     *
     * @param timeMillis 时间戳
     * @return 将timeMillis转换成int[] 分，秒
     */
    private int[] getMinuteAndSecond(int timeMillis) {
        timeMillis /= 1000;
        int[] time = new int[2];
        time[0] = timeMillis / 60;
        time[1] = timeMillis % 60;
        return time;
    }

    @Override
    public void onClick(View v) {
        timeMillisACTION_UP = System.currentTimeMillis();
        if (v.getId() == R.id.videoPlayImg) {//播放
            startPlay();
        } else if (v.getId() == R.id.videoPauseBtn) {//暂停
            if (customFullScreenVideoView.isPlaying()) {
                pausePlay();
            } else {
                startPlay();
            }
        } else if (v.getId() == R.id.viewBox) {//整个控件点击
            if (!animation)
                changeVideoControllerStatus(!videoControllerShow);
        } else if (v.getId() == R.id.screen_status_btn) {//底部栏 屏幕全屏/非全屏切换
            ((Activity) context).setRequestedOrientation(getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE
                    ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (v.getId() == R.id.videoError) {//播放出错 点击
            videoError.setVisibility(GONE);
            changeVideoControllerStatus(!videoControllerShow);
            startPlay();
        } else if (v.getId() == R.id.backImg) {//顶部栏返回
            onBackClicked();
        }
    }

    /**
     * 开始/恢复播放
     */
    private void startPlay() {
        startTimer();//启动timer
        customFullScreenVideoView.start();
        videoPlayImg.setVisibility(View.INVISIBLE);
        videoPauseImg.setImageResource(R.mipmap.icon_video_playing);
    }

    /**
     * 暂停播放
     */
    private void pausePlay() {
        stopTimer();//先取消timer
        customFullScreenVideoView.pause();
        videoPauseImg.setImageResource(R.mipmap.icon_video_start_play);
        videoPlayImg.setVisibility(View.VISIBLE);
    }

    public void start(String url) {
        videoPauseBtn.setEnabled(true);
        seekBarVideo.setEnabled(true);
        customFullScreenVideoView.setVideoURI(Uri.parse(url));
        customFullScreenVideoView.setOnPreparedListener(this);
    }

    public void showErrorView() {
        videoError.setVisibility(View.VISIBLE);
        pausePlay();
    }

    public void setFullScreen() {
        screen_status_img.setImageResource(R.mipmap.icon_exit_fullscreen);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenWidth));
        customFullScreenVideoView.requestLayout();
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void setNormalScreen() {
        screen_status_img.setImageResource(R.mipmap.icon_enter_fullscreen);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, selfHeight));
        customFullScreenVideoView.requestLayout();
        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    /**
     * 设置标题
     *
     * @param charSequence 标题
     */
    public void setTitle(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            titleView.setText(charSequence);
        }
    }

    /**
     * 顶部栏返回
     */
    public void onBackClicked() {
        int i = getResources().getConfiguration().orientation;
        if (i == Configuration.ORIENTATION_PORTRAIT) {//竖屏时 直接结束
            resetVolumAndBrightness();
            ((Activity) context).finish();
        } else if (i == Configuration.ORIENTATION_LANDSCAPE) {//横屏时 先返回竖屏
            ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * activity里面调用恢复 已调整的音量和亮度
     */
    public void resumeVolumAndBrightness() {
        while (lastVolume != AudioManagerUtil.getInstance().getStreamCurrentVolume(context, AudioManager.STREAM_MUSIC))
            if (lastVolume > AudioManagerUtil.getInstance().getStreamCurrentVolume(context,
                    AudioManager.STREAM_MUSIC)) {
                AudioManagerUtil.getInstance().adjustStreamVolumeRAISE(context, AudioManager.STREAM_MUSIC);
            } else {
                AudioManagerUtil.getInstance().adjustStreamVolumeLOWER(context, AudioManager.STREAM_MUSIC);
            }
        if (ScreenBrightnessUtil.getInstance().hasSettingsPermission(context)) {
            if (lastBrightness > 0) {
                ScreenBrightnessUtil.getInstance().closeAutomaticScreenBrightness((Activity) context, true);
                ScreenBrightnessUtil.getInstance().setScreenBritness((Activity) context, lastBrightness);
            }
        }
    }

    /**
     * activity里面调用恢复之前的音量和亮度
     */
    public void resetVolumAndBrightness() {
        while (initVolume != AudioManagerUtil.getInstance().getStreamCurrentVolume(context, AudioManager.STREAM_MUSIC))
            if (initVolume > AudioManagerUtil.getInstance().getStreamCurrentVolume(context,
                    AudioManager.STREAM_MUSIC)) {
                AudioManagerUtil.getInstance().adjustStreamVolumeRAISE(context, AudioManager.STREAM_MUSIC);
            } else {
                AudioManagerUtil.getInstance().adjustStreamVolumeLOWER(context, AudioManager.STREAM_MUSIC);
            }
        if (ScreenBrightnessUtil.getInstance().hasSettingsPermission(context)) {
            if (initBrightness > 0) {
                ScreenBrightnessUtil.getInstance().setScreenBritness((Activity) context, initBrightness);
                ScreenBrightnessUtil.getInstance().closeAutomaticScreenBrightness((Activity) context,
                        !isAutomaticBrightness);
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) {//这个是handler自动更新进度
            int[] time = getMinuteAndSecond(progress);
            videoCurTimeText.setText(String.format(Locale.getDefault(), "%02d:%02d", time[0], time[1]));
        } else {//用户手指触摸 改变进度
            timeMillisACTION_UP = System.currentTimeMillis();
            showTouchViewHorizantalByPercent(progress);
        }
        lastProgess = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        onStartTrackingTouch = true;
        //        customFullScreenVideoView.pause();//按下seekbar暂停播放
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        touchStatusView.setVisibility(View.GONE);
        onStartTrackingTouch = false;
        customFullScreenVideoView.seekTo(seekBarVideo.getProgress());
        customFullScreenVideoView.start();
        videoPlayImg.setVisibility(View.INVISIBLE);
        videoPauseImg.setImageResource(R.mipmap.icon_video_playing);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            setEnterBackGround();
            stopTimer();
        } else if (videoControllerShow)
            startTimer();
    }

    /**
     * 进入后台暂停播放
     */
    private void setEnterBackGround() {
        isBack = true;
        customFullScreenVideoView.pause();
        videoPauseImg.setImageResource(R.mipmap.icon_video_start_play);
        videoPlayImg.setVisibility(View.VISIBLE);
    }

    private enum Direction {
        None, Horizantal, Vertical
    }
}
