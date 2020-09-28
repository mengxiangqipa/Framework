package com.framework.widget.clipview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;

public class ClipZoomImageView extends AppCompatImageView implements OnScaleGestureListener,
        OnTouchListener,
        OnGlobalLayoutListener {
    private static final String TAG = ClipZoomImageView.class.getSimpleName();
    public static float SCALE_MAX = 4.0F;
    private static float SCALE_MID = 2.0F;
    private final float[] matrixValues;
    private final Matrix mScaleMatrix;
    private float initScale;
    private boolean once;
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;
    private int mTouchSlop;
    private float mLastX;
    private float mLastY;
    private boolean isCanDrag;
    private int lastPointerCount;
    private int mHorizontalPadding;
    private int mVerticalPadding;

    public ClipZoomImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ClipZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initScale = 1.0F;
        this.once = true;
        this.matrixValues = new float[9];
        this.mScaleGestureDetector = null;
        this.mScaleMatrix = new Matrix();
        this.setScaleType(ScaleType.MATRIX);
        this.mGestureDetector = new GestureDetector(context, new SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                if (ClipZoomImageView.this.isAutoScale) {
                    return true;
                } else {
                    float x = e.getX();
                    float y = e.getY();
                    if (ClipZoomImageView.this.getScale() < ClipZoomImageView.SCALE_MID) {
                        ClipZoomImageView.this.postDelayed(ClipZoomImageView.this.new AutoScaleRunnable
                                (ClipZoomImageView.SCALE_MID, x, y), 16L);
                        ClipZoomImageView.this.isAutoScale = true;
                    } else {
                        ClipZoomImageView.this.postDelayed(ClipZoomImageView.this.new AutoScaleRunnable
                                (ClipZoomImageView.this.initScale, x, y), 16L);
                        ClipZoomImageView.this.isAutoScale = true;
                    }

                    return true;
                }
            }
        });
        this.mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);
    }

    public boolean onScale(ScaleGestureDetector detector) {
        float scale = this.getScale();
        float scaleFactor = detector.getScaleFactor();
        if (this.getDrawable() == null) {
            return true;
        } else {
            if (scale < SCALE_MAX && scaleFactor > 1.0F || scale > this.initScale && scaleFactor < 1.0F) {
                if (scaleFactor * scale < this.initScale) {
                    scaleFactor = this.initScale / scale;
                }

                if (scaleFactor * scale > SCALE_MAX) {
                    scaleFactor = SCALE_MAX / scale;
                }

                this.mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                        detector.getFocusY());
                this.checkBorder();
                this.setImageMatrix(this.mScaleMatrix);
            }

            return true;
        }
    }

    private RectF getMatrixRectF() {
        Matrix matrix = this.mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = this.getDrawable();
        if (d != null) {
            rect.set(0.0F, 0.0F, (float) d.getIntrinsicWidth(), (float) d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }

        return rect;
    }

    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    public void onScaleEnd(ScaleGestureDetector detector) {
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (this.mGestureDetector.onTouchEvent(event)) {
            return true;
        } else {
            this.mScaleGestureDetector.onTouchEvent(event);
            float x = 0.0F;
            float y = 0.0F;
            int pointerCount = event.getPointerCount();

            for (int dx = 0; dx < pointerCount; ++dx) {
                x += event.getX(dx);
                y += event.getY(dx);
            }

            x /= (float) pointerCount;
            y /= (float) pointerCount;
            if (pointerCount != this.lastPointerCount) {
                this.isCanDrag = false;
                this.mLastX = x;
                this.mLastY = y;
            }

            this.lastPointerCount = pointerCount;
            switch (event.getAction()) {
                case 1:
                case 3:
                    this.lastPointerCount = 0;
                    break;
                case 2:
                    float var9 = x - this.mLastX;
                    float dy = y - this.mLastY;
                    if (!this.isCanDrag) {
                        this.isCanDrag = this.isCanDrag(var9, dy);
                    }

                    if (this.isCanDrag && this.getDrawable() != null) {
                        RectF rectF = this.getMatrixRectF();
                        if (rectF.width() <= (float) (this.getWidth() - this.mHorizontalPadding * 2)) {
                            var9 = 0.0F;
                        }

                        if (rectF.height() <= (float) (this.getHeight() - this.mVerticalPadding * 2)) {
                            dy = 0.0F;
                        }

                        this.mScaleMatrix.postTranslate(var9, dy);
                        this.checkBorder();
                        this.setImageMatrix(this.mScaleMatrix);
                    }

                    this.mLastX = x;
                    this.mLastY = y;
            }

            return true;
        }
    }

    public final float getScale() {
        this.mScaleMatrix.getValues(this.matrixValues);
        return this.matrixValues[0];
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    public void onGlobalLayout() {
        if (this.once) {
            Drawable d = this.getDrawable();
            if (d == null) {
                return;
            }

            this.mVerticalPadding =
                    (this.getHeight() - (this.getWidth() - 2 * this.mHorizontalPadding)) / 2;
            int width = this.getWidth();
            int height = this.getHeight();
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0F;
            if (dw < this.getWidth() - this.mHorizontalPadding * 2 && dh > this.getHeight() - this.mVerticalPadding *
                    2) {
                scale = ((float) this.getWidth() * 1.0F - (float) (this.mHorizontalPadding * 2)) / (float) dw;
            }

            if (dh < this.getHeight() - this.mVerticalPadding * 2 && dw > this.getWidth() - this.mHorizontalPadding *
                    2) {
                scale = ((float) this.getHeight() * 1.0F - (float) (this.mVerticalPadding * 2)) / (float) dh;
            }

            if (dw < this.getWidth() - this.mHorizontalPadding * 2 && dh < this.getHeight() - this.mVerticalPadding *
                    2) {
                float scaleW =
                        ((float) this.getWidth() * 1.0F - (float) (this.mHorizontalPadding * 2)) / (float) dw;
                float scaleH =
                        ((float) this.getHeight() * 1.0F - (float) (this.mVerticalPadding * 2)) / (float) dh;
                scale = Math.max(scaleW, scaleH);
            }

            this.initScale = scale;
            SCALE_MID = this.initScale * 2.0F;
            SCALE_MAX = this.initScale * 4.0F;
            this.mScaleMatrix.postTranslate((float) ((width - dw) / 2),
                    (float) ((height - dh) / 2));
            this.mScaleMatrix.postScale(scale, scale, (float) (this.getWidth() / 2),
                    (float) (this.getHeight() / 2));
            this.setImageMatrix(this.mScaleMatrix);
            this.once = false;
        }
    }

    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return Bitmap.createBitmap(bitmap, this.mHorizontalPadding, this.mVerticalPadding,
                this.getWidth() - 2 * this
                .mHorizontalPadding, this.getWidth() - 2 * this.mHorizontalPadding);
    }

    private void checkBorder() {
        RectF rect = this.getMatrixRectF();
        float deltaX = 0.0F;
        float deltaY = 0.0F;
        int width = this.getWidth();
        int height = this.getHeight();
        Log.e(TAG,
                "rect.width() =  " + rect.width() + " , width - 2 * mHorizontalPadding =" + (width - 2 * this
                .mHorizontalPadding));
        if ((double) rect.width() + 0.01D >= (double) (width - 2 * this.mHorizontalPadding)) {
            if (rect.left > (float) this.mHorizontalPadding) {
                deltaX = -rect.left + (float) this.mHorizontalPadding;
            }

            if (rect.right < (float) (width - this.mHorizontalPadding)) {
                deltaX = (float) (width - this.mHorizontalPadding) - rect.right;
            }
        }

        if ((double) rect.height() + 0.01D >= (double) (height - 2 * this.mVerticalPadding)) {
            if (rect.top > (float) this.mVerticalPadding) {
                deltaY = -rect.top + (float) this.mVerticalPadding;
            }

            if (rect.bottom < (float) (height - this.mVerticalPadding)) {
                deltaY = (float) (height - this.mVerticalPadding) - rect.bottom;
            }
        }

        this.mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((double) (dx * dx + dy * dy)) >= (double) this.mTouchSlop;
    }

    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public void setClipZoomImageViewWidth(int mWidth, Context context) {
        if (mWidth != 0) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            this.mHorizontalPadding = (outMetrics.widthPixels - (int) TypedValue.applyDimension(1
                    , (float) mWidth,
                    this.getResources().getDisplayMetrics())) / 2;
            if (this.mHorizontalPadding <= 0) {
                this.mHorizontalPadding = 0;
            }
        }
    }

    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07F;
        static final float SMALLER = 0.93F;
        private float mTargetScale;
        private float tmpScale;
        private float x;
        private float y;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (ClipZoomImageView.this.getScale() < this.mTargetScale) {
                this.tmpScale = 1.07F;
            } else {
                this.tmpScale = 0.93F;
            }
        }

        public void run() {
            ClipZoomImageView.this.mScaleMatrix.postScale(this.tmpScale, this.tmpScale, this.x,
                    this.y);
            ClipZoomImageView.this.checkBorder();
            ClipZoomImageView.this.setImageMatrix(ClipZoomImageView.this.mScaleMatrix);
            float currentScale = ClipZoomImageView.this.getScale();
            if ((this.tmpScale <= 1.0F || currentScale >= this.mTargetScale) && (this.tmpScale >= 1.0F || this
                    .mTargetScale >= currentScale)) {
                float deltaScale = this.mTargetScale / currentScale;
                ClipZoomImageView.this.mScaleMatrix.postScale(deltaScale, deltaScale, this.x,
                        this.y);
                ClipZoomImageView.this.checkBorder();
                ClipZoomImageView.this.setImageMatrix(ClipZoomImageView.this.mScaleMatrix);
                ClipZoomImageView.this.isAutoScale = false;
            } else {
                ClipZoomImageView.this.postDelayed(this, 16L);
            }
        }
    }
}