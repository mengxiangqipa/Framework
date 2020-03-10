package com.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framework.R;

/**
 * <pre>
 * 自定义描边textview
 * date 2015-4-15
 * 实现方法是两个TextView叠加,只有描边的TextView为底,实体TextView叠加在上面
 * 看上去文字就有个不同颜色的边框了
 * @author Administrator
 * </pre>
 */
public class CustomStrokeTextView extends androidx.appcompat.widget.AppCompatTextView {
    /**
     * 镂空宽度
     */
    float mStrokeWidth = 1;
    /**
     * 边界颜色
     */
    int mColor = getResources().getColor(R.color.colorCustomStrokeTextView);
    // int mColor=Color.parseColor("#00000000");
    /**
     * 边界textview
     */
    private TextView borderTextView = null;// /用于描边的TextView

    public CustomStrokeTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        borderTextView = new TextView(context);
        initData(mStrokeWidth, mColor);
    }

    public CustomStrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        borderTextView = new TextView(context, attrs);
        initData(mStrokeWidth, mColor);
    }

    public CustomStrokeTextView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        borderTextView = new TextView(context, attrs, defStyle);
        initData(mStrokeWidth, mColor);
    }

    private void initData(float strokeWidth, int color) {
        // TODO Auto-generated method stub
        mColor = color;
        mStrokeWidth = strokeWidth;
        TextPaint textPaint = borderTextView.getPaint();
        textPaint.setStrokeWidth(strokeWidth); // 设置描边宽度
        textPaint.setStyle(Style.STROKE); // 对文字只描边
        borderTextView.setTextColor(color); // 设置描边颜色
        borderTextView.setGravity(getGravity());
    }

    /**
     * <pre>
     * 对外提供设置镂空宽度setStrokeWidth
     * @param strokeWidth
     * </pre>
     */
    public void setBorderStrokeWidth(float strokeWidth) {
        if (null != borderTextView) {
            initData(strokeWidth, mColor);
        }
    }

    /**
     * <pre>
     * 对外提供设置边界颜色 资源id
     * @param colorId
     * </pre>
     */
    public void setBorderColor(int colorId) {
        if (null != borderTextView) {
            initData(mStrokeWidth, colorId);
        }
    }

    /**
     * <pre>
     * 对外提供设置边界颜色 #00000000
     * </pre>
     */
    public void setBorderColor(String color) {
        try {
            if (null != borderTextView) {
                int color_ = Color.parseColor(color);
                initData(mStrokeWidth, color_);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        // MLog.i("setLayoutParams");
        super.setLayoutParams(params);
        borderTextView.setLayoutParams(params);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        try {
            borderTextView.setText(text, type);
        } catch (Exception e) {
        }
        super.setText(text, type);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // MLog.i("onLayout");
        super.onLayout(changed, left, top, right, bottom);
        borderTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // MLog.i("onDraw");
        borderTextView.draw(canvas);
        super.onDraw(canvas);
    }
}
