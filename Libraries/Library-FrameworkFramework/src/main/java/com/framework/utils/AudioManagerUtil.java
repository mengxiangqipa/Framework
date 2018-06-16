package com.framework.utils;

import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;

import java.io.IOException;

/**
 * @author Yobert Jomi
 * className AudioManagerUtil
 * created at  2016/10/1  23:10
 */
public class AudioManagerUtil {
    private static volatile AudioManagerUtil instance;
    private AudioManager mAudioManager;

    public static AudioManagerUtil getInstance() {
        if (null == instance) {
            synchronized (AudioManagerUtil.class) {
                if (null == instance) {
                    instance = new AudioManagerUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @param context                                   context
     * @param streamType_AudioManager_STREAM_VOICE_CALL AudioManager.STREAM_VOICE_CALL
     */
    public int getStreamMaxVolume(Context context, int streamType_AudioManager_STREAM_VOICE_CALL) {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager.getStreamMaxVolume(streamType_AudioManager_STREAM_VOICE_CALL);
    }

    /**
     * @param context                                   context
     * @param streamType_AudioManager_STREAM_VOICE_CALL AudioManager.STREAM_VOICE_CALL
     */
    public int getStreamCurrentVolume(Context context, int streamType_AudioManager_STREAM_VOICE_CALL) {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager.getStreamVolume(streamType_AudioManager_STREAM_VOICE_CALL);
    }

    /**
     * @param context                                   context
     * @param streamType_AudioManager_STREAM_VOICE_CALL AudioManager.STREAM_VOICE_CALL
     */
    public void adjustStreamVolumeRAISE(Context context, int streamType_AudioManager_STREAM_VOICE_CALL) {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        //        mAudioManager.adjustStreamVolume(streamType_AudioManager_STREAM_VOICE_CALL,
        //                AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
        //                        | AudioManager.FLAG_SHOW_UI);
        mAudioManager.adjustStreamVolume(streamType_AudioManager_STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * parm context
     * parm streamType AudioManager.STREAM_VOICE_CALL
     */
    public void adjustStreamVolumeLOWER(Context context, int streamType_AudioManager_STREAM_VOICE_CALL) {
        if (null == mAudioManager) {
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        }
        //        mAudioManager.adjustStreamVolume(streamType_AudioManager_STREAM_VOICE_CALL,
        //                AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND
        //                        | AudioManager.FLAG_SHOW_UI);
        mAudioManager.adjustStreamVolume(streamType_AudioManager_STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    /**
     * 模拟音量键  加
     */
    public void simulationVolumeRAISE() {
        try {
            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_UP;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟音量键  减
     */
    public void simulationVolumeLOWER() {
        try {
            String keyCommand = "input keyevent " + KeyEvent.KEYCODE_VOLUME_DOWN;
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(keyCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
