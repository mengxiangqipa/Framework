/*
 *  Copyright (c) 2019 YobertJomi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.framework.R;

/**
 * 人脸识别的遮罩
 */
public class FaceShadeView extends View {

    /**
     * 背景画笔
     */
    private Paint bgPaint;
    private Paint mPaintTemp;

    /**
     * 透明路径
     */
    private Path transPath;

    private PorterDuffXfermode mode;
    /**
     * 圆的半径
     */
    private int mRadius;
    /**
     * 圆环宽度
     */
    private int thicknessWidth;
    /**
     * 圆环颜色
     */
    private int ringColor;
    /**
     * 距离顶部的高度
     */
    private int marginTop;

    /**
     * 内圆环厚度
     */
    private int mBorderThicknessInside = 0;
    /**
     * 内圆颜色
     */
    private int mBorderInsideColor = Color.parseColor("#ffffff");

    public FaceShadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        transPath = new Path();
        transPath.addRect(100, 100, 200, 200, Path.Direction.CW);
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.parseColor("#ffffff"));

        mPaintTemp = new Paint();
        mPaintTemp.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FaceShadeView);
        mRadius = a.getDimensionPixelSize(
                R.styleable.FaceShadeView_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dp2px(50), getResources().getDisplayMetrics()));// 默认为50dp
        marginTop = a.getDimensionPixelSize(
                R.styleable.FaceShadeView_marginTop,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        0, getResources().getDisplayMetrics()));// 默认为0dp
        ringColor = a.getColor(R.styleable.FaceShadeView_ringColor,
                getResources().getColor(android.R.color.black));
        thicknessWidth = a.getDimensionPixelSize(R.styleable.FaceShadeView_thicknessWidth,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                        getResources().getDisplayMetrics()));
        // 默认为0dp
        a.recycle();
    }

    public FaceShadeView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
//        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), bgPaint, Canvas
//        .ALL_SAVE_FLAG);
        // draw内圆环
        mPaintTemp.setColor(mBorderInsideColor);
        bgPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), bgPaint);
        bgPaint.setColor(ringColor);
        canvas.drawCircle(getWidth() / 2, marginTop + mRadius, mRadius, bgPaint);//有色圆圈
        //设置混合模式
        bgPaint.setXfermode(mode);
        canvas.drawCircle(getWidth() / 2, marginTop + mRadius, mRadius - thicknessWidth, bgPaint);//透明圆圈
//        canvas.drawCircle(mRadius, mRadius, mRadius, mPaintTemp);
//        canvas.drawCircle(mRadius, mRadius, mRadius - mBorderThicknessInside, mBitmapPaint);
        //清除混合模式
        bgPaint.setXfermode(null);
        //还原画布
//        canvas.restoreToCount(saveCount);
    }

    public void setBorderThicknessInside(int mBorderThicknessInside) {
        int pxVal = dp2px(mBorderThicknessInside);
        if (this.mBorderThicknessInside != pxVal) {
            this.mBorderThicknessInside = pxVal;
            invalidate();
        }
    }

    /**
     * @param ringColor 圆环颜色
     */
    public void setRingColor(int ringColor) {
        this.ringColor = ringColor;
        invalidate();
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}