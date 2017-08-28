package com.library.pulltorefresh.rentalssun;

import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.R;
import com.library.pulltorefresh.storehouse.LocalDisplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 *     @author Yangjie
 *     className RentalsSunlPullToRefreshLayout
 *     created at  2016/9/21  10:23
 */
public class RentalsSunlPullToRefreshLayout extends BaseAbstractPullToRefreshLayout implements BaseAbstractPullToRefreshLayout.OnClickEmptyViewListener
{
	RentalsSunHeaderView header;
	RentalsSunFooterView footer;
	IndicatorDelegate indicator;

	public RentalsSunlPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initIndicatorDelegate();
	}

	public RentalsSunlPullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initIndicatorDelegate();
	}

	public RentalsSunlPullToRefreshLayout(Context context)
	{
		super(context);
		initIndicatorDelegate();
	}

	private void initIndicatorDelegate()
	{
		indicator = new IndicatorDelegate();
		indicator.setCanPullDown(true);//设置是否能下拉
		indicator.setCanPullUp(true);//设置是否能上拉
		indicator.setLoadmoreDistcance(290);//上拉距离px
		indicator.setRefreshDistance(290);//下拉距离px
		indicator.setResistance(1f);//设置阻力系数，内部按正余弦变化，设置可能无效
		indicator.setMOVE_SPEED(2);//设置每次滚动距离
		indicator.setResistanceTime(500);//设置释放回滚的时间
		indicator.setMonitorFinishScroll(true);//刷新完成时监控回滚状态
		indicator.setRollingTime(200);//从释放刷新到后面的时间
		indicator.setFixedMode(IndicatorDelegate.FixedMode.FixedNothing);//设置中间内容区域固定 //太阳升起用于固定header 效果不好，header，footer有缩放效果
		setIndicatorDelegate(indicator);
	}

	@Override
	protected View initRefreshView()
	{
		header = getInflateRefreshView();
		return header;
	}

	@Override
	protected View initLoadMoreView()
	{
		return getInflateLoadingView();
	}

	@Override
	protected void changeRefreshState(int lastState,int toState, float deltaY)
	{
		switch (toState)
		{
		case INIT:
			// 下拉布局初始状态
			// 上拉布局初始状态
			header.changeStateInit();
			footer.changeStateInit();
			break;
		case MOVING_TO_REFRESH_HEIGHT:
			header.changeStateMoving((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case MOVING_TO_ONLOADING_HEIGHT:
			footer.changeStateMoving((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			header.changeStateMoving((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case REFRESHING:
			// 正在刷新状态
			header.changeStateOnRefreshing((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			footer.changeStateMoving((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case LOADING:
			// 正在加载状态
			footer.changeStateOnRefreshing((int) deltaY, deltaY / indicator.getRefreshDistance() * 1f);
			break;
		}
	}

	@Override
	protected void refreshComplete(int refreshResult)
	{
		switch (refreshResult)
		{
		case SUCCEED:
			// 刷新成功
			header.changeStateFinish();
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			header.changeStateFinish();
			//
			View inflate = LayoutInflater.from(getContext()).inflate(R.layout.allview_empty_view, null);
			inflate.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
			inflate.setMinimumHeight(LocalDisplay.getScreenHeightPixels(getContext()));
			setOnClickEmptyViewListener(this);//先设置监听器，再addview
			setEmptyView(inflate);
			break;
		}
	}

	@Override
	protected void loadMoreComplete(int refreshResult)
	{
		switch (refreshResult)
		{
		case SUCCEED:
			// 刷新成功
			footer.changeStateFinish();
			//测试
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			footer.changeStateFinish();
			break;
		}
	}

	@Override
	protected void clearPullAnimation()
	{

	}

	private RentalsSunHeaderView getInflateRefreshView()
	{ // header
		final RentalsSunHeaderView header = new RentalsSunHeaderView(getContext());

		header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
		return header;
	}

	private ViewGroup getInflateLoadingView()
	{
		footer = new RentalsSunFooterView(getContext());

		footer.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
		footer.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
		footer.setMinimumHeight((int) indicator.getRefreshDistance());
		RelativeLayout relativeLayout = new RelativeLayout(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relativeLayout.setLayoutParams(params);
		relativeLayout.addView(footer, params);
		return relativeLayout;
	}

	@Override
	public void OnClickEmptyView(View emptyView)
	{
		Toast.makeText(getContext(), "我点击了空view", Toast.LENGTH_SHORT).show();
		autoRefresh(600);
	}
}
