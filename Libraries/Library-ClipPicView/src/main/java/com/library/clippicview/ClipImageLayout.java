package com.library.clippicview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * 可裁剪imageview的viewgroup
 */
public class ClipImageLayout extends RelativeLayout {

    android.view.ViewGroup.LayoutParams lp;
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private ClipCircleBorderView mClipCircleView;
    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 0;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        lp = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        /**
         * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
         */
        mZoomImageView.setImageDrawable(getResources().getDrawable(
                R.drawable.actionbar_clip_icon));

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 设置选图未圆形
     * 应第一步设置
     */
    public void setClipLayoutCircle(Context context) {
        this.removeAllViews();
        mZoomImageView = new ClipZoomImageView(context);
        mClipCircleView = new ClipCircleBorderView(context);
        this.addView(mZoomImageView, lp);
        this.addView(mClipCircleView, lp);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        if (mClipImageView != null) {
            mClipImageView.setHorizontalPadding(mHorizontalPadding);
        }
        if (mClipCircleView != null) {
            mClipCircleView.setHorizontalPadding(mHorizontalPadding);
        }
    }

    /**
     * 对外公布设置ClipImageBorderView（白色边框）隐藏的方法
     *
     * @param hide
     */
    public void setClipImageBorderViewHide(boolean hide) {
        if (hide) {
            mClipImageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 对外公布设置ClipImageBorderView的bitmap
     */
    public void setClipZoomImageViewBitmap(Bitmap bitmap) {
        if (null != bitmap) {
            mZoomImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 对外公布设置ClipImageBorderView的Drawable
     */
    public void setClipZoomImageViewDrawable(Drawable drawable) {
        if (null != drawable) {
            mZoomImageView.setImageDrawable(drawable);
        }
    }

    /**
     * 对外公布设置ClipImageBorderView的Resource
     */
    public void setClipZoomImageViewResource(int resId) {
        if (0 != resId) {
            mZoomImageView.setImageResource(resId);
        }
    }

    /**
     * 对外公布设置ClipImageBorderView的Drawable
     */
    public void setClipZoomImageViewMatrix(Matrix matrix) {
        if (null != matrix) {
            mZoomImageView.setImageMatrix(matrix);
        }
    }

    public void setClipLayoutWidth(int mWidth, Context context) {
        mZoomImageView.setClipZoomImageViewWidth(mWidth, context);
        if (mClipImageView != null) {
            mClipImageView.setClipImageBorderViewWidth(mWidth, context);
        }
        if (mClipCircleView != null) {
            mClipCircleView.setClipCircleBorderViewWidth(mWidth, context);
        }
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }
}
