package com.library.adapter_recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.library.adapter.R;

import java.util.List;

/**
 * 可侧滑删除的adapter
 *
 * @author YobertJomi
 * className SwipeDeleteAdapter
 * created at  2017/9/6  10:16
 */
public abstract class SwipeDeleteAdapter<D> extends UniversalAdapter<D> {

    private SwipeOpenViewHolder swipeOpenViewHolder;

    public SwipeDeleteAdapter(@NonNull Context context, @LayoutRes int layoutId,
                              @Nullable List<D> list) {
        this(context, layoutId, list, null);
    }

    public SwipeDeleteAdapter(@NonNull Context context, @LayoutRes int layoutId,
                              @Nullable List<D> list,
                              SwipeOpenViewHolder swipeOpenViewHolder) {
        super(context, layoutId, list);
        this.swipeOpenViewHolder = swipeOpenViewHolder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.ViewHolder createUniversalViewHolder(View itemView) {
        return new SwipeViewHolder(itemView);
    }

    public class SwipeViewHolder extends UniversalViewHolder implements SwipeOpenViewHolder {
        SwipeViewHolder(View itemView) {
            super(itemView);
        }

        @NonNull
        @Override
        public View getSwipeView() {
            if (null != swipeOpenViewHolder) {
                return swipeOpenViewHolder.getSwipeView();
            }
            return getView(R.id.content);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder getViewHolder() {
            return this;
        }

        @Override
        public float getEndHiddenViewSize() {
            if (null != swipeOpenViewHolder) {
                return swipeOpenViewHolder.getEndHiddenViewSize();
            }
            return getView(R.id.btnDelete).getMeasuredWidth();
        }

        @Override
        public float getStartHiddenViewSize() {
            if (null != swipeOpenViewHolder) {
                return swipeOpenViewHolder.getStartHiddenViewSize();
            }
            return getView(R.id.btnUnRead).getMeasuredWidth();
        }

        @Override
        public void notifyStartOpen() {
            if (null != swipeOpenViewHolder) {
                swipeOpenViewHolder.notifyStartOpen();
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.red_adapter));
            }
        }

        @Override
        public void notifyEndOpen() {
            if (null != swipeOpenViewHolder) {
                swipeOpenViewHolder.notifyEndOpen();
            } else {
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                        R.color.blue_adapter));
            }
        }
    }
}
