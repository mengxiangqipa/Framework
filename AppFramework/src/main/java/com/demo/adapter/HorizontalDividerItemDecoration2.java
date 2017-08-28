package com.demo.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 简单RecyclerView的分割线
 * ItewDecoration
 *
 * @author Yangjie
 *         className HorizontalDividerItemDecoration
 *         created at  2017/1/16  10:32
 */

public class HorizontalDividerItemDecoration2 extends RecyclerView.ItemDecoration {
    private int dividerHeight;
    private Paint dividerPaint;

    @SuppressWarnings("deprecation")
    public HorizontalDividerItemDecoration2(@NonNull Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(android.support.v7.appcompat.R.color.material_grey_600));
        dividerHeight = 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        if (parent.getAdapter() instanceof UniversalAdapter) {
            if (((UniversalAdapter) parent.getAdapter()).isFooter(itemPosition) || ((UniversalAdapter) parent.getAdapter()).isHeader(itemPosition)) {
                outRect.bottom = 0;
            } else {
                outRect.bottom = dividerHeight;
            }
        }
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
//        outRect.bottom = dividerHeight;
//    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof UniversalAdapter) {
            UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                if (adapter.isHeader(position) || adapter.isFooter(position))
                    continue;
                float top = view.getBottom();
                float bottom = view.getBottom() + dividerHeight;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }

    }

    @SuppressWarnings("deprecation")
    public void setColor(@NonNull Context context, @ColorRes int color) {
        dividerPaint.setColor(context.getResources().getColor(color));
    }

    public void setDividerHeightPx(@IntRange(from = 1) int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }
}
