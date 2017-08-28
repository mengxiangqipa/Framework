package com.framework.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.framework.R;


/**
 * date 2015-7-27
 *
 * @author Administrator
 *         http://blog.csdn.net/lmj623565791/article/details/41967509
 */
public class CustomRoundImageView_new extends AppCompatImageView {
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;
    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";
    private static final String STATE_BORDER_ThicknessInside = "state_border_ThicknessInside";
    private static final String STATE_BORDER_InsideColor = "state_border_InsideColor";
    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;
    /**
     * 绘图的Paint
     */
    private Paint mBitmapPaint;
    private Paint mPaintTemp;
    /**
     * 圆角的半径
     */
    private int mRadius;
    // 如果只有其中一个有值，则只画一个圆形边框
    /**
     * 3x3 矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染图像，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的宽度
     */
    private int mWidth;
    private RectF mRoundRect;
    /**
     * 内圆环厚度
     */
    private int mBorderThicknessInside = 0;
    /**
     * 内圆颜色
     */
    private int mBorderInsideColor = Color.parseColor("#ffffff");

    public CustomRoundImageView_new(Context context, AttributeSet attrs) {

        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mPaintTemp = new Paint();
        mPaintTemp.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomRoundImageView_new);

        mBorderRadius = a.getDimensionPixelSize(
                R.styleable.CustomRoundImageView_new_borderRadius_new,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        BODER_RADIUS_DEFAULT, getResources()
                                .getDisplayMetrics()));// 默认为10dp
        type = a.getInt(R.styleable.CustomRoundImageView_new_type_new,
                TYPE_CIRCLE);// 默认为Circle
        // 以下自己加的
        mBorderThicknessInside = a.getDimensionPixelSize(
                R.styleable.CustomRoundImageView_new_mBorderThicknessInside, 0);
        mBorderInsideColor = a.getColor(
                R.styleable.CustomRoundImageView_new_mBorderInsideColor,
                Color.parseColor("#ffffff"));
        a.recycle();
    }

    public CustomRoundImageView_new(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable, type);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, TileMode.CLAMP, TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_ROUND) {
            if (!(bmp.getWidth() == getWidth() && bmp.getHeight() == getHeight())) {
                // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
                scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
                        getHeight() * 1.0f / bmp.getHeight());
            }

        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mBitmapPaint.setShader(mBitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else {
            // drawSomeThing(canvas);
            if (mBorderThicknessInside <= 0) {
                // draw正常bitmap
                canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
            } else {
                // draw内圆环
                mPaintTemp.setColor(mBorderInsideColor);
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaintTemp);
                canvas.drawCircle(mRadius, mRadius, mRadius
                        - mBorderThicknessInside, mBitmapPaint);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // 圆角图片的范围
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, w, h);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable, int type) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            //IF判断是自己加的，圆形居中
            if (type == TYPE_CIRCLE) {
                Bitmap bit = bd.getBitmap();
//                com.my.utils.Y.y("drawableToBitamp1");
//                com.my.utils.Y.y("bit.getWidth()"+bit.getWidth());
//                com.my.utils.Y.y("bit.getHeight()"+bit.getHeight());
//                com.my.utils.Y.y("getMeasuredWidth:"+getMeasuredWidth());
//                com.my.utils.Y.y("getMeasuredHeight:"+getMeasuredHeight());
                return Bitmap.createBitmap(bit, (bit.getWidth() - bit.getHeight())>0? Math.abs(bit.getWidth() - bit.getHeight()) / 2:0,(bit.getWidth() - bit.getHeight())<0? Math.abs(bit.getWidth() - bit.getHeight()) / 2:0, Math.min(bit.getWidth(),bit.getHeight()), Math.min(bit.getWidth(), bit.getHeight()));
            }
//            com.my.utils.Y.y("drawableToBitamp2");
            return bd.getBitmap();
        }
//        com.my.utils.Y.y("drawableToBitamp3");
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        bundle.putInt(STATE_BORDER_ThicknessInside, mBorderThicknessInside);
        bundle.putInt(STATE_BORDER_InsideColor, mBorderInsideColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
            this.mBorderThicknessInside = bundle
                    .getInt(STATE_BORDER_ThicknessInside);
            this.mBorderInsideColor = bundle.getInt(STATE_BORDER_InsideColor);
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

}