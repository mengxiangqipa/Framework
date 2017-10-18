package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

public class PullableGridView extends GridView implements Pullable
{

	public PullableGridView(Context context)
	{
		super(context);
		setOverScrollMode();
	}

	public PullableGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOverScrollMode();
	}

	public PullableGridView(Context context, AttributeSet attrs, int defStyle)
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
		//没有item的时候也可以下拉刷新|| 滑到顶部了
		return getCount() == 0 || (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0);
	}

	@Override
	public boolean canPullUp()
	{
		if (!canPullUp)
		{
			return false;
		}
		try
		{
			if (getCount() == 0)
			{
				// 没有item的时候也可以上拉加载
				return getCount() == 0;
			} else if (getLastVisiblePosition() == (getCount() - 1))
			{
				// 滑到底部了
				return (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
						&& getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight());
			}
		} catch (Exception e)
		{
			e.printStackTrace();
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
