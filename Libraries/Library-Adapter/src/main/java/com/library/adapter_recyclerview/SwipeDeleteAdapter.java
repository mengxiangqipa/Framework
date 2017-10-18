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
 *     @author YobertJomi
 *     className SwipeDeleteAdapter
 *     created at  2017/9/6  10:16
 */
public abstract class SwipeDeleteAdapter<D> extends UniversalAdapter<D>
{

    public SwipeDeleteAdapter(@NonNull Context context, @LayoutRes int layoutId, @Nullable List<D> list)
    {
        super(context, layoutId, list);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecyclerView.ViewHolder createUniversalViewHolder(View itemView)
    {
        return new SwipeViewHolder(itemView);
    }

    public class SwipeViewHolder extends UniversalViewHolder implements SwipeOpenViewHolder
    {
        SwipeViewHolder(View itemView)
        {
            super(itemView);
        }

        @NonNull
        @Override
        public View getSwipeView()
        {
            return getView(R.id.content);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder getViewHolder()
        {
            return this;
        }

        @Override
        public float getEndHiddenViewSize()
        {
            return getView(R.id.btnDelete).getMeasuredWidth();
        }

        @Override
        public float getStartHiddenViewSize()
        {
            return getView(R.id.btnUnRead).getMeasuredWidth();
        }

        @Override
        public void notifyStartOpen()
        {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.red_adapter));
        }

        @Override
        public void notifyEndOpen()
        {
            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_adapter));
        }
    }

}
