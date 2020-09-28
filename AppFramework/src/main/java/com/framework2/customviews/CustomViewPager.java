package com.framework2.customviews;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;

/**
 * 自定义一个viewPager，解决在上下拉刷新控件里面滑动冲突
 *
 * @author Yangjie
 * className CustomViewPager
 * created at  2017/1/19  16:29
 */
public class CustomViewPager extends ViewPager {
    private BaseAbstractPullToRefreshLayout layout;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewPager(Context context) {
        super(context);
    }

    public void setLayout(@NonNull BaseAbstractPullToRefreshLayout layout) {
        this.layout = layout;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (null != layout) {
                    layout.requestFirstTouch(true);
                    super.onTouchEvent(ev);
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (null != layout) {
                    layout.requestFirstTouch(true);
                    super.onTouchEvent(ev);
                    return true;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (null != layout) {
                    layout.requestFirstTouch(false);
                    super.onTouchEvent(ev);
                    return false;
                }
        }
        return super.onTouchEvent(ev);
    }
}
