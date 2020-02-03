/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;

import com.framework.util.ScreenUtil;
import com.google.zxing.ResultPoint;
import com.library.scan.R;
import com.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;

    /**
     * 四个绿色边角对应的长度
     */
    private int cornerLenth;

    /**
     * 四个绿色边角对应的宽度
     */
    private int CORNER_WIDTH = 5;
    /**
     * 扫描框中的中间线的宽度
     */
    private static final int MIDDLE_LINE_WIDTH = 6;

    /**
     * 扫描框中的中间线的与扫描框左右的间隙
     */
    private static final int MIDDLE_LINE_PADDING = 5;

    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 2;//修改5->2

    /**
     * 手机的屏幕密度
     */
    private static float density;
    /**
     * 字体大小
     */
    private static final int TEXT_SIZE = 14;
    /**
     * 字体距离扫描框下面的距离
     */
    private static final int TEXT_PADDING_TOP = 30;

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;

    /**
     * 中间滑动线的最底端位置
     */
    private int slideBottom;

    /**
     * 将扫描的二维码拍下来，这里没有这个功能，暂时不考虑
     */
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int textColor;
    private final int cornerColor;

    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    private Rect lineRect = new Rect();//中间横线
    private Rect rectTemp;//缓存取景框外框
    private boolean isFirst;
    private boolean showRandomCircle;//是否显示闪亮小圆点
    //获取屏幕的宽和高
    private int width;
    private int height;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        //将像素转换成dp
        cornerLenth = (int) (20 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        textColor = resources.getColor(R.color.txt);
        cornerColor = resources.getColor(R.color.viewfinder_corner);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    /**
     * 设置四个角的长宽
     *
     * @param cornerLenthPx 取景框四个角的长宽 像素点
     */
    public void setCornerLenth(@IntRange(from = 20, to = 1000) int cornerLenthPx) {
        this.cornerLenth = cornerLenthPx;
    }

    /**
     * 设置四个角的厚度
     *
     * @param cornerThicknessPx 取景框四个角的厚度 像素点
     */
    public void setCornerThickness(@IntRange(from = 20, to = 1000) int cornerThicknessPx) {
        this.CORNER_WIDTH = cornerThicknessPx;
    }

    /**
     * 设置是否显示随机小圆点
     *
     * @param showRandomCircle 是否显示随机小圆点
     */
    public void setShowRandomCircle(boolean showRandomCircle) {
        this.showRandomCircle = showRandomCircle;
    }

    /**
     * 绘制外部遮蔽层
     */
    private void drawOuterView(Canvas canvas, Rect frame) {
        if (frame == null) {
            return;
        }
        //画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
        //扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (null != CameraManager.get()) {
            //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
            //CameraManager.get().setRectInPreview(framingRect);
            Rect frame = CameraManager.get().getFramingRect();
//            Rect frame = CameraManager.get().getFramingRectInPreview();
            if (frame == null) {
                drawOuterView(canvas, rectTemp);
                return;
            }

            //初始化中间线滑动的最上边和最下边
            if (!isFirst) {
                isFirst = true;
                slideTop = frame.top;
                slideBottom = frame.bottom;
                if (rectTemp == null) {
                    rectTemp = new Rect(frame);
                }
                //获取屏幕的宽和高
                width = canvas.getWidth();
                height = canvas.getHeight();
            }

            paint.setColor(resultBitmap != null ? resultColor : maskColor);

            drawOuterView(canvas, frame);

            if (resultBitmap != null) {
                // Draw the opaque result bitmap over the scanning rectangle
                paint.setAlpha(OPAQUE);
                canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
            } else {
                //画扫描框边上的角，总共8个部分
                paint.setColor(cornerColor);
                canvas.drawRect(frame.left, frame.top, frame.left + cornerLenth,
                        frame.top + CORNER_WIDTH, paint);
                canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                        + cornerLenth, paint);
                canvas.drawRect(frame.right - cornerLenth, frame.top, frame.right,
                        frame.top + CORNER_WIDTH, paint);
                canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                        + cornerLenth, paint);
                canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                        + cornerLenth, frame.bottom, paint);
                canvas.drawRect(frame.left, frame.bottom - cornerLenth,
                        frame.left + CORNER_WIDTH, frame.bottom, paint);
                canvas.drawRect(frame.right - cornerLenth, frame.bottom - CORNER_WIDTH,
                        frame.right, frame.bottom, paint);
                canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - cornerLenth,
                        frame.right, frame.bottom, paint);

                //绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
                slideTop += SPEEN_DISTANCE;
                if (slideTop >= frame.bottom - 4) {//修改 slideTop >= frame.bottom
                    slideTop = frame.top;
                }
