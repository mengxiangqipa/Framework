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
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.framework.R;

/**
 * 自定义广告进度条
 */
public class SuccessAnimView extends View {

    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画字体的画笔
    private Paint mTextPaint;
    //是否绘制背景圆环
    private boolean drawBgCircleRing;
    //圆环中间文字
    private String text;
    //圆环中间文字颜色
    private int mTextColor;
    //圆环中间文字大小
    private float textSize;
    // 圆形（实心）颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环颜色(底色)
    private int mbgRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 总进度
    private final int mTotalProgress;
    /**
     * 当前进度  100 画圆  180画圆与勾
     */
    private float mProgress;
    private RectF oval;

    public SuccessAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
        oval = new RectF();
        mTotalProgress = 100;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SuccessAnimView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.SuccessAnimView_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.SuccessAnimView_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.SuccessAnimView_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.SuccessAnimView_ringColor, 0xFFFFFFFF);
        mbgRingColor = typeArray.getColor(R.styleable.SuccessAnimView_bgRingColor, 0xFFFFFFFF);
        mProgress = typeArray.getFloat(R.styleable.SuccessAnimView_progress, 0F);
        drawBgCircleRing = typeArray.getBoolean(R.styleable.SuccessAnimView_drawBgCircleRing, true);
        text = typeArray.getString(R.styleable.SuccessAnimView_text);
        mTextColor = typeArray.getColor(R.styleable.SuccessAnimView_textColor, 0xFFFFFFFF);
        textSize = typeArray.getDimension(R.styleable.SuccessAnimView_textSize_, 14);
        typeArray.recycle();
        mProgress = Math.max(Math.min(mProgress, 200F), 0F);

        mRingRadius = mRadius + mStrokeWidth / 2;
    }

    private void initVariable() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
//        mTextPaint.setARGB(255, 255, 255, 255);
        mTextPaint.setColor(mTextColor);
//        mTextPaint.setTextSize(mRadius / 2);
        mTextPaint.setTextSize(textSize);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSize > 0 && heightSize > 0) {
            mXCenter = widthSize / 2;
            mYCenter = widthSize / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        mXCenter = getWidth() / 2;
//        mYCenter = getHeight() / 2;

        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
        if (mProgress >= 0) {
//            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            mRingPaint.setColor(mbgRingColor);
            if (drawBgCircleRing) {
                canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
            }
            mRingPaint.setColor(mRingColor);
            canvas.drawArc(oval, -90, -(mProgress / mTotalProgress) * 360, false, mRingPaint);
            if (mProgress > 100) {
                if (mProgress - 100 <= 50) {
                    canvas.drawLine(mXCenter - mRingRadius / 2, mYCenter,
                            mXCenter - (150 - mProgress) / 50 * mRingRadius / 2,
                            mYCenter + (mProgress - 100) / 50 * mRingRadius / 2, mRingPaint);
                } else if (mProgress - 100 <= 100) {
                    //+-mStrokeWidth/2 补偿
                    canvas.drawLine(mXCenter - mRingRadius / 2, mYCenter,
                            mXCenter + mStrokeWidth * 5 / 14,
                            mYCenter + mRingRadius / 2 + mStrokeWidth * 5 / 14,
                            mRingPaint);
//                    float endX = mXCenter + 2 * mRingRadius / 3;
//                    float endY = mYCenter - mRingRadius / 3;
                    canvas.drawLine(mXCenter, mYCenter + mRingRadius / 2,
                            mXCenter + (mProgress - 150) / 50 * 2 * mRingRadius / 3,
                            mYCenter + mRingRadius / 2 - (mProgress - 150) / 50 * 5 * mRingRadius / 6, mRingPaint);
                }
            }
            if (!TextUtils.isEmpty(text)) {
                mTxtWidth = mTextPaint.measureText(text, 0, text.length());
                canvas.drawText(text, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4,
                        mTextPaint);
            }
        }
    }

    public void refresh(ICallback callback) {
        if (mProgress <= 200) {
            mProgress += 2;
            if (mProgress >= 200 && callback != null) {
                callback.onFinish();
            }
            postInvalidateDelayed(10);
        }
    }

    public interface ICallback {
        void onFinish();
    }

    public void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }
}

