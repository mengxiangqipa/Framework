package com.framework2.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.utils.ScreenUtils;
import com.framework2.customLoading.LoadingDialog;

/**
 * 自定义对话框
 */
public class CustomLoadingDialog extends Dialog {
    private View view;
    private String mLoadingTip;
    private TextView mLoadingTv;
    private LoadingDialog customLoadingView;

    public CustomLoadingDialog(Context context, String content) {
//        super(context);
        super(context, R.style.CustomDialog);
        this.mLoadingTip = content;
        setCanceledOnTouchOutside(false);
        initView();
        initData();
    }

    private void initData() {
        mLoadingTv.setText(mLoadingTip);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setContent(String str) {
        if (TextUtils.isEmpty(str)) {
            mLoadingTv.setVisibility(View.GONE);
        } else {
            mLoadingTv.setVisibility(View.VISIBLE);
            mLoadingTv.setText(str);
        }
    }

    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.progress_dialog, null);
        mLoadingTv = (TextView) view.findViewById(R.id.loadingTv);
        customLoadingView = (LoadingDialog) view.findViewById(R.id.customLoadingView);
        customLoadingView.setState(LoadingDialog.State.Loading);
        if (TextUtils.isEmpty(mLoadingTip)) {
            mLoadingTv.setVisibility(View.GONE);
        }
    }

    /**
     * 显示dialog
     */
    public void showDialog() {
        show();
        Window window = getWindow();
        if (null != window) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ScreenUtils.getInstance().getScreenWidthPx(getContext());
            window.setAttributes(layoutParams);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(0));
            window.setContentView(view);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }
    }
}
