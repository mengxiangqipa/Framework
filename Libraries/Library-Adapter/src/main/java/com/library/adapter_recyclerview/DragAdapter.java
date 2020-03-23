package com.library.adapter_recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * 可拖动和滑动删除的adapter
 *
 * @author YobertJomi
 * className DragAdapter
 * created at  2017/2/7  15:39
 */

public abstract class DragAdapter<D> extends UniversalAdapter<D> implements DragItemTouchHelperCallback
        .ItemTouchMoveDismissHelper {
    private OnStartDragListener onStartDragListener;

    public DragAdapter(@NonNull Context context, @LayoutRes int layoutId, @Nullable List<D> list,
                       @NonNull
            OnStartDragListener onStartDragListener) {
        super(context, layoutId, list);
        this.onStartDragListener = onStartDragListener;
    }

    /**
     * item被选中
     */
    public abstract void onItemSelected_(Context context, SwipeViewHolder viewHolder,
                                         int realPosition);

    /**
     * item取消选中
     */
    public abstract void onItemClear_(Context context, SwipeViewHolder viewHolder,
                                      int realPosition);

    /**
     * item消失
     *
     * @param context context
     */
    public abstract void onItemDismiss_(Context context, int position);

    /**
     * item移动成功通知
     *
     * @param context context
     */
    public abstract void onItemMoveSuccess_(Context context, int fromPosition, int toPosition);

    /**
     * @param position position
     */
    @Override
    public void onItemDismiss(int position) {
        onItemDismiss_(getContext(), isHeader(position) || isFooter(position) ? -2 :
                position - getHeaderCount());
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        int fromPositionReal = fromPosition - getHeaderCount();//实际开始位置要减去header的数量
        int toPositionReal = toPosition - getHeaderCount();//实际结束位置要减去header的数量
        if (fromPositionReal < 0 || toPositionReal < 0 && getDataList() == null)
            return;
        //		{
        //			//这个是表示从fromPositionReal位置与toPositionReal这个   交换位置
        //			Collections.swap(getDataList(), fromPositionReal, toPositionReal);
        //			notifyItemRangeChanged(Math.min(fromPosition, toPosition), Math.abs
        //			(fromPosition - toPosition));
        //		}
        //这个是表示从fromPositionReal位置移动到toPositionReal这个位置
        if (fromPositionReal < toPositionReal) {
            for (int i = fromPositionReal; i < toPositionReal; i++) {
                try {
                    Collections.swap(getDataList(), i, i + 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = fromPositionReal; i > toPositionReal; i--) {
                try {
                    Collections.swap(getDataList(), i, i - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        onItemMoveSuccess_(getContext(), fromPositionReal, toPositionReal);
    }

    public void startDrag(SwipeViewHolder viewHolder) {
        onStartDragListener.onStartDrag(viewHolder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.ViewHolder createUniversalViewHolder(View itemView) {
        return new SwipeViewHolder(itemView);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public class SwipeViewHolder extends UniversalAdapter.UniversalViewHolder implements DragItemTouchHelperCallback
            .ItemTouchDragHelper {
        SwipeViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onItemSelected(Context context) {
            onItemSelected_(context, SwipeViewHolder.this, getLayoutPosition() - getHeaderCount());
        }

        @Override
        public void onItemClear(Context context) {
            onItemClear_(context, SwipeViewHolder.this, getLayoutPosition() - getHeaderCount());
        }
    }
}
