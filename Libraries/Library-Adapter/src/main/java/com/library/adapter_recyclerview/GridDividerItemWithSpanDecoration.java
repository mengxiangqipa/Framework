package com.library.adapter_recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 有做header，footer的判断、 保持左右边距
 *
 * @author YobertJomi
 * className GridDividerItemDecoration2
 * created at  2017/1/16  12:00
 */

public class GridDividerItemWithSpanDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private boolean canDraw = true;
    private boolean drawFirstLine = true;
    private boolean drawLastLine = true;

    public GridDividerItemWithSpanDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public void setDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setDrawFirstLine(boolean drawFirstLine) {
        this.drawFirstLine = drawFirstLine;
    }

    public void setDrawLastLine(boolean drawLastLine) {
        this.drawLastLine = drawLastLine;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (canDraw) {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = 1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
            if (adapter.isHeader(position) || adapter.isFooter(position)) {//过滤header，footer
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            final int topFirstLaw = child.getTop() - params.topMargin - mDivider.getIntrinsicHeight();//第一行top
            final int bottomFirstLaw = child.getTop() - params.topMargin;//第一行bottom
            int spanCount = getSpanCount(parent);
            if (position - adapter.getHeaderCount() < spanCount && drawFirstLine) {//第一行顶部
                if ((position - adapter.getHeaderCount()) % spanCount == 0) {
                    mDivider.setBounds(left - mDivider.getIntrinsicWidth(), topFirstLaw, right - mDivider
                            .getIntrinsicWidth() / 2, bottomFirstLaw);
                } else if ((position - adapter.getHeaderCount()) % spanCount == (spanCount - 1)) {//列最后一项
                    mDivider.setBounds(left - mDivider.getIntrinsicWidth() / 2, topFirstLaw, right, bottomFirstLaw);
                } else {
                    mDivider.setBounds(left - mDivider.getIntrinsicWidth() / 2, topFirstLaw, right - mDivider
                            .getIntrinsicWidth() / 2, bottomFirstLaw);
                }
                mDivider.draw(c);
            }
            //其他
            if ((position - adapter.getHeaderCount()) % spanCount == 0) {//列第一项
                mDivider.setBounds(left - mDivider.getIntrinsicWidth(), top, right - mDivider.getIntrinsicWidth() /
                        2, bottom);
            } else if ((position - adapter.getHeaderCount()) % spanCount == (spanCount - 1)) {//列最后一项
                mDivider.setBounds(left - mDivider.getIntrinsicWidth() / 2, top, right, bottom);
            } else {
                mDivider.setBounds(left - mDivider.getIntrinsicWidth() / 2, top, right - mDivider.getIntrinsicWidth()
                        / 2, bottom);
            }
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
            if (adapter.isHeader(position) || adapter.isFooter(position)) {//过滤header，footer
                continue;
            }
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            int spanCount = getSpanCount(parent);
            if ((position - adapter.getHeaderCount()) % spanCount == 0) {
                mDivider.setBounds(0, top, right - left, bottom);
                mDivider.draw(c);
            }
            //其他
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastRow(RecyclerView parent, @IntRange(from = 0) int itemPosition, int spanCount, int
            childCount) {
        UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是最后一行，则不需要绘制底部,这个是考虑resizeSpan的情况
            return (itemPosition - adapter.getHeaderCount() >= adapter.getDataList().size() - 1 - (
                    (GridLayoutManager) parent
                            .getLayoutManager()).getSpanSizeLookup().getSpanIndex(adapter.getDataList().size() - 1,
                    spanCount));
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (itemPosition >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((itemPosition + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (parent.getAdapter() instanceof UniversalAdapter) {
            UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
            if (null != adapter) {
                int spanCount = getSpanCount(parent);
                int childCount = adapter.getDataItemCount();
                boolean isLastRaw = isLastRow(parent, itemPosition, spanCount, childCount);
                int bottom = drawLastLine && isLastRaw ? mDivider.getIntrinsicHeight() : 0;
                if (adapter.isHeader(itemPosition) || adapter.isFooter(itemPosition)) {
                    outRect.set(0, 0, 0, 0);
                } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = ((GridLayoutManager) parent
                            .getLayoutManager());
                    int currentSpanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(itemPosition, spanCount);
                    int nextSpanIndex = -1;
                    if (itemPosition - adapter.getHeaderCount() < adapter.getDataList().size() - 1) {
                        nextSpanIndex = gridLayoutManager.getSpanSizeLookup().getSpanIndex(itemPosition + 1, spanCount);
                    }
//                    Y.y("getItemOffsets--: " + itemPosition + "  currentSpanIndex:" + currentSpanIndex + "  " +
//                            "nextSpanIndex:" + nextSpanIndex);
                    //列第一项
                    if (currentSpanIndex == 0) {
                        if (nextSpanIndex >= 0 && nextSpanIndex < spanCount - 1) {//正常列span为1
                            if (!drawFirstLine && itemPosition - adapter.getHeaderCount() == 0) {//列第一项第一个
                                outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), bottom);
//                                Y.y("getItemOffsets 列第一项0-- "+ itemPosition);
                            } else {
                                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(),
                                        mDivider.getIntrinsicWidth(), bottom);
//                                Y.y("getItemOffsets 列第一项1-- "+ itemPosition);
                            }
                            //该列属于首项
                        } else {
                            if (!drawFirstLine && itemPosition - adapter.getHeaderCount() == 0) {//列第一项第一个
                                outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), bottom);
//                                Y.y("getItemOffsets 该列属于首项0-- "+ itemPosition);
                            } else {
                                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), mDivider
                                        .getIntrinsicWidth(), bottom);
//                                Y.y("getItemOffsets 该列属于首项2-- "+ itemPosition);
                            }
                        }
                        //列最后一项
                    } else if (currentSpanIndex == spanCount - 1 || nextSpanIndex == 0) {
                        if (!drawFirstLine && itemPosition - adapter.getHeaderCount() <= spanCount) {//列第一项第一个
                            outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), bottom);
//                            Y.y("getItemOffsets 列最后一项1-- "+ itemPosition);
                        } else {
                            outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), mDivider
                                    .getIntrinsicWidth(), bottom);
//                            Y.y("getItemOffsets 列最后一项2-- "+ itemPosition);
                        }
                        //第一行
                    } else if (gridLayoutManager.getSpanSizeLookup().getSpanGroupIndex(itemPosition, spanCount) == 0) {
                        if (!drawFirstLine) {//列第一项第一个
                            outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), bottom);
//                            Y.y("getItemOffsets 第一行1-- "+ itemPosition);
                        } else {
                            outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(),
                                    mDivider.getIntrinsicWidth(), bottom);
//                            Y.y("getItemOffsets 第一行2-- "+ itemPosition);
                        }
                        //列中间项
                    } else {
                        outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(),
                                mDivider.getIntrinsicWidth(), bottom);
//                        Y.y("getItemOffsets 列中间项-- "+ itemPosition);
                    }
                } else {//列中间项
                    outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(),
                            mDivider.getIntrinsicWidth(), bottom);
                }
            }
        }
    }
}
