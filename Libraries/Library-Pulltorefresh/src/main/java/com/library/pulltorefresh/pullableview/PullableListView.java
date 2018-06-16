package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author Administrator 加载更多
 */
public class PullableListView extends ListView implements Pullable {

    boolean canPullUp = true;

    public PullableListView(Context context) {
        super(context);
        setOverScrollMode();
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode();
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode();
    }

    private void setOverScrollMode() {
        this.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean canPullDown() {
        try {
            return getCount() == 0 || (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (!canPullUp) {
            return false;
        }
        try {
            if (getCount() == 0) {
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                        && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <=
                        getMeasuredHeight())
                    return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 手动设置是否能上拉
     *
     * @param canPullUp canPullUp
     */
    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }
}