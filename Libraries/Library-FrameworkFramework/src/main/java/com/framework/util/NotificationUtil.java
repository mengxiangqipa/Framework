package com.framework.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

/**
 * 通知栏工具类
 */
public class NotificationUtil extends ContextWrapper {

    private NotificationManager manager;
    public static final String NOTIFICATIONCHANNEL_ID = "NOTIFY_CHANNEL_ID";
    public static final String NOTIFICATIONCHANNEL_NAME = "NOTIFY_CHANNEL_NAME";

    public NotificationUtil(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(String notificationchannelId,
                                          String notificationchanneName) {
        NotificationChannel channel =
                new NotificationChannel(TextUtils.isEmpty(notificationchannelId) ?
                NOTIFICATIONCHANNEL_ID : notificationchannelId,
                        TextUtils.isEmpty(notificationchanneName) ?
                NOTIFICATIONCHANNEL_NAME : notificationchanneName,
                NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);//小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
//        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(Context context, Intent intent,
                                                       String title, String content,
                                                       String notificationchanneId,
                                                       boolean api26OnlyAlertOnce) {
        if (context == null) {
            return null;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext(),
                TextUtils.isEmpty(notificationchanneId) ? NOTIFICATIONCHANNEL_ID :
                        notificationchanneId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//悬挂式Notification，5.0后显示
            notificationBuilder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
//            notificationBuilder .setNumber(15);
//            notificationBuilder .setFullScreenIntent(pendingIntent, true);
        }
        //设定震动的模式，以一个long数组保存毫秒级间隔的震动。
        long[] pattern = new long[]{200, 0, 200, 0};
        notificationBuilder.setVibrate(pattern);
        //设置震动模式
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        if (api26OnlyAlertOnce) {
            notificationBuilder.setOnlyAlertOnce(true);
        }
        return notificationBuilder
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getNotification_25(Context context, Intent intent,
                                                         String title, String content) {
        if (context == null) {
            return null;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setVisibility(Notification.VISIBILITY_PUBLIC);
            // 关联PendingIntent
            notificationBuilder.setFullScreenIntent(pendingIntent, true);// 横幅
        }
        //设定震动的模式，以一个long数组保存毫秒级间隔的震动。
        long[] pattern = new long[]{200, 0, 200, 0};
        notificationBuilder.setVibrate(pattern);
        //设置震动模式
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        return notificationBuilder
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    public void sendNotification(Context context, Intent intent, String title, String content) {
        sendNotification(context, intent, title, content, null, null, false);
    }

    public void sendNotification(Context context, Intent intent, String title, String content,
                                 String notificationchanneId, String
                                         notificationchanneName, boolean api26OnlyAlertOnce) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(notificationchanneId, notificationchanneName);
            Notification notification = getChannelNotification
                    (context, intent, title, content, notificationchanneId, api26OnlyAlertOnce).build();
            getManager().notify(0x1234, notification);
        } else {
            Notification notification = getNotification_25(context, intent, title, content).build();
            getManager().notify(0x1234, notification);
        }
    }
}