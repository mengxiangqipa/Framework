package com.framework.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * @author Administrator 下载器工具类
 */
@SuppressLint("InlinedApi")
public class DownLoadManagerUtils {
    public static final String DownLoad_ID = "downloadId";
    public static final String DownLoad_FileName = "downLoad_fileName";
    public static final String SDAvailable = "SDAvailable";
    public static final String FileName = "fileName";
    public static final int DOWNLOAD_STATE_SHOULD_BEGEIN = 0;//应该开始创建下载
    public static final int DOWNLOAD_STATE_RESUME = 1;//从暂停到继续下载状态
    public static final int DOWNLOAD_STATE_UNKNOW = -1;//未知状态
    private static final String DownLoad_State = "downLoad_state";
    private static DownLoadManagerUtils newInstance;
    public String tempPath = Environment.DIRECTORY_DOWNLOADS;
    private DownloadManager downloadManager;

    public static DownLoadManagerUtils getInstance() {
        if (null == newInstance) {
            synchronized (DownLoadManagerUtils.class) {
                if (null == newInstance) {
                    newInstance = new DownLoadManagerUtils();
                }
            }
        }
        return newInstance;
    }

    private DownloadManager getDownloadManager(Context context) {
        if (null == downloadManager) {
            synchronized (DownLoadManagerUtils.class) {
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            }
        }
        return downloadManager;
    }

    /**
     * @param ids 移除正在下载的
     */
    public void removeTask(Context context, long... ids) {
        getDownloadManager(context).remove(ids);
    }

    /**
     * <pre>
     * return  -1 未知错误   0:开始正常下载   4:下载暂停
     *    1:STATUS_PENDING 2:STATUS_RUNNING 8:STATUS_SUCCESSFUL
     *
     * @param url      请求的下载url
     * @param fileName 下载的文件名  //例如:"我的歌声里.mp3"
     * @ (destinationInExternalFilesDir)设置下载后文件存放的位置，不设置会存在data/data/com.android.provider.downloads/cache/下面，
     * 设置后存在sd上的Android/data/<包名>/files/下面。
     * 第2个参数是files下再建目录的目录名，第3个参数是文件名，如果第3个参数带路径，要确保路径存在，第2个参数路径随便写，会自己创建
     * 路径不存在要报错  应该是eg:Environment.DIRECTORY_DOWNLOADS
     *
     * <pre/>
     */
    public int requestDownLoad(Context mContext, String url, String fileName, String notifyTitle, String appName) {
        return requestDownLoad(mContext, url, 0, true, true, 1, Environment.DIRECTORY_DOWNLOADS, "", fileName,
                notifyTitle, appName);
    }

    public int requestDownLoad(Context mContext, String downLoadDir, String url, String fileName, String notifyTitle,
                               String appName) {
        return requestDownLoad(mContext, url, 0, true, true, 1, "", downLoadDir, fileName, notifyTitle, appName);
    }

    public int requestDownLoad(Context mContext, String url, boolean hideNotification, String fileName, String
            notifyTitle, String appName) {
        return requestDownLoad(mContext, url, hideNotification ? 0 : 1, true, true, 0, Environment
                .DIRECTORY_DOWNLOADS, "", fileName, notifyTitle, appName);
    }

