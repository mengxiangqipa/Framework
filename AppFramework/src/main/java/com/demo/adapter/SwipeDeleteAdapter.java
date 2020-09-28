package com.demo.adapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.demo.demo.R;
import com.library.adapter_recyclerview.SwipeOpenViewHolder;
import com.library.adapter_recyclerview.UniversalAdapter;

import java.util.List;

/**
 * 可侧滑删除的adapter
 *
 * @author Yangjie
 * className SwipeAdapter
 * created at  2017/2/7  15:39
 */

public abstract class SwipeDeleteAdapter<D> extends UniversalAdapter<D> {

    public SwipeDeleteAdapter(@NonNull Context context, @LayoutRes int layoutId,
                              @Nullable List<D> list) {
        super(context, layoutId, list);
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
            return getView(R.id.content);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder getViewHolder() {
            return this;
        }

        @Override
        public float getEndHiddenViewSize() {
            return getView(R.id.btnDelete).getMeasuredWidth();
        }

        @Override
        public float getStartHiddenViewSize() {
            return getView(R.id.btnUnRead).getMeasuredWidth();
        }

        @Override
        public void notifyStartOpen() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.red));
        }

        @Override
        public void notifyEndOpen() {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                    R.color.blue));
        }
    }
}
