package com.framework.customviews.clipview;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class ClipCircleBorderView extends View {
    private int mHorizontalPadding;
    private int mVerticalPadding;
    private int mWidth;
    private int mBorderColor;
    private int mBorderWidth;
    private Paint mPaint;

    public ClipCircleBorderView(Context context) {
        this(context, (AttributeSet)null);
    }

    public ClipCircleBorderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipCircleBorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBorderColor = Color.parseColor("#FFFFFF");
        this.mBorderWidth = 1;
        this.mBorderWidth = (int) TypedValue.applyDimension(1, (float)this.mBorderWidth, this.getResources().getDisplayMetrics());
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
    }

    public void setClipCircleBorderViewWidth(int mWidth, Context context) {
        if(mWidth != 0) {
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            this.mHorizontalPadding = (outMetrics.widthPixels - (int)TypedValue.applyDimension(1, (float)mWidth, this.getResources().getDisplayMetrics())) / 2;
            if(this.mHorizontalPadding <= 0) {
                this.mHorizontalPadding = 0;
            }
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mWidth = this.getWidth() - 2 * this.mHorizontalPadding;
        this.mVerticalPadding = (this.getHeight() - this.mWidth) / 2;
        this.mPaint.setColor(Color.parseColor("#aa000000"));
        this.mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0.0F, 0.0F, (float)this.mHorizontalPadding, (float)this.getHeight(), this.mPaint);
        canvas.drawRect((float)(this.getWidth() - this.mHorizontalPadding), 0.0F, (float)this.getWidth(), (float)this.getHeight(), this.mPaint);
        canvas.drawRect((float)this.mHorizontalPadding, 0.0F, (float)(this.getWidth() - this.mHorizontalPadding), (float)this.mVerticalPadding, this.mPaint);
        canvas.drawRect((float)this.mHorizontalPadding, (float)(this.getHeight() - this.mVerticalPadding), (float)(this.getWidth() - this.mHorizontalPadding), (float)this.getHeight(), this.mPaint);
        RectF oval = new RectF();
        oval.left = (float)this.mHorizontalPadding;
        oval.top = (float)this.mVerticalPadding;
        oval.right = (float)(this.getWidth() - this.mHorizontalPadding);
        oval.bottom = (float)(this.getHeight() - this.mVerticalPadding);
        Path path1 = new Path();
        path1.moveTo((float)this.mHorizontalPadding, (float)this.mVerticalPadding);
        path1.lineTo((float)this.mHorizontalPadding, (float)(this.getHeight() / 2));
        path1.addArc(oval, 180.0F, 90.0F);
        path1.lineTo((float)this.mHorizontalPadding, (float)this.mVerticalPadding);
        canvas.drawPath(path1, this.mPaint);
        Path path2 = new Path();
        path2.moveTo((float)(this.getWidth() / 2), (float)(this.getHeight() / 2 - this.mWidth / 2));
        path2.addArc(oval, 270.0F, 90.0F);
        path2.lineTo((float)(this.getWidth() / 2 + this.mWidth / 2), (float)(this.getHeight() / 2 - this.mWidth / 2));
        path2.lineTo((float)(this.getWidth() / 2), (float)(this.getHeight() / 2 - this.mWidth / 2));
        canvas.drawPath(path2, this.mPaint);
        Path path3 = new Path();
        path3.moveTo((float)(this.getWidth() / 2 + this.mWidth / 2), (float)(this.getHeight() / 2));
        path3.addArc(oval, 360.0F, 90.0F);
        path3.lineTo((float)(this.getWidth() / 2 + this.mWidth / 2), (float)(this.getHeight() / 2 + this.mWidth / 2));
        path3.lineTo((float)(this.getWidth() / 2 + this.mWidth / 2), (float)(this.getHeight() / 2));
        canvas.drawPath(path3, this.mPaint);
        Path path4 = new Path();
        path4.moveTo((float)(this.getWidth() / 2), (float)(this.getHeight() / 2 + this.mWidth / 2));
        path4.addArc(oval, 90.0F, 90.0F);
        path4.lineTo((float)(this.getWidth() / 2 - this.mWidth / 2), (float)(this.getHeight() / 2 + this.mWidth / 2));
        path4.lineTo((float)(this.getWidth() / 2), (float)(this.getHeight() / 2 + this.mWidth / 2));
        canvas.drawPath(path4, this.mPaint);
        this.mPaint.setColor(this.mBorderColor);
        this.mPaint.setStrokeWidth((float)this.mBorderWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float)(this.getWidth() / 2), (float)(this.getHeight() / 2), (float)(this.mWidth / 2), this.mPaint);
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        this.invalidate();
    }
}