    /**
     * <pre>
     * return  -1 未知错误   0:开始正常下载   4:下载暂停
     *    1:STATUS_PENDING 2:STATUS_RUNNING 8:STATUS_SUCCESSFUL  100:已存在直接安装
     *
     * @param url                            请求的下载url
     * @param fileName                       下载的文件名  //例如:"我的歌声里.mp3"
     * @param netWorkType                    网络下载环境  0both wifi and mobile 1wifi 2mobile 默认为0
     * @param setVisibleInDownloadsUi        是否希望下载的文件可以被系统的Downloads应用扫描到并管理
     * @param setAllowedOverRoaming          移动网络情况下是否允许漫游
     * @param notificationVisibility         在通知栏显示下载详情，比如百分比。
     *                                       0HIDDEN 1VISIBLE 2VISIBLE_NOTIFY_COMPLETED
     *                                       3VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION default VISIBLE
     * @param destinationInExternalFilesDir  设置下载后文件存放的位置，不设置会存在data/data/com.android.provider.downloads/cache/下面，
     *                                       设置后存在sd上的Android/data/<包名>/files/下面。
     *                                       第2个参数是files下再建目录的目录名，第3个参数是文件名，如果第3个参数带路径，要确保路径存在，第2个参数路径随便写，会自己创建
     * @param destinationInExternalPublicDir 以sd卡路径为根路径,与上方法只有一个有效。第一个参数创建文件夹用的是mkdir,
     *                                       路径不存在要报错  应该是eg:Environment.DIRECTORY_DOWNLOADS
     *
     *                                       <pre/>
     */
    public int requestDownLoad(Context mContext, String url, int netWorkType, boolean setVisibleInDownloadsUi,
                               boolean setAllowedOverRoaming,
                               int notificationVisibility, String destinationInExternalPublicDir, String
                                       destinationInExternalFilesDir, String fileName, String notifyTitle,
                               String appName) {
        try {
            long filterById = PreferencesHelper.getInstance().getLongData(fileName);
            int queryDownloadStatus = queryDownloadStatus(mContext, filterById, fileName);
            switch (queryDownloadStatus) {
                //-1
                case DOWNLOAD_STATE_UNKNOW:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    break;
                // 1
                case DownloadManager.STATUS_PENDING:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    removeTask(mContext, filterById);
                    return DownloadManager.STATUS_PENDING;
                // 2
                case DownloadManager.STATUS_RUNNING:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 2L);
                    return DownloadManager.STATUS_RUNNING;
                // 4
                case DownloadManager.STATUS_PAUSED:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    removeTask(mContext, filterById);
                    break;
                // 8
                case DownloadManager.STATUS_SUCCESSFUL:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 8L);
                    break;
                // 16
                case DownloadManager.STATUS_FAILED:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    removeTask(mContext, filterById);
                    break;

                default:
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    break;
            }
            DownloadManager.Request requestDownLoad = new DownloadManager.Request(Uri.parse(url));
            // 设置下载路径
            tempPath = getPath(mContext, requestDownLoad, destinationInExternalFilesDir,
                    destinationInExternalPublicDir, fileName);
            Y.y("可以创建新下载或者下载成功tempPath:" + tempPath);
            long state = PreferencesHelper.getInstance().getLongData(DownLoad_State);
            Y.y("可以创建新下载或者下载成功:" + state);
            if (DOWNLOAD_STATE_SHOULD_BEGEIN == state || state == DownloadManager.STATUS_SUCCESSFUL) {//可以创建新下载或者下载成功
                Y.y("可以创建新下载或者下载成功");
                // 创建下载请求
                // 设置允许使用的网络类型
                if (netWorkType == 0) {
                    requestDownLoad.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager
                            .Request.NETWORK_WIFI);
                } else if (netWorkType == 1) {
                    requestDownLoad.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                } else if (netWorkType == 2) {
                    requestDownLoad.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
                } else {
                    requestDownLoad.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager
                            .Request.NETWORK_WIFI);
                }
                // 禁止发出通知，既后台下载
                if (notificationVisibility == 0) {
                    requestDownLoad.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                } else if (notificationVisibility == 1) {
                    requestDownLoad.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                } else if (notificationVisibility == 2) {
                    requestDownLoad.setNotificationVisibility(DownloadManager.Request
                            .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                } else if (notificationVisibility == 3) {
                    requestDownLoad.setNotificationVisibility(DownloadManager.Request
                            .VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
                } else {
                    requestDownLoad.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                }
                // 是否显示下载界面
                requestDownLoad.setVisibleInDownloadsUi(setVisibleInDownloadsUi);

                // 判断漫游
                requestDownLoad.setAllowedOverRoaming(setAllowedOverRoaming);
                requestDownLoad.setTitle(TextUtils.isEmpty(notifyTitle) ? "" : notifyTitle);
                if (hasInstallableApk(mContext, !TextUtils.isEmpty(destinationInExternalFilesDir), tempPath, appName)) {
                    return 100;
                }
                // 将下载请求放入队列
                long id = getDownloadManager(mContext).enqueue(requestDownLoad);
                // 保存id
                PreferencesHelper.getInstance().putInfo(DownLoad_FileName, fileName);
                PreferencesHelper.getInstance().putInfo(fileName, id);
                return DOWNLOAD_STATE_SHOULD_BEGEIN;
            } else if (state == DownloadManager.STATUS_PAUSED) {//下载暂停了 ,然后恢复下载
                getDownloadManager(mContext).remove(filterById);//先删除任务，再重新下载
                requestDownLoad(mContext, url, fileName, notifyTitle, appName);
                return DOWNLOAD_STATE_RESUME;
            }
        } catch (Exception e) {
            Y.y("e" + e.getMessage());
        }
        return DOWNLOAD_STATE_UNKNOW;
    }

    /**
     * @param filterById 查询下载的id
     * @param fileName   在sharepreference里面存的fileName键
     * @return 查询下载状态
     */
    public int queryDownloadStatus(Context context, long filterById, String fileName) {
        try {
            Y.y("queryDownloadStatus开始查询");
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(filterById);
            Cursor c = getDownloadManager(context).query(query);
            int status = 0;
            //			String path = null;
            if (c.moveToFirst()) {
                status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                //				path = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                c.close();
                Y.y("queryDownloadStatus-de");
            }
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
//				Log.i("yy", "queryDownloadStatus-STATUS_PAUSED");
                    Y.y("queryDownloadStatus-STATUS_PAUSED");
                    return DownloadManager.STATUS_PAUSED;
                case DownloadManager.STATUS_PENDING:
//				Log.i("yy", "queryDownloadStatus-STATUS_PENDING");
                    Y.y("queryDownloadStatus-STATUS_PENDING");
                    return DownloadManager.STATUS_PENDING;
                case DownloadManager.STATUS_RUNNING:
                    // 正在下载，不做任何事情
//				Log.i("yy", "queryDownloadStatus-STATUS_RUNNING");
                    Y.y("queryDownloadStatus-STATUS_RUNNING");
                    return DownloadManager.STATUS_RUNNING;
                case DownloadManager.STATUS_SUCCESSFUL:
                    // 完成
//				Log.i("yy", "queryDownloadStatus-STATUS_SUCCESSFUL");
                    Y.y("queryDownloadStatus-STATUS_SUCCESSFUL");
                    return DownloadManager.STATUS_SUCCESSFUL;
                case DownloadManager.STATUS_FAILED:
//				Log.i("yy", "queryDownloadStatus-STATUS_FAILED");
                    Y.y("queryDownloadStatus-STATUS_FAILED");
                    // 清除已下载的内容，重新下载
                    if (null == fileName || "".equals(fileName)) {
                        PreferencesHelper.getInstance().putInfo(DownLoad_ID, 0L);
                    }
                    PreferencesHelper.getInstance().putInfo(DownLoad_State, 0L);
                    return DownloadManager.STATUS_FAILED;
            }
        } catch (Exception e) {
            Y.y("queryDownloadStatus-Exception:" + e.getMessage());
        }
        return -1;
    }

    public int queryDownloadStatus(Context context, long filterById) {
        return queryDownloadStatus(context, filterById, null);
    }

    private void installAPK(Context context, String path,String authority) {
        try {
            if (PreferencesHelper.getInstance().getBooleanData(SDAvailable)) {
                PackageManagerUtil.getInstance().installApk(context, path,authority);// SD卡可用
            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory("Download"), DownloadManager
                        .COLUMN_TITLE);
                if (file.exists()) {
                    PackageManagerUtil.getInstance().installApk(context, file.getPath(),authority);
                }
            }
        } catch (Exception e) {
            Y.y("error:" + e.getMessage());
        }
    }

    /**
     * 判断是否有能安装且名字合适的apk
     *
     * @param mContext 上下文
     * @param innerDir 是否是内部文件
     * @param tempPath 文件路径
     * @return 是否存在可以安装的apk, 返回true会自动安装
     */
    public boolean hasInstallableApk(Context mContext, boolean innerDir, String tempPath, String apkName) {
        Y.y("hasInstallableApk：" + tempPath);
        try {
            if (innerDir) {
                tempPath = mContext.getExternalFilesDir(tempPath).getAbsolutePath();
            } else if (TextUtils.equals(tempPath, Environment.DIRECTORY_DOWNLOADS)) {
                tempPath = Environment.getExternalStorageDirectory().getPath() + File.separator + tempPath;
            }
            Y.y("hasInstallableApk：" + tempPath);
            File f = new File(tempPath);
            if (f.exists()) {
                File[] files = f.listFiles();
                if (null != files) {
                    for (File file : files) {
                        //文件包含了apk
                        try {
                            Y.y("hasInstallableApk：" + "file.isFile():" + file.isFile() + "  file.exists():" + file
                                    .exists() + "   getPackageName:" + mContext.getPackageName() + "  " +
                                    "otherPackageName:" + PackageManagerUtil.getInstance().getOtherApkPkgName
                                    (mContext, File.separator + File.separator + file.getPath()));
                            if (file != null && file.isFile() && file.exists() && !TextUtils.isEmpty(mContext
                                    .getPackageName())
                                    && TextUtils.equals(
                                    PackageManagerUtil.getInstance().getOtherApkPkgName(mContext, File.separator +
                                            File.separator + file.getPath()),
                                    mContext.getPackageName())) {
                                if (PackageManagerUtil.getInstance().isApkCanInstall(mContext, File.separator + File
                                        .separator + file.getPath())) {
                                    if (PackageManagerUtil.getInstance().getOtherApkVersionCode(mContext,
                                            File.separator + File.separator + file.getPath()) >= PackageManagerUtil
                                            .getInstance()
                                            .getCurrentApkVersionCode(mContext)) {
                                        installAPK(mContext, File.separator + File.separator + file.getPath(),mContext.getPackageName()+".provider");
                                        return true;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Y.y("error:" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Y.y("hasInstallableApk：" + "没有可用apk包");
        return false;
    }

    /**
     * 获取文件下载的位置
     *
     * @param mContext                       上下文
     * @param requestDownLoad                下载请求
     * @param destinationInExternalFilesDir  destinationInExternalFilesDir
     * @param destinationInExternalPublicDir destinationInExternalPublicDir
     * @param fileName                       文件名
     * @return 返回下载路劲
     */
    private String getPath(Context mContext, DownloadManager.Request requestDownLoad, String
            destinationInExternalFilesDir,
                           String destinationInExternalPublicDir, String fileName) {
        String tempPath = null;
        try {
            if (PackageManagerUtil.getInstance().isSDcardExist()) {
                PreferencesHelper.getInstance().putInfo(SDAvailable, true);
                if (!TextUtils.isEmpty(destinationInExternalFilesDir)) {
                    // 完全定义的路径存在
                    requestDownLoad.setDestinationInExternalFilesDir(mContext, destinationInExternalFilesDir, fileName);
                    tempPath = destinationInExternalFilesDir;
                } else if (!TextUtils.isEmpty(destinationInExternalPublicDir)) {
                    // 非完全定义的路径不存在,则设置系统路径
                    requestDownLoad.setDestinationInExternalPublicDir(destinationInExternalPublicDir, fileName);
                    tempPath = destinationInExternalPublicDir;
                }
            } else {
                PreferencesHelper.getInstance().putInfo(SDAvailable, false);
                try {
                    requestDownLoad.setDestinationInExternalPublicDir(destinationInExternalPublicDir, fileName);
                    tempPath = destinationInExternalPublicDir;
                } catch (Exception e) {
                    Y.y("error:" + e.getMessage());
                }
            }
        } catch (Exception e) {
            try {
                requestDownLoad.setDestinationInExternalPublicDir(destinationInExternalPublicDir, fileName);
                tempPath = destinationInExternalPublicDir;
            } catch (Exception e2) {
                Y.y("error:" + e.getMessage());
            }
        }
        return tempPath;
    }
}
