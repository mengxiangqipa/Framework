package com.library.adapter.touch;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author YobertJomi
 * className OnItemMoveListener
 * created at  2019/6/22  23:57
 */
public interface OnItemMoveListener {

    /**
     * When drag and drop the callback.
     *
     * @param srcHolder    src.
     * @param targetHolder target.
     * @return To deal with the returns true, false otherwise.
     */
    boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder);

    /**
     * When items should be removed when the callback.
     *
     * @param srcHolder src.
     */
    void onItemDismiss(RecyclerView.ViewHolder srcHolder);
}