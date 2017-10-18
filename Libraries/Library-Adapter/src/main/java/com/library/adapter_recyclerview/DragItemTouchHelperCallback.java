
package com.library.adapter_recyclerview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.library.adapter.R;
import com.library.utils.ScreenUtils;

public class DragItemTouchHelperCallback extends ItemTouchHelper.Callback
{

	private Context context;
	private DragAdapter swipeAdapter;
	private final ItemTouchMoveDismissHelper moveDismissHelper;
	private Paint p = new Paint();

	public DragItemTouchHelperCallback(@NonNull Context context, @Nullable DragAdapter swipeAdapter)
	{
		this.swipeAdapter = swipeAdapter;
		this.context = context;
		this.moveDismissHelper = swipeAdapter;
	}

	private DragFlag dragFlag = DragFlag.UP_DOWN;
	private int swipeFlag = 0;

	public enum DragFlag
	{
		NONE, UP_DOWN, ALL_DIRECTION
	}

	public void setDragFlag(DragFlag dragFlag)
	{
		this.dragFlag = dragFlag;
	}

	/**
	 * 设置侧滑的方向
	 * @param swipeFlag 侧滑的方向    默认0为禁止swipe
	 *         ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT为左右swipe
	 */
	public void setSwipeFlag(@IntRange(from = 0) int swipeFlag)
	{
		this.swipeFlag = swipeFlag;
	}

	@Override
	public boolean isLongPressDragEnabled()
	{
		return false;
	}

	@Override
	public boolean isItemViewSwipeEnabled()
	{
		return true;
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		// Enable drag up and down and right swipe in right direction
		int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		if (dragFlag == DragFlag.ALL_DIRECTION)
		{
			dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
		} else if (dragFlag == DragFlag.UP_DOWN)
		{
			dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
		} else if (dragFlag == DragFlag.NONE)
		{
			dragFlags = 0;
		}
		int swipeFlags = swipeFlag;
		//header，footer不滑动
		if (viewHolder == null || viewHolder.getLayoutPosition() < swipeAdapter.getHeaderCount()
				|| viewHolder.getLayoutPosition() >= swipeAdapter.getHeaderCount() + swipeAdapter.getDataItemCount())
			swipeFlags = 0;
		// final int swipeFlags =  ItemTouchHelper.END | ItemTouchHelper.START; Enable swipe in both direction
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public long getAnimationDuration(RecyclerView recyclerView, int animationType, float animateDx, float animateDy)
	{
		// return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : DEFAULT_SWIPE_ANIMATION_DURATION;
		return animationType == ItemTouchHelper.ANIMATION_TYPE_DRAG ? DEFAULT_DRAG_ANIMATION_DURATION : 350;
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target)
	{
		if (source.getItemViewType() != target.getItemViewType())
		{
			return false;
		}
		// Notify the adapter of the move
		if (null != moveDismissHelper)
			moveDismissHelper.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
		return true;
	}

	@Override
	public void onSwiped(final RecyclerView.ViewHolder viewHolder, int i)
	{
		// Notify the adapter of the dismissal
		if (null != moveDismissHelper)
			moveDismissHelper.onItemDismiss(viewHolder.getAdapterPosition());
	}

	@Override
	public void onChildDraw(final Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState,
			boolean isCurrentlyActive)
	{
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		// Fade out the view as it is swiped out of the parent's bounds
		if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
		{
			View itemView = viewHolder.itemView;
			Bitmap bitmap;
			if (dX > 0)
			{
				bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_phone);
				// Set color for right swipe
				p.setColor(ContextCompat.getColor(context, R.color.red_adapter));

				// Draw Rect with varying right side, equal to displacement dX
				c.drawRect((float) itemView.getLeft() + ScreenUtils.getInstance().dip2px(context, 0), (float) itemView.getTop(),
						dX + ScreenUtils.getInstance().dip2px(context, 0), (float) itemView.getBottom(), p);

				// Set the image icon for right swipe
				c.drawBitmap(bitmap, (float) itemView.getLeft() + ScreenUtils.getInstance().dip2px(context, 16),
						(float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - bitmap.getHeight()) / 2, p);

				bitmap.recycle();
			}
		}
	}

	@Override
	public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState)
	{
		if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
		{
			// Let the view holder know that this item is being moved or dragged
			ItemTouchDragHelper itemViewHolder = (ItemTouchDragHelper) viewHolder;
			itemViewHolder.onItemSelected(context);
		}

		super.onSelectedChanged(viewHolder, actionState);

		/* final boolean swiping = actionState == ItemTouchHelper.ACTION_STATE_SWIPE;
		swipeRefreshLayout.setEnabled(!swiping);*/
	}

	@Override
	public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
	{
		super.clearView(recyclerView, viewHolder);
		// Tell the view holder it's time to restore the idle state
		ItemTouchDragHelper itemViewHolder = (ItemTouchDragHelper) viewHolder;
		itemViewHolder.onItemClear(context);
	}

	@Override
	public float getMoveThreshold(RecyclerView.ViewHolder viewHolder)
	{
		//  return super.getMoveThreshold(viewHolder);
		return 0.1f;
		//	return super.getMoveThreshold(0.5f);
	}

	@Override
	public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder)
	{
		//if (viewHolder instanceof RecyclerView.ViewHolder) return 1f;
		//return super.getSwipeThreshold(viewHolder);
		return 0.9f;
	}

	/**
	 * recyclerView滑动删除及dismiss的方法接口
	 *     @author Yangjie
	 *     className ItemTouchMoveDismissHelper
	 *     created at  2017/2/7  16:02
	 */
	interface ItemTouchMoveDismissHelper
	{
		void onItemMove(int fromPosition, int toPosition);

		void onItemDismiss(int position);
	}

	/**
	 * recyclerView拖拽的方法接口
	 *     @author Yangjie
	 *     className ItemTouchDragHelper
	 *     created at  2017/2/7  16:02
	 */
	interface ItemTouchDragHelper
	{
		void onItemSelected(Context context);

		void onItemClear(Context context);
	}
}
