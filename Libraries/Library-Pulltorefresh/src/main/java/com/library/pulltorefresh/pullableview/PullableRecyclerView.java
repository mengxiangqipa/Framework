package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author YobertJomi
 * className PullableRecyclerView
 * created at  2016/12/28  11:21
 */
public class PullableRecyclerView extends RecyclerView implements Pullable {
    private boolean canPullUp = true;
    private boolean canPullDown = true;
    private OnAutoLoadListener onAutoLoadListener;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean onLoading = false;//是否正在加载更多

    public PullableRecyclerView(Context context) {
        super(context);
        setOverScrollMode();
    }

    public PullableRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode();
    }

    public PullableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode();
    }

    private void setOverScrollMode() {
        this.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public boolean canPullDown() {
        //		RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        //		RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return canPullDown && !canScrollVertically(-1);
    }

    @Override
    public boolean canPullUp() {
        //		RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        //		RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        return canPullUp && !canScrollVertically(1);
    }

    /**
     * 手动设置是否能上拉
     *
     * @param canPullUp canPullUp
     */
    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    /**
     * 手动设置是否能下拉
     *
     * @param canPullDown canPullDown
     */
    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    public boolean isHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
    }

    public boolean isOnLoading() {
        return onLoading;
    }

    public void setOnLoading(boolean onLoading) {
        this.onLoading = onLoading;
    }

    public void setOnAutoLoadListener(OnAutoLoadListener onAutoLoadListener) {
        this.onAutoLoadListener = onAutoLoadListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!canScrollVertically(1)) {
            if (null != onAutoLoadListener && hasMoreData && !isOnLoading()) {
                onAutoLoadListener.onAutoLoad(this);
            }
        }
    }

    public interface OnAutoLoadListener {
        /**
         * 自动加载数据的方法
         *
         * @param pullableRecyclerView pullableRecyclerView
         */
        void onAutoLoad(PullableRecyclerView pullableRecyclerView);
    }
}