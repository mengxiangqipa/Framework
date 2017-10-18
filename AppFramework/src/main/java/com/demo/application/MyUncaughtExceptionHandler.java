package com.demo.application;

import java.lang.Thread.UncaughtExceptionHandler;

import com.demo.activity.HomePageActivity;
import com.framework.utils.ActivityTaskUtil;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

class MyUncaughtExceptionHandler implements UncaughtExceptionHandler
{
	private ShareApplication application;
	private UncaughtExceptionHandler mDefaultHandler;

	MyUncaughtExceptionHandler(ShareApplication application)
	{
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		this.application = application;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else
		{
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				Log.e("yy", "error : ", e);
			}
			Intent intent = new Intent(application.getApplicationContext(), HomePageActivity.class);
			PendingIntent restartIntent = PendingIntent.getActivity(application.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// 退出程序
			AlarmManager alarmManager = (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 500ms钟后重启应用
			ActivityTaskUtil.getInstance().exit();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex Throwable
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread()
		{
			@Override
			public void run()
			{
				Looper.prepare();
				// Toast.makeText(application.getApplicationContext(),
				// "很抱歉,程序将重启.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		return true;
	}

}
