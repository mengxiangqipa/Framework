package com.library.adapter_recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 简单RecyclerView的分割线
 * ItewDecoration
 *
 * @author YobertJomi
 * className HorizontalDividerItemDecoration
 * created at  2017/1/16  10:32
 */

public class HorizontalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;
    private Paint dividerPaint;

    @SuppressWarnings("deprecation")
    public HorizontalDividerItemDecoration(@NonNull Context context) {
        dividerPaint = new Paint();
        dividerPaint.setColor(context.getResources().getColor(androidx.appcompat.R.color.material_grey_600));
        dividerHeight = 1;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
        if (parent.getAdapter() instanceof UniversalAdapter) {
            if (itemPosition < ((UniversalAdapter) parent.getAdapter()).getHeaderCount() || itemPosition >= (
                    (UniversalAdapter) parent.getAdapter()).getDataItemCount() - 1) {
                outRect.bottom = 0;//最后一项设置为0
            } else {
                outRect.bottom = dividerHeight;
            }
        }
    }

//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
//    state) {
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
                if (adapter.isHeader(position) || adapter.isFooter(position) || position == (adapter.getHeaderCount()
                        + adapter.getDataItemCount() - 1)) {
                    continue;
                }
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
