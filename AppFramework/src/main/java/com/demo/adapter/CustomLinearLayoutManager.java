package com.demo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * 可设置RecyclerView不可滚动（纵向）
 *
 * @author Yangjie
 *         className CustomLinearLayoutManager
 *         created at  2017/3/21  12:15
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
