package com.library.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ListView;

public class PullableWebView extends WebView implements Pullable
{

	public PullableWebView(Context context)
	{
		super(context);
		setOverScrollMode();
	}

	public PullableWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setOverScrollMode();
	}

	public PullableWebView(Context context, AttributeSet attrs, int defStyle)
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

	@SuppressWarnings("deprecation")
	@Override
	public boolean canPullUp()
	{
		try
		{
			return canPullUp && getScrollY() >= Math.floor(getContentHeight() * getScale() - getMeasuredHeight());
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private boolean canPullUp = true;

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
