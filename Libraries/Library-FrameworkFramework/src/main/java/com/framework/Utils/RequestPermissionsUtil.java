package com.framework.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * @author Yangjie
 *         className RequestPermissionsUtil Android6.0请求权限工具类
 *         created at  2016/9/26  10:18
 */
public class RequestPermissionsUtil
{

	private static volatile RequestPermissionsUtil instance;
	public static int PERMISSION_LOCATION = 1;
	public static int PERMISSION_CAMERA = 2;
	public static int PERMISSION_READ_EXTERNAL_STORAGE = 3;
	public static int PERMISSION_WRITE_EXTERNAL_STORAGE = 4;
	public static int PERMISSION_WRITE_READ_EXTERNAL_STORAGE = 5;

	public static RequestPermissionsUtil getInstance()
	{
		if (null == instance)
		{
			synchronized (RequestPermissionsUtil.class)
			{
				if (null == instance)
				{
					instance = new RequestPermissionsUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * @param activity activity
	 * @param permissions new String{ Manifest.permission.ACCESS_COARSE_LOCATION }
	 * @return true 需要申请权限，false已有权限
	 */
	public boolean checkPermissionsThenRequest(Activity activity, String[] permissions, int requestCode)
	{
		if (null != permissions)
		{
			for (String permission : permissions)
			{
				if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
				{
					ActivityCompat.requestPermissions(activity, permissions, requestCode);
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * @param activity
	 * @param permissions new String{ Manifest.permission.ACCESS_COARSE_LOCATION }
	 */
	//    private void checkPermissions(Activity activity, String[] permissions, int requestCode) {
	//        if (null != permissions) {
	//            List<String> listPermission = new ArrayList<String>();
	//            String[] permissionsTemp;
	//            for (String permission : permissions) {
	//                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
	//                    listPermission.add(permission);
	//                }
	//            }
	//            if (listPermission.size() > 0) {
	//                permissionsTemp = (String[]) listPermission.toArray();
	//                ActivityCompat.requestPermissions(activity, permissionsTemp, requestCode);
	//            }
	//        }
	//    }

	/**
	 * 以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
	 *@param packageName  包名，当前应用context.getPackageName(),其他的传入字符串
	 */
	public void showInstalledAppDetailSettingIntent(Context context, String packageName)
	{
		Intent localIntent = new Intent();
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 9)
		{// 2.3（ApiLevel 9）以上，使用SDK提供的接口
			localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			localIntent.setData(Uri.fromParts("package", TextUtils.isEmpty(packageName) ? context.getPackageName() : packageName, null));
		} else if (Build.VERSION.SDK_INT <= 8)
		{// 2.3以下，使用非公开的接口（API9查看InstalledAppDetails源码）
				// 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。pkg(2.2 API8)/com.android.settings.ApplicationPkgName(2.1 API7及以下)
			localIntent.setAction(Intent.ACTION_VIEW);
			localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
			localIntent.putExtra(Build.VERSION.SDK_INT == 8 ? "pkg" : "com.android.settings.ApplicationPkgName",
					TextUtils.isEmpty(packageName) ? context.getPackageName() : packageName);
		}
		context.startActivity(localIntent);
	}

	/**
	 * 以下代码可以跳转到应用详情，可以通过应用详情跳转到权限界面(6.0系统测试可用)
	 * 当前应用
	 */
	public void showCurrentAppDetailSettingIntent(Context context)
	{
		showInstalledAppDetailSettingIntent(context, context.getPackageName());
	}
}
