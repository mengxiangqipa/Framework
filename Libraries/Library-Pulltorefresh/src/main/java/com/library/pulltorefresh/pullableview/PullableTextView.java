package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PullableTextView extends TextView implements Pullable {

    boolean canPullUp = true;

    public PullableTextView(Context context) {
        super(context);
    }

    public PullableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return canPullUp;
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