//			canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame.right -
//			MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2, paint);

//                lineRect.left = frame.left;
//                lineRect.right = frame.right;
//                lineRect.top = slideTop;
//                lineRect.bottom = slideTop + 18;
//                canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.mipmap.qrcode_scan_line_test)))
//                .getBitmap(), null, lineRect, paint);
                lineRect.left = frame.left + 20;
                lineRect.right = frame.right - 20;
                lineRect.top = slideTop;
                lineRect.bottom = slideTop + 4;
                canvas.drawBitmap(((BitmapDrawable) (getResources().getDrawable(R.mipmap.qrcode_scan_line_test1))).getBitmap(), null, lineRect, paint);

                //画扫描框下面的字
//			paint.setColor(Color.WHITE);
//			paint.setTextSize(TEXT_SIZE * density);
//			paint.setAlpha(0x40);
//			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
//			canvas.drawText(getResources().getString(R.string.scan_text), frame.left, (float) (frame.bottom + (float)
//			TEXT_PADDING_TOP *density), paint);

                paint.setColor(textColor);
                paint.setTextSize(TEXT_SIZE * density);
//                paint.setAlpha(0x40);
//                paint.setTypeface(Typeface.DEFAULT_BOLD);
                // 这里取消掉了 绘制文字
                String text = getResources().getString(R.string.scan_text);
                float textWidth = paint.measureText(text);
//                canvas.drawText(text, (width - textWidth) / 2, (frame.bottom + (float) TEXT_PADDING_TOP * density),
//                paint);
//                canvas.drawText(text, frame.left+((frame.right- frame.left)- textWidth) / 2, (frame.bottom +
//                (float) TEXT_PADDING_TOP * density), paint);
                canvas.drawText(text, (width - textWidth) / 2,
                        (frame.bottom + (float) ScreenUtil.getInstance().dip2px(getContext(), 30)), paint);

                //绘制小圆点
                if (showRandomCircle) {
                    Collection<ResultPoint> currentPossible = possibleResultPoints;
                    Collection<ResultPoint> currentLast = lastPossibleResultPoints;
                    if (currentPossible.isEmpty()) {
                        lastPossibleResultPoints = null;
                    } else {
                        possibleResultPoints = new HashSet<ResultPoint>(5);
                        lastPossibleResultPoints = currentPossible;
                        paint.setAlpha(OPAQUE);
                        paint.setColor(resultPointColor);
                        for (ResultPoint point : currentPossible) {
                            canvas.drawCircle(frame.left + point.getX(), frame.top
                                    + point.getY(), 6.0f, paint);
                        }
                    }
                    if (currentLast != null) {
                        paint.setAlpha(OPAQUE / 2);
                        paint.setColor(resultPointColor);
                        for (ResultPoint point : currentLast) {
                            canvas.drawCircle(frame.left + point.getX(), frame.top
                                    + point.getY(), 3.0f, paint);
                        }
                    }
                }

                if (showRandomCircle)
                    //只刷新扫描框的内容，其他地方不刷新
                    postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                            frame.right, frame.bottom);
            }
        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }
}
