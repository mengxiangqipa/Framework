package com.library.clippicview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281 判断边界的自定义view
 */
public class ClipCircleBorderView extends View {
    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;
    /**
     * 绘制的矩形的宽度
     */
    private int mWidth;
    /**
     * 边框的颜色，默认为白色
     */
    private int mBorderColor = Color.parseColor("#FFFFFF");
    /**
     * 边框的宽度 单位dp
     */
    private int mBorderWidth = 1;

    private Paint mPaint;

    public ClipCircleBorderView(Context context) {
        this(context, null);
    }

    public ClipCircleBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipCircleBorderView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);

        mBorderWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
                        .getDisplayMetrics());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    /**
     * 设置ClipCircleBorderView的radius 单位dp
     */
    public void setClipCircleBorderViewWidth(int mWidth, Context context) {
        if (mWidth != 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            mHorizontalPadding = (outMetrics.widthPixels - (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, mWidth,
                            getResources().getDisplayMetrics())) / 2;
            if (mHorizontalPadding <= 0) {
                mHorizontalPadding = 0;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 计算矩形区域的宽度
        mWidth = getWidth() - 2 * mHorizontalPadding;
        // 计算距离屏幕垂直边界 的边距
        mVerticalPadding = (getHeight() - mWidth) / 2;
        mPaint.setColor(Color.parseColor("#aa000000"));
        mPaint.setStyle(Style.FILL);
        // 绘制左边1
        canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
        // 绘制右边2
        canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
                getHeight(), mPaint);
        // 绘制上边3
        canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
                mVerticalPadding, mPaint);
        // 绘制下边4
        canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
                getWidth() - mHorizontalPadding, getHeight(), mPaint);
        // 以下为绘制 户型
        RectF oval = new RectF();
        oval.left = mHorizontalPadding;
        oval.top = mVerticalPadding;
        oval.right = getWidth() - mHorizontalPadding;
        oval.bottom = getHeight() - mVerticalPadding;
        Path path1 = new Path();
        path1.moveTo(mHorizontalPadding, mVerticalPadding);
        path1.lineTo(mHorizontalPadding, getHeight() / 2);
        path1.addArc(oval, 180, 90);
        path1.lineTo(mHorizontalPadding, mVerticalPadding);
        canvas.drawPath(path1, mPaint);
        Path path2 = new Path();
        path2.moveTo(getWidth() / 2, getHeight() / 2 - mWidth / 2);
        path2.addArc(oval, 270, 90);
        path2.lineTo(getWidth() / 2 + mWidth / 2, getHeight() / 2 - mWidth / 2);
        path2.lineTo(getWidth() / 2, getHeight() / 2 - mWidth / 2);
        canvas.drawPath(path2, mPaint);
        Path path3 = new Path();
        path3.moveTo(getWidth() / 2 + mWidth / 2, getHeight() / 2);
        path3.addArc(oval, 360, 90);
        path3.lineTo(getWidth() / 2 + mWidth / 2, getHeight() / 2 + mWidth / 2);
        path3.lineTo(getWidth() / 2 + mWidth / 2, getHeight() / 2);
        canvas.drawPath(path3, mPaint);
        Path path4 = new Path();
        path4.moveTo(getWidth() / 2, getHeight() / 2 + mWidth / 2);
        path4.addArc(oval, 90, 90);
        path4.lineTo(getWidth() / 2 - mWidth / 2, getHeight() / 2 + mWidth / 2);
        path4.lineTo(getWidth() / 2, getHeight() / 2 + mWidth / 2);
        canvas.drawPath(path4, mPaint);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setStyle(Style.STROKE);
        // // 绘制外边框
        // canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
        // - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);
        // 绘制一个圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mWidth / 2, mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        this.invalidate();
    }
}
