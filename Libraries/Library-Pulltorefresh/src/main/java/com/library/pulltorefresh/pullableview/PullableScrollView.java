package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

	public PullableScrollView(Context context)
	{
		super(context);
		setOverScrollMode();
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOverScrollMode();
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
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
		return getScrollY() == 0;
	}

	@Override
	public boolean canPullUp()
	{
		if (!canPullUp)
		{
			return false;
		}
		return (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()));
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
