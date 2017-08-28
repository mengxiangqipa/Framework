package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class PullableExpandableListView extends ExpandableListView implements Pullable
{

	public PullableExpandableListView(Context context)
	{
		super(context);
		setOverScrollMode();
	}

	public PullableExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOverScrollMode();
	}

	public PullableExpandableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setOverScrollMode();
	}

	private void setOverScrollMode()
	{
		this.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
	}

	@Override
	public boolean canPullDown()
	{
		try
		{
			if (getCount() == 0)
			{
				// 没有item的时候也可以下拉刷新
				return true;
			} else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0)
			{
				// 滑到顶部了
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (!canPullUp)
		{
			return false;
		}
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}

	boolean canPullUp = true;

	/**
	 * 手动设置是否能上拉
	 *
	 * @param canPullUp canPullUp
	 */
	public void setCanPullUp(boolean canPullUp)
	{
		this.canPullUp = canPullUp;
	}
}
