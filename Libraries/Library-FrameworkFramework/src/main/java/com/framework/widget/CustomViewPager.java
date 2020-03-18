package com.framework.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以阻止viewpager滑动
 *
 * @author YobertJomi
 * className CustomViewPager
 * created at  2017/9/12  10:05
 */
public class CustomViewPager extends ViewPager {

    private boolean enabled = true;//false;//默认不可滑动

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.enabled = true;
    }

    //触摸没有反应就可以了
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            try {
                return super.onTouchEvent(event);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            try {
                return super.onInterceptTouchEvent(event);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
