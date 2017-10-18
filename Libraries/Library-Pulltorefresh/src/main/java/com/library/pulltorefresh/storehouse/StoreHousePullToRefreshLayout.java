package com.library.pulltorefresh.storehouse;

import java.util.ArrayList;
import java.util.List;

import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.R;

import android.content.Context;
import android.graphics.Point;
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
public class StoreHousePullToRefreshLayout extends BaseAbstractPullToRefreshLayout implements BaseAbstractPullToRefreshLayout.OnClickEmptyViewListener
{
	StoreHouseHeader header, footer;
	IndicatorDelegate indicator;

	public StoreHousePullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initIndicatorDelegate();
	}

	public StoreHousePullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initIndicatorDelegate();
	}

	public StoreHousePullToRefreshLayout(Context context)
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
		return getInflateRefreshView();
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
			header.stopLoading();
			footer.stopLoading();
			break;
		case MOVING_TO_REFRESH_HEIGHT:
			header.stopLoading();
			header.setProgress(deltaY / indicator.getRefreshDistance() * 1f);
			break;
		case MOVING_TO_ONLOADING_HEIGHT:
			footer.stopLoading();
			footer.setProgress(deltaY / indicator.getLoadmoreDistcance() * 1f);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			header.startLoading();
			break;
		case REFRESHING:
			// 正在刷新状态
			header.startLoading();
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			footer.startLoading();
			break;
		case LOADING:
			// 正在加载状态
			footer.startLoading();
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
			header.stopLoading();
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			header.stopLoading();

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
			footer.stopLoading();
			//测试
			resetView();
			break;
		case FAIL:
		default:
			// 刷新失败
			footer.stopLoading();
			break;
		}
	}

	@Override
	protected void clearPullAnimation()
	{

	}
	@SuppressWarnings("deprecation")
	private ViewGroup getInflateRefreshView()
	{
		header = new StoreHouseHeader(getContext());
		header.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
		header.setMinimumHeight((int) indicator.getRefreshDistance());
		//		header.setPadding(0, (int)(indicator.getRefreshDistance()/3), 0, (int)(indicator.getRefreshDistance()/3));
		header.initWithString("Loading");
		//		header.initWithString("Ultra PTR");
		//		header.initWithPointList(getPointList());//加载中
		RelativeLayout relativeLayout = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		relativeLayout.setLayoutParams(params);
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		relativeLayout.setBackgroundColor(getResources().getColor(R.color.lib_ptr_black));
		params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relativeLayout.addView(header, params2);
		return relativeLayout;
	}
	@SuppressWarnings("deprecation")
	private ViewGroup getInflateLoadingView()
	{
		footer = new StoreHouseHeader(getContext());
		footer.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
		footer.setMinimumHeight((int) indicator.getRefreshDistance());
		//		footer.setPadding(0, (int)indicator.getRefreshDistance()/3, 0, (int)indicator.getRefreshDistance()/3);
		footer.initWithString("More");
		//		header.initWithString("Ultra PTR");
		//		header.initWithPointList(getPointList());//加载中
		RelativeLayout relativeLayout = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		relativeLayout.setBackgroundColor(getResources().getColor(R.color.lib_ptr_black));
		//		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		relativeLayout.setLayoutParams(params);
		relativeLayout.addView(footer, params);
		return relativeLayout;
	}

	private ArrayList<float[]> getPointList()
	{
		// this point is taken from https://github.com/cloay/CRefreshLayout
		List<Point> startPoints = new ArrayList<Point>();
		startPoints.add(new Point(240, 80));
		startPoints.add(new Point(270, 80));
		startPoints.add(new Point(265, 103));
		startPoints.add(new Point(255, 65));
		startPoints.add(new Point(275, 80));
		startPoints.add(new Point(275, 80));
		startPoints.add(new Point(302, 80));
		startPoints.add(new Point(275, 107));

		startPoints.add(new Point(320, 70));
		startPoints.add(new Point(313, 80));
		startPoints.add(new Point(330, 63));
		startPoints.add(new Point(315, 87));
		startPoints.add(new Point(330, 80));
		startPoints.add(new Point(315, 100));
		startPoints.add(new Point(330, 90));
		startPoints.add(new Point(315, 110));
		startPoints.add(new Point(345, 65));
		startPoints.add(new Point(357, 67));
		startPoints.add(new Point(363, 103));

		startPoints.add(new Point(375, 80));
		startPoints.add(new Point(375, 80));
		startPoints.add(new Point(425, 80));
		startPoints.add(new Point(380, 95));
		startPoints.add(new Point(400, 63));

		List<Point> endPoints = new ArrayList<Point>();
		endPoints.add(new Point(270, 80));
		endPoints.add(new Point(270, 110));
		endPoints.add(new Point(270, 110));
		endPoints.add(new Point(250, 110));
		endPoints.add(new Point(275, 107));
		endPoints.add(new Point(302, 80));
		endPoints.add(new Point(302, 107));
		endPoints.add(new Point(302, 107));

		endPoints.add(new Point(340, 70));
		endPoints.add(new Point(360, 80));
		endPoints.add(new Point(330, 80));
		endPoints.add(new Point(340, 87));
		endPoints.add(new Point(315, 100));
		endPoints.add(new Point(345, 98));
		endPoints.add(new Point(330, 120));
		endPoints.add(new Point(345, 108));
		endPoints.add(new Point(360, 120));
		endPoints.add(new Point(363, 75));
		endPoints.add(new Point(345, 117));

		endPoints.add(new Point(380, 95));
		endPoints.add(new Point(425, 80));
		endPoints.add(new Point(420, 95));
		endPoints.add(new Point(420, 95));
		endPoints.add(new Point(400, 120));
		ArrayList<float[]> list = new ArrayList<float[]>();

		int offsetX = Integer.MAX_VALUE;
		int offsetY = Integer.MAX_VALUE;

		for (int i = 0; i < startPoints.size(); i++)
		{
			offsetX = Math.min(startPoints.get(i).x, offsetX);
			offsetY = Math.min(startPoints.get(i).y, offsetY);
		}
		for (int i = 0; i < endPoints.size(); i++)
		{
			float[] point = new float[4];
			point[0] = startPoints.get(i).x - offsetX;
			point[1] = startPoints.get(i).y - offsetY;
			point[2] = endPoints.get(i).x - offsetX;
			point[3] = endPoints.get(i).y - offsetY;
			list.add(point);
		}
		return list;
	}

	@Override
	public void OnClickEmptyView(View emptyView)
	{
		Toast.makeText(getContext(), "我点击了空view", Toast.LENGTH_SHORT).show();
		autoRefresh(600);
	}
}
