package com.library.adapter.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * @author YobertJomi
 * className OnItemMovementListener
 * created at  2019/6/22  23:57
 */
public interface OnItemMovementListener {

    int INVALID = 0;

    int LEFT = ItemTouchHelper.LEFT;

    int UP = ItemTouchHelper.UP;

    int RIGHT = ItemTouchHelper.RIGHT;

    int DOWN = ItemTouchHelper.DOWN;

    /**
     * Can drag and drop the ViewHolder?
     *
     * @param recyclerView     {@link RecyclerView}.
     * @param targetViewHolder target ViewHolder.
     * @return use {@link #LEFT}, {@link #UP}, {@link #RIGHT}, {@link #DOWN}.
     */
    int onDragFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder);

    /**
     * Can swipe and drop the ViewHolder?
     *
     * @param recyclerView     {@link RecyclerView}.
     * @param targetViewHolder target ViewHolder.
     * @return use {@link #LEFT}, {@link #UP}, {@link #RIGHT}, {@link #DOWN}.
     */
    int onSwipeFlags(RecyclerView recyclerView, RecyclerView.ViewHolder targetViewHolder);
}