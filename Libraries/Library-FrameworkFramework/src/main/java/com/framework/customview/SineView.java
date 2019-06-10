package com.framework.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 正余弦view
 *
 * @author YobertJomi
 * className SineView
 * created at  2019/3/6  18:01
 */
public class SineView extends View {
    private Paint paintTop;
    private Paint paintBottom;
    private Path pathTop;
    private Path pathBottom;
    private DrawDirection drawDirection = DrawDirection.RIGHT;
    private DrawLocation drawLocation = DrawLocation.ONLY_TOP;
    private float width;
    private float height;
    //设置的左右边距
    private float margin;

    //绘制方向，从左到右或者从右到左
    public enum DrawDirection {
        LEFT, RIGHT
    }

    //绘制位置，只绘制顶部，只绘制底部，BOTH
    public enum DrawLocation {
        ONLY_TOP, ONLY_BOTTOM, BOTH
    }

    public SineView(Context context) {
        super(context);
        init();
    }

    public SineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintTop = new Paint();
        paintTop.setColor(Color.BLACK);
        paintTop.setAntiAlias(true);
        paintTop.setStrokeWidth(4);
        paintTop.setStyle(Paint.Style.STROKE);
        //绘制长度为4的实线后再绘制长度为4的空白区域，0位间隔
        paintTop.setPathEffect(new DashPathEffect(new float[]{8, 8}, 0));

        paintBottom = new Paint();
        paintBottom.setColor(Color.BLACK);
        paintBottom.setAntiAlias(true);
        paintBottom.setStrokeWidth(4);
        paintBottom.setStyle(Paint.Style.STROKE);
        //绘制长度为4的实线后再绘制长度为4的空白区域，1位间隔
        //这个画笔设置一位间隔
        paintBottom.setPathEffect(new DashPathEffect(new float[]{8, 8}, 1));

        pathTop = new Path();
        pathBottom = new Path();
    }

    /**
     * 比onDraw先执行
     * <p>
     * 一个MeasureSpec封装了父布局传递给子布局的布局要求，每个MeasureSpec代表了一组宽度和高度的要求。
     * 一个MeasureSpec由大小和模式组成
     * 它有三种模式：UNSPECIFIED(未指定),父元素部队自元素施加任何束缚，子元素可以得到任意想要的大小;
     * EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；
     * AT_MOST(至多)，子元素至多达到指定大小的值。
     * <p>
     * 它常用的三个函数：
     * 1.static int getMode(int measureSpec):根据提供的测量值(格式)提取模式(上述三个模式之一)
     * 2.static int getSize(int measureSpec):根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)
     * 3.static int makeMeasureSpec(int size,int mode):根据提供的大小值和模式创建一个测量值(格式)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

//        final int minimumWidth = getSuggestedMinimumWidth();
//        final int minimumHeight = getSuggestedMinimumHeight();
//        setMeasuredDimension(minimumWidth, minimumHeight);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int width = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
//        int height = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        if (widthSize > 0 && heightSize > 0) {
            width = widthSize;
            height = heightSize;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        drawTest(canvas, pathTop, paintTop);

        //从左边边开始绘制
        if (null != drawDirection && drawDirection == DrawDirection.LEFT) {
            if (drawLocation == DrawLocation.ONLY_TOP) {
                drawTopLeft(canvas, pathTop, paintTop);
            } else if (drawLocation == DrawLocation.ONLY_BOTTOM) {
                drawBottomLeft(canvas, pathBottom, paintBottom);
            } else {
                drawTopLeft(canvas, pathTop, paintTop);
                drawBottomLeft(canvas, pathBottom, paintBottom);
            }
        } else {
            //从右边开始绘制
            if (drawLocation == DrawLocation.ONLY_TOP) {
                drawTopRight(canvas, pathTop, paintTop);
            } else if (drawLocation == DrawLocation.ONLY_BOTTOM) {
                drawBottomRight(canvas, pathBottom, paintBottom);
            } else {
                drawTopRight(canvas, pathTop, paintTop);
                drawBottomRight(canvas, pathBottom, paintBottom);
            }
        }
    }

    private void drawTest(Canvas canvas, Path pathTop, Paint paintTop) {
//        canvas.translate(0,-height/2);
        pathTop.reset();
        //起始位置
        pathTop.moveTo(width / 2, height);
//        pathTop.moveTo(width / 3, height );
        //rQuardto的位置是相对的

        //测试可用
//        pathTop.rQuadTo(100, 100, 200, 0);
//        pathTop.rQuadTo(100, -100, 200, 0);
//        canvas.drawPath(pathTop, paintTop);
//        canvas.drawPoint(width / 2, height / 2,paintBottom);
//        canvas.drawPoint(width / 2+100, height / 2+100,paintBottom);
//        canvas.drawPoint(width / 2+200, height / 2,paintBottom);
//        canvas.drawPoint(width / 2+200+100, height / 2-100,paintBottom);
//        canvas.drawPoint(width / 2+200+200, height / 2,paintBottom);
//        canvas.drawLine(width / 2, height / 2+50, width / 2+750, height / 2+50, paintBottom);

//        canvas.clipRect(width/2, height/2, width, height, Region.Op.DIFFERENCE);
        canvas.clipRect(width / 2, height / 2, width, height);
        canvas.drawCircle(width / 2, height / 2, 50, paintTop);
//        pathTop.rQuadTo(200, -100, 0, 200);
//        pathTop.rQuadTo(-200, -100, 0, 200);
        pathTop.rQuadTo(width, -height / 2, 0, -height);
//        pathTop.rQuadTo(-width / 2, -height / 2, 0, -height / 2);
        canvas.drawPath(pathTop, paintTop);
//        canvas.translate(0,height/2);
    }

    /**
     * 绘制顶部
     */
    private void drawTopLeft(Canvas canvas, Path path, Paint paint) {
        canvas.save();
        //起始位置
        path.reset();
        path.moveTo(width / 2, 0);
        //rQuardto的位置是相对的
        canvas.clipRect(0, 0, width / 2, height / 2);
        path.rQuadTo(-width + 2 * margin, height / 2, 0, height);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void drawBottomLeft(Canvas canvas, Path path, Paint paint) {
        canvas.save();
        //起始位置
        path.reset();
        path.moveTo(width / 2, height);
        //rQuardto的位置是相对的
        canvas.clipRect(0, height / 2, width / 2, height);
        path.rQuadTo(-width + 2 * margin, -height / 2, 0, -height);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void drawTopRight(Canvas canvas, Path path, Paint paint) {
        canvas.save();
        //起始位置
        path.reset();
        path.moveTo(width / 2, 0);
        //rQuardto的位置是相对的
        canvas.clipRect(width / 2, 0, width, height / 2);
        path.rQuadTo(width - 2 * margin, height / 2, 0, height);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private void drawBottomRight(Canvas canvas, Path path, Paint paint) {
        canvas.save();
        //起始位置
        path.reset();
        path.moveTo(width / 2, height);
        //rQuardto的位置是相对的
        canvas.clipRect(width / 2, height / 2, width, height);
        path.rQuadTo(width - 2 * margin, -height / 2, 0, -height);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    public void setDrawDirection(DrawDirection drawDirection) {
        this.drawDirection = drawDirection;
    }

    public void setDrawLocation(DrawLocation drawLocation) {
        this.drawLocation = drawLocation;
    }

    public void setPaintColorTop(int paintColorTop) {
        try {
            paintTop.setColor(paintColorTop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPaintColorBottom(int paintColorBottom) {
        try {
            paintBottom.setColor(paintColorBottom);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMargin(float marginPx) {
        this.margin = marginPx;
    }
}
