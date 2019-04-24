package com.framework.customviews;

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
public class CustomADprogress extends View {

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
    // 当前进度
    private float mProgress;
    private RectF oval;

    public CustomADprogress(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
        initVariable();
        oval = new RectF();
        mTotalProgress = 100;
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomADprogress, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.CustomADprogress_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.CustomADprogress_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.CustomADprogress_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.CustomADprogress_ringColor, 0xFFFFFFFF);
        mbgRingColor = typeArray.getColor(R.styleable.CustomADprogress_bgRingColor, 0xFFFFFFFF);
        mProgress = typeArray.getFloat(R.styleable.CustomADprogress_progress, 0F);
        drawBgCircleRing = typeArray.getBoolean(R.styleable.CustomADprogress_drawBgCircleRing, true);
        text = typeArray.getString(R.styleable.CustomADprogress_text);
        mTextColor = typeArray.getColor(R.styleable.CustomADprogress_textColor, 0xFFFFFFFF);
        textSize = typeArray.getDimension(R.styleable.CustomADprogress_textSize_, 14);

        mProgress = Math.max(Math.min(mProgress, 100F), 0F);

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

            if (!TextUtils.isEmpty(text)) {
                mTxtWidth = mTextPaint.measureText(text, 0, text.length());
                canvas.drawText(text, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
            }
        }
    }

    public void setProgress(float progress) {
        mProgress = progress;
        postInvalidate();
    }

    public void setText(String text) {
        this.text = text;
        postInvalidate();
    }
}
