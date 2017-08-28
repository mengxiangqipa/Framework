package com.framework2.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.configs.ConstantsME;
import com.demo.demo.R;
import com.framework.Utils.DownLoadManagerUtils;
import com.framework.Utils.DownLoadObserver;
import com.framework.Utils.PreferencesHelper;
import com.framework.Utils.RequestPermissionsUtil;
import com.framework.Utils.ScreenUtils;

/**
 * 自定义的dialog
 *
 * @author Yangjie
 *         className UpdateDialog
 *         created at  2017/3/17  10:06
 */
public class UpdateDialog extends Dialog
{
    private View view;
    private TextView tvSure;
    private TextView tvCancel;
    private TextView desc;
    private TextView versionTxt;
    private String downLoadUrl;
    private String version;
    private boolean isUpdate;
    private Activity activity;

    public UpdateDialog(Activity activity) {
//        super(activity, R.style.CustomDialog);
        super(activity);
        this.activity = activity;
        init();
    }

    public UpdateDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public UpdateDialog(Activity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        init();
    }

    private void init() {
        view = LayoutInflater.from(getContext()).inflate(
                R.layout.update_layout, null);
        desc = (TextView) view.findViewById(R.id.desc);
        versionTxt = (TextView) view.findViewById(R.id.versionTxt);
        tvSure = (TextView) view.findViewById(R.id.tv_ensure);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

//        view.setPadding(32, 0, 32, 0);
    }

    private void initData(final Handler progressHandler) {
        tvSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isUpdate) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        PreferencesHelper.getInstance().putInfo(
                                DownLoadManagerUtils.FileName,
                                getContext().getResources().getString(R.string.app_name)
                                        + PreferencesHelper.getInstance().getStringData(ConstantsME.appVersion) + ".apk");
                        DownLoadManagerUtils.getInstance().requestDownLoad(getContext(),
                                Environment.DIRECTORY_DOWNLOADS,
                                downLoadUrl,
                                getContext().getResources().getString(R.string.app_name)
                                        + version + ".apk",
                                getContext().getResources().getString(R.string.app_name) + "新版本"
                                        + version, getContext().getResources().getString(R.string.app_name));
                        if (null != progressHandler) {
                            //监听下载进度条
                            activity.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new DownLoadObserver(progressHandler, activity,
                                    PreferencesHelper.getInstance().getLongData(PreferencesHelper.getInstance().getStringData(DownLoadManagerUtils.DownLoad_FileName))));
                        }
                        dismiss();
                    } else {
                        int permissionCheck1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                        int permissionCheck2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            dismiss();
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RequestPermissionsUtil.PERMISSION_WRITE_READ_EXTERNAL_STORAGE);
                        } else {
                            PreferencesHelper.getInstance().putInfo(
                                    DownLoadManagerUtils.FileName,
                                    getContext().getResources().getString(R.string.app_name)
                                            + PreferencesHelper.getInstance().getStringData(ConstantsME.appVersion) + ".apk");
                            DownLoadManagerUtils.getInstance().requestDownLoad(getContext(),
                                    Environment.DIRECTORY_DOWNLOADS,
                                    downLoadUrl,
                                    getContext().getResources().getString(R.string.app_name)
                                            + PreferencesHelper.getInstance().getStringData(ConstantsME.appVersion) + ".apk",
                                    getContext().getResources().getString(R.string.app_name) + "新版本"
                                            + PreferencesHelper.getInstance().getStringData(ConstantsME.appVersion), getContext().getResources().getString(R.string.app_name));
                            if (null != progressHandler) {
                                //监听下载进度条
                                activity.getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true, new DownLoadObserver(progressHandler, activity,
                                        PreferencesHelper.getInstance().getLongData(PreferencesHelper.getInstance().getStringData(DownLoadManagerUtils.DownLoad_FileName))));
                            }
                            dismiss();
                        }
                    }
                } else {
                    dismiss();
                }
            }
        });
        if (!isUpdate)
            tvCancel.setVisibility(View.GONE);
        else
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
    }

    public UpdateDialog setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
        return this;
    }

    public UpdateDialog setVersion(String version) {
        versionTxt.setText("新版本：" + version);
        this.version = version;
        return this;
    }

    public UpdateDialog setContent(@NonNull String content) {
        desc.setText(content);
        return this;
    }

    public UpdateDialog setUpdate(boolean update) {
        isUpdate = update;
        return this;
    }

    public void showDialog() {
        showDialog(null);
    }

    /**
     * 显示dialog
     */
    public void showDialog(Handler progressHandler) {
        initData(progressHandler);
        show();
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ScreenUtils.getInstance().getScreenWidthPx(getContext());
            window.setAttributes(layoutParams);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setContentView(view);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
    }
}
