package com.framework.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PackageManagerUtil {
    private static volatile PackageManagerUtil instance;

    public PackageManagerUtil() {
    }

    public static PackageManagerUtil getInstance() {
        if (null == instance) {
            synchronized (PackageManagerUtil.class) {
                if (null == instance) {
                    instance = new PackageManagerUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 判断SD卡是否存在
     */
    public boolean isSDcardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * <pre>
     * 获取已安装程序列表包名
     * @return
     * <pre/>
     */
    public String getInstallAppPackage(Context context, String name) {
        PackageManager manager = context.getPackageManager();
        List<ApplicationInfo> aiList = manager.getInstalledApplications(0);
        for (ApplicationInfo ai : aiList) {
            if ((ApplicationInfo.FLAG_SYSTEM & ai.flags) == 0) {
                if (name.equals(manager.getApplicationLabel(ai).toString())) {
                    return ai.packageName;
                }
            }
        }
        return "";
    }

    /**
     * 获取指定路径中apk的版本code
     *
     * @param filePath 已下载apk路径
     * @return 包名
     */
    public String getOtherApkPkgName(Context mContext, String filePath) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return info.packageName;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * <pre>
     * 打开应用程序
     * @throws Exception
     *
     * <pre/>
     */
    public void openApp(Context context, String packageName) throws Exception {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(packageName, 0);
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String pkg = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(pkg, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * <pre>
     * 卸载程序
     * @param context
     *
     * <pre>
     */
    public void unInstallApp(Context context, String package1) {
        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + package1));
        context.startActivity(intent);
    }

    /**
     * <pre>
     * @param apkPath /storage/emulated/0/Download/和路通
     * @param apkPath 7.0及以上使用的文件访问authority  形如 包名+".fileprovider"
     * 安装APP
     *
     * <pre/>
     */
    public void installApk(Context context, String apkPath, String authority) {
        if (null == apkPath) {
            return;
        }
        if (!TextUtils.isEmpty(apkPath) && apkPath.endsWith(".apk")) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //7.0以上通过FileProvider
            if (android.os.Build.VERSION.SDK_INT >= 24) {
                Uri uri = FileProvider.getUriForFile(context, TextUtils.isEmpty(authority) ? context.getPackageName()
                        + ".fileprovider" : authority, new File(apkPath));
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (apkPath.startsWith("file:")) {
                    intent.setDataAndType(Uri.parse(apkPath), "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
                }
            }
            context.startActivity(intent);
        }
    }

    /**
     * @param authority              activity.getPackageName() + "" + ".fileprovider"
     * @param permissionRequestConde Manifest.permission.REQUEST_INSTALL_PACKAGES 的权限请求code
     * @return 进入安装页面就返回true 返回false标识进入了申请权限
     */
    public boolean installApk(Activity activity, String apkPath, String authority, int permissionRequestConde) {
        if (null == activity || TextUtils.isEmpty(apkPath))
            return false;
        if (Build.VERSION.SDK_INT >= 26) {
            //来判断应用是否有权限安装apk
            boolean installAllowed = activity.getPackageManager().canRequestPackageInstalls();
            Y.y("installAPK---：" + installAllowed);
            //有权限
            if (installAllowed) {
                Y.y("installAPK---安装apk：");
                //安装apk
                PackageManagerUtil.getInstance().installApk(activity, apkPath, TextUtils.isEmpty(authority) ?
                        activity.getPackageName() + "" + ".fileprovider" : authority);
                return true;
            } else {
                Y.y("installAPK---无权限 申请权限：");
                //无权限 申请权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission
                        .REQUEST_INSTALL_PACKAGES}, permissionRequestConde);
                return false;
            }
        } else {
            PackageManagerUtil.getInstance().installApk(activity, apkPath, TextUtils.isEmpty(authority) ?
                    activity.getPackageName() + "" + ".fileprovider" : authority);
            return true;
        }
    }

    /**
     * 判断apk文件是否能安装
     *
     * @param filePath apk路劲
     * @return 是否能安装
     */
    public boolean isApkCanInstall(Context mContext, String filePath) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取指定路径中apk的版本code
     *
     * @param mContext 上下文
     * @param filePath 其他apk文件路劲
     * @return -1为非完整apk文件
     */
    public int getOtherApkVersionCode(Context mContext, String filePath) {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return info.versionCode;
            } else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取当前apk的versioncode--int
     *
     * @param mContext 上下文
     * @return getCurrentApkVersionCode
     */
    public int getCurrentApkVersionCode(Context mContext) {
        try {
            int code;
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            code = info.versionCode;
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取当前应用名称
     */
    public String getCurrentApkName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo
                    (context.getPackageName(), 0));
            return applicationLabel.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号
     */
    public String getCurrentApkVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * 检测SDcard是否可用
     */
    public boolean inspectSDcardIsAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public String getTargetPath(Context context, String url, String fileName) {
        String extention = url.substring(url.lastIndexOf("."));
        return setLocalePath(context) + fileName + System.currentTimeMillis() + extention;
    }

    private String setLocalePath(Context context) {
        if (PackageManagerUtil.getInstance().inspectSDcardIsAvailable()) {
            String path = Environment.getExternalStoragePublicDirectory("fruits") + File.separator + "apks";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath() + File.separator;
        } else {
            String path = context.getCacheDir() + File.separator + "apks";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] args = {"chmod", "705", file.getAbsolutePath()};
            PackageManagerUtil.getInstance().exec(args);
            return file.getAbsolutePath() + File.separator;
        }
    }

    /**
     * 执行Linux命令，并返回执行结果。
     */
    public String exec(String[] args) {
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            int read;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    /**
     * <pre>
     * scrollTo方法可以调整view的显示位置。 在需要的地方调用以下方法即可。
     * scroll表示外层的view，inner表示内层的view，其余内容都在inner里。 注意，方法中开一个新线程是必要的。
     * 否则在数据更新导致换行时getMeasuredHeight方法并不是最新的高度。
     * <pre/>
     */
    public void scrollToBottom(final View scroll, final View inner) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }
                scroll.scrollTo(0, offset);
            }
        });
    }
}
