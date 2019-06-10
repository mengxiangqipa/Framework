package com.framework.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @author YobertJomi
 * className AlarmUtil
 * created at  2017/8/4  10:43
 */
public class AlarmUtil {
    private static volatile AlarmUtil instance;

    private AlarmUtil() {
    }

    public static AlarmUtil getInstance() {
        if (instance == null) {
            synchronized (AlarmUtil.class) {
                if (null == instance) {
                    instance = new AlarmUtil();
                }
            }
        }
        return instance;
    }

    /**
     * @param context
     * @param intent
     * @param requestCode      alarm 的tag
     * @param wakeUpTimeMillis 开始触发时间毫秒
     */
    public void setAlarmTaskActivity(Context context, Intent intent, int requestCode, long wakeUpTimeMillis) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schedule the alarm!
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                wakeUpTimeMillis, pendingIntent);
    }

    /**
     * @param context
     * @param intent
     * @param requestCode      alarm 的tag
     * @param wakeUpTimeMillis 开始触发时间毫秒
     */
    public void setAlarmTaskRepeatActivity(Context context, Intent intent, int requestCode, long wakeUpTimeMillis,
                                           long intervalMillis) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schedule the alarm!
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                wakeUpTimeMillis, intervalMillis, pendingIntent);
    }

    /**
     * @param context
     * @param intent
     * @param requestCode      alarm 的tag
     * @param wakeUpTimeMillis 开始触发时间毫秒
     */
    public void setAlarmTaskService(Context context, Intent intent, int requestCode, long wakeUpTimeMillis) {
        PendingIntent pendingIntent = PendingIntent.getService(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schedule the alarm!
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                wakeUpTimeMillis, pendingIntent);
    }

    /**
     * @param context
     * @param intent
     * @param requestCode      alarm 的tag
     * @param wakeUpTimeMillis 开始触发时间毫秒
     */
    public void setAlarmTaskRepeatService(Context context, Intent intent, int requestCode, long wakeUpTimeMillis,
                                          long intervalMillis) {
        PendingIntent pendingIntent = PendingIntent.getService(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schedule the alarm!
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                wakeUpTimeMillis, intervalMillis, pendingIntent);
    }
}
