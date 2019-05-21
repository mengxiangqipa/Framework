package com.library.adapter_recyclerview;

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
 * 简单RecyclerView的分割线 末项可绘制
 * ItewDecoration
 *
 * @author YobertJomi
 * className HorizontaItemDecoration
 * created at  2017/1/16  10:32
 */

public class HorizontaItemDecoration extends RecyclerView.ItemDecoration {
    private Paint dividerPaint;
    private int dividerHeight;
    private TYPE type = TYPE.WITH_BOTTOM;

    public enum TYPE {
        WITH_TOP, WITH_BOTTOM, BOTH
    }

    @SuppressWarnings("deprecation")
    public HorizontaItemDecoration(@NonNull Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(android.support.v7.appcompat.R.color.material_grey_600));
        dividerHeight = 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = parent.getChildAdapterPosition(view);
        if (parent.getAdapter() instanceof UniversalAdapter) {
            UniversalAdapter adapter = (UniversalAdapter) parent.getAdapter();
            if (adapter.isFooter(itemPosition) || adapter.isHeader(itemPosition)) {
                outRect.bottom = 0;
                outRect.top = 0;
            } else if (type == TYPE.WITH_BOTTOM) {
                outRect.bottom = dividerHeight;
                outRect.top = 0;
            } else if (type == TYPE.WITH_TOP) {
                outRect.bottom = 0;
                outRect.top = dividerHeight;
            } else {
                //BOTH
                if (itemPosition - adapter.getHeaderCount() == 0) {
                    outRect.bottom = dividerHeight;
                    outRect.top = dividerHeight;
                } else {
                    outRect.bottom = dividerHeight;
                    outRect.top = 0;
                }
            }
        }
    }

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
                if ((type == TYPE.BOTH || type == TYPE.WITH_TOP) && i == 0) {
                    c.drawRect(left, view.getTop() - dividerHeight, right, view.getTop(), dividerPaint);
                }
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

    public void setDividerType(TYPE type) {
        this.type = type;
    }
}

