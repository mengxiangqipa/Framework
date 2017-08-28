package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.demo.activity.HomePageActivity;
import com.demo.configs.EventBusTag;
import com.demo.configs.InterfaceConfig;
import com.demo.entity.UpdateInfo;
import com.demo.networkModel.HttpUtil;
import com.framework.Utils.ActivityTaskUtil;
import com.framework.Utils.PackageManagerUtil;
import com.framework.Utils.Y;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * service
 *
 * @author Yangjie
 *         className CheckUpdateService
 *         created at  2017/4/6  13:38
 */

public class CheckUpdateService extends Service
{

    private Timer timer;
    private TimerTask timerTask;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initTimer();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * 全局timer------未使用长链接
     */
    private void initTimer()
    {
        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 1000, 10000);
    }

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if (ActivityTaskUtil.getInstance().isTopActivity(CheckUpdateService.this, HomePageActivity.class.getName()))
                requestUpdate();
        }
    };

    private void requestUpdate()
    {
        try
        {
            HttpUtil.getInstance().requestVersionUpdate(InterfaceConfig.checkUpdateUrl, new HttpUtil.OnRequestResult<String>()
            {
                @Override
                public void onSuccess(String... t)
                {
                    if (null != t && t.length >= 4)
                    {
                        int versionCodeFromServer = 0;
                        try
                        {
                            versionCodeFromServer = Integer.parseInt(t[0]);
                        } catch (NumberFormatException e)
                        {
                            e.printStackTrace();
                        }
                        Y.y("versionCodeFromServer:" + versionCodeFromServer);
                        Y.y("local:" + PackageManagerUtil.getInstance().getCurrentApkVersionCode(getApplicationContext()));
                        if (versionCodeFromServer > PackageManagerUtil.getInstance().getCurrentApkVersionCode(getApplicationContext()))
                        {
                            UpdateInfo updateInfo = new UpdateInfo();
                            updateInfo.setVersion(TextUtils.isEmpty(t[1]) ? "版本号未知" : t[1]);
                            updateInfo.setUpdateContent(TextUtils.isEmpty(t[2]) ? "版本号未知" : t[2]);
                            updateInfo.setDownLoadUrl(TextUtils.isEmpty(t[3]) ? "http://www.baidu.com" : t[3]);
                            EventBus.getDefault().post(updateInfo, EventBusTag.updateDialog);
                        }
                    }
                }

                @Override
                public void onFail(int code, String msg)
                {
                    Y.y("onFail:" + msg);
                }
            });
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        cancelTask();
        super.onDestroy();
    }

    private void cancelTask()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null)
        {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
