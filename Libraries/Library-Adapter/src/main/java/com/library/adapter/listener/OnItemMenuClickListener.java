package com.library.adapter.listener;

import com.library.adapter.widget.swipemenu.SwipeMenuBridge;

/**
 * @author YobertJomi
 * className OnItemMenuClickListener
 * created at  2019/6/23  0:02
 */
public interface OnItemMenuClickListener {

    /**
     * @param menuBridge      menu bridge.
     * @param adapterPosition position of item.
     */
    void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition);
}