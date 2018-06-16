package com.framework.customviews.clipview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

public class ClipImageLayout extends RelativeLayout {
    LayoutParams lp;
    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    private ClipCircleBorderView mClipCircleView;
    private int mHorizontalPadding = 0;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mZoomImageView = new ClipZoomImageView(context);
        this.mClipImageView = new ClipImageBorderView(context);
        this.lp = new LayoutParams(-1, -1);
        this.addView(this.mZoomImageView, this.lp);
        this.addView(this.mClipImageView, this.lp);
        this.mHorizontalPadding = (int) TypedValue.applyDimension(1, (float) this.mHorizontalPadding, this
                .getResources().getDisplayMetrics());
        this.mZoomImageView.setHorizontalPadding(this.mHorizontalPadding);
        this.mClipImageView.setHorizontalPadding(this.mHorizontalPadding);
    }

    public void setClipLayoutCircle(Context context) {
        this.removeAllViews();
        this.mZoomImageView = new ClipZoomImageView(context);
        this.mClipCircleView = new ClipCircleBorderView(context);
        this.addView(this.mZoomImageView, this.lp);
        this.addView(this.mClipCircleView, this.lp);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        mHorizontalPadding = (int) TypedValue.applyDimension(1, (float) mHorizontalPadding, this.getResources()
                .getDisplayMetrics());
        this.mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        if (this.mClipImageView != null) {
            this.mClipImageView.setHorizontalPadding(mHorizontalPadding);
        }

        if (this.mClipCircleView != null) {
            this.mClipCircleView.setHorizontalPadding(mHorizontalPadding);
        }
    }

    public void setClipImageBorderViewHide(boolean hide) {
        if (hide) {
            this.mClipImageView.setVisibility(4);
        }
    }

    public void setClipZoomImageViewBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            this.mZoomImageView.setImageBitmap(bitmap);
        }
    }

    public void setClipZoomImageViewDrawable(Drawable drawable) {
        if (drawable != null) {
            this.mZoomImageView.setImageDrawable(drawable);
        }
    }

    public void setClipZoomImageViewResource(int resId) {
        if (resId != 0) {
            this.mZoomImageView.setImageResource(resId);
        }
    }

    public void setClipZoomImageViewMatrix(Matrix matrix) {
        if (matrix != null) {
            this.mZoomImageView.setImageMatrix(matrix);
        }
    }

    public void setClipLayoutWidth(int mWidth, Context context) {
        this.mZoomImageView.setClipZoomImageViewWidth(mWidth, context);
        if (this.mClipImageView != null) {
            this.mClipImageView.setClipImageBorderViewWidth(mWidth, context);
        }

        if (this.mClipCircleView != null) {
            this.mClipCircleView.setClipCircleBorderViewWidth(mWidth, context);
        }
    }

    public Bitmap clip() {
        return this.mZoomImageView.clip();
    }
}