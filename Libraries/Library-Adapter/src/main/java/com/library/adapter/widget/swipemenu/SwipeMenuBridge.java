package com.library.adapter.widget.swipemenu;

import com.library.adapter.Controller;
import com.library.adapter.widget.UniversalRecyclerView;

/**
 * @author YobertJomi
 * className SwipeMenuBridge
 * created at  2019/6/22  23:58
 */
public class SwipeMenuBridge {

    private final Controller mController;
    private final int mDirection;
    private final int mPosition;

    public SwipeMenuBridge(Controller controller, int direction, int position) {
        this.mController = controller;
        this.mDirection = direction;
        this.mPosition = position;
    }

    @UniversalRecyclerView.Direction
    public int getDirection() {
        return mDirection;
    }

    /**
     * Get the position of button in the menu.
     */
    public int getPosition() {
        return mPosition;
    }

    public void closeMenu() {
        mController.smoothCloseMenu();
    }
}