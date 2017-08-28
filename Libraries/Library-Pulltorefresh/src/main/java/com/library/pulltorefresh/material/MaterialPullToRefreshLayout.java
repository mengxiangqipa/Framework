package com.library.pulltorefresh.material;

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
 *
 * Created by Administrator on 2016/9/11.
 */
public class MaterialPullToRefreshLayout extends BaseAbstractPullToRefreshLayout implements BaseAbstractPullToRefreshLayout.OnClickEmptyViewListener
{
	MaterialHeader header, footer;
	IndicatorDelegate indicator;

	public MaterialPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initIndicatorDelegate();
	}

	public MaterialPullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initIndicatorDelegate();
	}

	public MaterialPullToRefreshLayout(Context context)
	{
		super(context);
		initIndicatorDelegate();
	}

	private void initIndicatorDelegate()
	{
		indicator = new IndicatorDelegate();
		indicator.setCanPullDown(true);//设置是否能下拉
		indicator.setCanPullUp(true);//设置是否能上拉
		indicator.setLoadmoreDistcance(150);//上拉距离px
		indicator.setRefreshDistance(150);//下拉距离px
		indicator.setResistance(1f);//设置阻力系数，内部按正余弦变化，设置可能无效
		indicator.setMOVE_SPEED(2);//设置每次滚动距离
		indicator.setResistanceTime(500);//设置释放回滚的时间
		indicator.setMonitorFinishScroll(true);//刷新完成时监控回滚状态
		indicator.setRollingTime(200);//从释放刷新到后面的时间
		indicator.setFixedMode(IndicatorDelegate.FixedMode.FixedNothing);//设置中间内容区域固定
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
			header.changeStateInit();
			header.changeStateMovingToRefresh(deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case MOVING_TO_ONLOADING_HEIGHT:
			footer.changeStateInit();
			footer.changeStateMovingToRefresh(deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			header.changeStateReleaseToRefresh();
			break;
		case REFRESHING:
			// 正在刷新状态
			footer.changeStateReleaseToRefresh();
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			footer.changeStateReleaseToRefresh();
			break;
		case LOADING:
			// 正在加载状态
			footer.changeStateReleaseToRefresh();
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
			header.changeStateInit();
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			header.changeStateInit();

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
			footer.changeStateInit();
			//测试
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			footer.changeStateInit();
			break;
		}
	}

	@Override
	protected void clearPullAnimation()
	{

	}

	private MaterialHeader getInflateRefreshView()
	{ // header
		final MaterialHeader header = new MaterialHeader(getContext());
		int[] colors = getResources().getIntArray(R.array.google_colors);
		header.setColorSchemeColors(colors);
		header.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
		return header;
	}

	private ViewGroup getInflateLoadingView()
	{
		footer = new MaterialHeader(getContext());
		int[] colors = getResources().getIntArray(R.array.google_colors);
		footer.setColorSchemeColors(colors);
		footer.setPadding(0, LocalDisplay.dp2px(15), 0, LocalDisplay.dp2px(10));
		footer.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
		footer.setMinimumHeight((int) indicator.getRefreshDistance());
		RelativeLayout relativeLayout = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
