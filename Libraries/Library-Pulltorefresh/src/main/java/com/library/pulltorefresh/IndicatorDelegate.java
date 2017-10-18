package com.library.pulltorefresh;

/**
 *     @author Yobert Jomi
 *     className Indicator
 *     created at  2016/9/11  11:56
 *     上下拉刷新的参数设置
 */
public class IndicatorDelegate
{
	/**
	 * 释放刷新的距离
	 */
	private float refreshDistance = 150;
	/**
	 * 释放加载的距离
	 */
	private float loadmoreDistcance = 150;
	/**
	 * 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	 */
	private float resistance = 1f;
	/**
	 * 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	 */
	private boolean canPullDown = true;
	/**
	 * 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	 */
	private boolean canPullUp = true;
	/**
	 * 是否监听刷新/加载更多 完成时监控回滚状态,默认false，完成后的状态不监控
	 * 这对以后的状态有影响,即DONE状态下滑动，不监听达到上下拉阈值等各个状态
	 */
	private boolean monitorFinishScroll = false;
	/**
	 * 正在刷新时是否监听changeState(REFRESHING, pullDownY);(timer),默认样式固定，false
	 * 要头部悬停，设置monitorOnRefreshing为true
	 */
	private boolean monitorOnRefreshingOrLoading = false;

	/**
	 * 回滚速度
	 */
	private float MOVE_SPEED = 8;
	/**
	 * 回滚速度是否被改变
	 */
	private boolean changeMoveSpeed = false;
	/**
	 * 回弹/隐藏时间
	 */
	private long resistanceTime = 300;
	/**
	 * 从释放刷新到后面的时间
	 */
	private long rollingTime = 200;

	/**
	 * 自动刷新回滚的时间
	
	 */
	private long autoRefreshTimemillis = 300;

	private FixedMode fixedMode = FixedMode.FixedNothing;

	public enum FixedMode//FixedHeaderHover 头部悬浮，之后消失
	{
		FixedNothing, FixedContent, FixedHeader,FixedHeaderHover
	}

	public float getRefreshDistance()
	{
		return refreshDistance;
	}

	/**
	 * 设置下拉阈值
	 * @param refreshDistance 下拉阈值
	 */
	public void setRefreshDistance(float refreshDistance)
	{
		this.refreshDistance = refreshDistance;
	}

	public float getLoadmoreDistcance()
	{
		return loadmoreDistcance;
	}

	/**
	 * 设置上拉阈值
	 * @param loadmoreDistcance 上拉阈值
	 */
	public void setLoadmoreDistcance(float loadmoreDistcance)
	{
		this.loadmoreDistcance = loadmoreDistcance;
	}

	public float getResistance()
	{
		return resistance;
	}

	/**
	 * 设置阻力系数 /Base里面做了处理，可能无效(可以修改)
	 * @param resistance 0-1f
	 */
	public void setResistance(float resistance)
	{
		this.resistance = resistance;
	}

	public boolean isCanPullDown()
	{
		return canPullDown;
	}

	/**
	 * 设置是否可下拉
	 */
	public void setCanPullDown(boolean canPullDown)
	{
		this.canPullDown = canPullDown;
	}

	public boolean isCanPullUp()
	{
		return canPullUp;
	}

	/**
	 * 设置是否可上拉
	 * @param canPullUp 是否可上拉
	 */
	public void setCanPullUp(boolean canPullUp)
	{
		this.canPullUp = canPullUp;
	}

	public float getMOVE_SPEED()
	{
		return MOVE_SPEED;
	}

	/**
	 * 设置  回弹/隐藏时的每次递增/递减的值
	 * @param MOVE_SPEED 回弹/隐藏时的每次递增/递减的值
	 */
	public void setMOVE_SPEED(float MOVE_SPEED)
	{
		changeMoveSpeed = true;
		this.MOVE_SPEED = MOVE_SPEED;
	}

	public boolean isChangeMoveSpeed()
	{
		return changeMoveSpeed;
	}

	public void setChangeMoveSpeed(boolean changeMoveSpeed)
	{
		this.changeMoveSpeed = changeMoveSpeed;
	}

	public long getResistanceTime()
	{
		return resistanceTime;
	}

	/**
	 * 设置 回弹/隐藏时间
	 * @param resistanceTime 回弹/隐藏时间
	 */
	public void setResistanceTime(long resistanceTime)
	{
		this.resistanceTime = resistanceTime;
	}

	public boolean isMonitorFinishScroll()
	{
		return monitorFinishScroll;
	}

	public void setMonitorFinishScroll(boolean monitorFinishScroll)
	{
		this.monitorFinishScroll = monitorFinishScroll;
	}

	public long getRollingTime()
	{
		return rollingTime;
	}

	public void setRollingTime(long rollingTime)
	{
		this.rollingTime = rollingTime;
	}

	public FixedMode getFixedMode()
	{
		return fixedMode;
	}

	public void setFixedMode(FixedMode fixedMode)
	{
		this.fixedMode = fixedMode;
	}

	public long getAutoRefreshTimemillis()
	{
		return autoRefreshTimemillis;
	}

	/**
	 * 自动刷新回滚的时间
	 * @param autoRefreshTimemillis 刷新时间
	 */
	public void setAutoRefreshTimemillis(long autoRefreshTimemillis)
	{
		this.autoRefreshTimemillis = autoRefreshTimemillis;
	}

	public boolean isMonitorOnRefreshingOrLoading()
	{
		return monitorOnRefreshingOrLoading;
	}

	public void setMonitorOnRefreshingOrLoading(boolean monitorOnRefreshingOrLoading)
	{
		this.monitorOnRefreshingOrLoading = monitorOnRefreshingOrLoading;
	}
}
