package com.library.pulltorefresh;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.library.pulltorefresh.pullableview.Pullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public abstract class BaseAbstractPullToRefreshLayout extends RelativeLayout {
    /**
     * 自动刷新
     */
    public static final int AUTO_REFRESH = 0x66;
    /**
     * 初始状态
     */
    public static final int INIT = 0;
    /**
     * 释放刷新 达到下拉刷新阈值 并继续下拉
     */
    public static final int RELEASE_TO_REFRESH = 1;
    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;
    /**
     * 释放加载  达到上拉刷新阈值 并继续上拉
     */
    public static final int RELEASE_TO_LOAD = 3;
    /**
     * 正在加载
     */
    public static final int LOADING = 4;
    /**
     * 操作完毕
     */
    public static final int DONE = 5;
    /**
     * 初始状态-->未达到下拉刷新阈值
     */
    public static final int MOVING_TO_REFRESH_HEIGHT = 100;
    /**
     * 初始状态-->未达到上拉刷新阈值
     */
    public static final int MOVING_TO_ONLOADING_HEIGHT = 200;
    /**
     * 刷新成功
     */
    public static final int SUCCEED = 0;
    /**
     * 刷新失败
     */
    public static final int FAIL = 1;
    /**
     * 子类请求获取滚动优先权,主要处理跟viewPager等的滚动冲突
     */
    private boolean requestFirstTouch = false;
    /**
     * last y
     */
    private int mLastMotionY;
    /**
     * last x
     */
    private int mLastMotionX;
    /**
     * 当前状态
     */
    private int state = INIT;
    /**
     * 刷新回调接口
     */
    private OnRefreshListener mListener;
    /**
     * 点击emptyView回调接口
     */
    private OnClickEmptyViewListener onClickEmptyViewListener;
    /**
     * downY按下Y坐标，lastY上一个事件点Y坐标
     */
    private float downY, lastY;

    /**
     * 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
     */
    private float pullDownY = 0;
    /**
     * 上拉的距离
     */
    private float pullUpY = 0;
    /**
     * 释放刷新的距离
     */
    private float refreshDist = 150;
    /**
     * 释放加载的距离
     */
    private float loadmoreDist = 150;

    private MyTimer timer;//回滚的timer
    /**
     * 回滚速度
     */
    private float MOVE_SPEED = 8;
    /**
     * 第一次执行布局
     */
    private boolean isLayout = false;
    /**
     * 在刷新过程中滑动操作
     */
    private boolean isTouch = false;
    /**
     * 手指是否按下屏幕
     */
    private boolean isDown = false;
    /**
     * 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
     */
    private float radio = 1f;

    /**
     * 下拉头
     */
    private View refreshView;
    /**
     * 上拉头
     */
    private View loadmoreView;

    /**
     * 中间内容区域
     */
    private View contentView;
    /**
     * 中间缺省空view
     */
    private View emptyView;
    /**
     * 过滤多点触碰
     */
    private int mEvents;
    /**
     * 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
     */
    private boolean canPullDown = true;
    /**
     * 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
     */
    private boolean canPullUp = true;
    /**
     * 人为设置是否可用下拉
     */
    private boolean canPullDownFromSet = true;
    /**
     * 人为设置是否可用上拉
     */
    private boolean canPullUpFromSet = true;
    /**
     * 上下拉参数设置；比如 上下拉距离，回滚速度，阻力系数
     */
    private IndicatorDelegate indicatorDelegate;
    /**
     * 执行自动回滚的handler
     */
    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            boolean autoRefresh = msg.arg1 == AUTO_REFRESH;
            if (indicatorDelegate.isChangeMoveSpeed()) {
                MOVE_SPEED = indicatorDelegate.getMOVE_SPEED();
            } else {
                // 回弹速度随下拉距离moveDeltaY增大而增大
                MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs
                        (pullUpY))));
            }
            if (!isTouch)//取消timer的情况
            {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                } else if ((state == INIT || state == MOVING_TO_REFRESH_HEIGHT || state == MOVING_TO_ONLOADING_HEIGHT
                        || state == DONE) && pullDownY <= 0
                        && pullUpY >= 0 && !autoRefresh) {
                    //Log.e("yy", "自动被取消了1");
                    timer.cancel();
                } else if (autoRefresh && pullDownY >= refreshDist) {
                    timer.cancel();
                }
            } else if ((state == INIT || state == MOVING_TO_REFRESH_HEIGHT || state == MOVING_TO_ONLOADING_HEIGHT ||
                    state == DONE) && pullDownY <= 0
                    && pullUpY >= 0 && !autoRefresh) {
                //Log.e("yy", "自动被取消了2");
                timer.cancel();
            }
            if (autoRefresh) {//自动刷新
                if (pullDownY < indicatorDelegate.getRefreshDistance()) {
                    pullDownY += MOVE_SPEED;
                }
            } else {
                if (pullDownY > 0)
                    pullDownY -= MOVE_SPEED;
                else if (pullUpY < 0)
                    pullUpY += MOVE_SPEED;
            }
            //Log.e("yy", "autoRefresh:" + autoRefresh + "    state:" + state + "  pullDownY:" + pullDownY + "
            // pullUp:" + pullUpY + "   isTouch:" + isTouch);
            ///////////{以下为回滚时监听
            if (state != REFRESHING && state != LOADING && (indicatorDelegate.isMonitorFinishScroll() || state !=
                    DONE)) {
                if (pullDownY >= indicatorDelegate.getRefreshDistance())
                    changeState(RELEASE_TO_REFRESH, pullDownY);
                else if (pullDownY > 0)
                    changeState(MOVING_TO_REFRESH_HEIGHT, pullDownY);
                if (-pullUpY >= indicatorDelegate.getLoadmoreDistcance())
                    changeState(RELEASE_TO_LOAD, -pullUpY);
                else if (-pullUpY > 0)
                    changeState(MOVING_TO_ONLOADING_HEIGHT, -pullUpY);
                else if (pullDownY <= 0 && pullUpY >= 0)
                    changeState(INIT, 0);
            } else if (!indicatorDelegate.isMonitorFinishScroll() && state == DONE) {//特殊处理不监听完成滚动&&状态完成，上下拉值为0
                if (pullDownY <= 0 && pullUpY >= 0)
                    changeState(INIT, 0);
            }
            if (state == REFRESHING && indicatorDelegate.isMonitorOnRefreshingOrLoading()) {
                changeState(REFRESHING, pullDownY);
            } else if (state == LOADING && indicatorDelegate.isMonitorOnRefreshingOrLoading()) {
                changeState(LOADING, -pullUpY);
            }
            if (autoRefresh && pullDownY >= refreshDist && state != REFRESHING) {
                timer.cancel();
                changeState(REFRESHING, pullDownY);
                if (null != mListener) {
                    mListener.onRefresh(BaseAbstractPullToRefreshLayout.this);
                }
            }
            ///////////以上为回滚时监听}
            if (pullDownY < 0) {// 已完成回弹
                pullDownY = 0;
                clearPullAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT, pullDownY);
                timer.cancel();
            }
            if (pullUpY > 0) {// 已完成回弹
                pullUpY = 0;
                clearPullAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING)
                    changeState(INIT, -pullUpY);
                timer.cancel();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
        }
    };

    public BaseAbstractPullToRefreshLayout(Context context) {
        super(context);
        initTimer();
    }

    public BaseAbstractPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTimer();
    }

    public BaseAbstractPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTimer();
    }

    /**
     * 监听刷新/加载
     *
     * @param listener 监听刷新/加载
     */
    public void setOnRefreshListener(@NonNull OnRefreshListener listener) {
        mListener = listener;
    }

    /**
     * 监听点击空view
     *
     * @param onClickEmptyViewListener 监听点击空view
     */
    public void setOnClickEmptyViewListener(@NonNull OnClickEmptyViewListener onClickEmptyViewListener) {
        this.onClickEmptyViewListener = onClickEmptyViewListener;
    }

    private void initTimer() {
        timer = new MyTimer(updateHandler);
    }

    /**
     * 初始化设置参数
     */
    private void initIndicator() {
        indicatorDelegate = null == getIndicatorDelegate() ? new IndicatorDelegate() : getIndicatorDelegate();
        refreshDist = indicatorDelegate.getRefreshDistance();//获取下拉距离
        loadmoreDist = indicatorDelegate.getLoadmoreDistcance();//获取上拉距离
        radio = indicatorDelegate.getResistance();//获取拉动阻力系数
        canPullDownFromSet = indicatorDelegate.isCanPullDown();//获取是否人为设置可以下拉
        canPullUpFromSet = indicatorDelegate.isCanPullUp();//获取是否人为设置可以上拉
    }

    private void hideScroll() {
        int period = (int) (indicatorDelegate.getMOVE_SPEED() * indicatorDelegate.getResistanceTime() /
                (indicatorDelegate.getRefreshDistance()));
        period = Math.max(period, 1);
        timer.schedule(indicatorDelegate.isChangeMoveSpeed() ? period : 5);
    }

    public void hide() {
        int period = (int) (indicatorDelegate.getMOVE_SPEED() * indicatorDelegate.getRollingTime() / Math.max(5, Math
                .abs(pullDownY) + Math.abs(pullUpY)));
        period = Math.max(period, 1);
        timer.schedule(indicatorDelegate.isChangeMoveSpeed() ? period : 5);
    }

    /**
     * 马上隐藏
     */
    public void hideImmediately() {
        if (timer != null) {
            timer.cancel();
        }
        if (updateHandler != null) {
            updateHandler = null;
        }
        state = INIT;
        pullUpY = 0;
        pullDownY = 0;
        onActionUp();
        requestLayout();
    }
    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        switch (refreshResult) {
            case SUCCEED:// 刷新成功
                refreshComplete(SUCCEED);
                if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover) {
                    onActionUp();
                }
                break;
            case FAIL:
            default:// 刷新失败
                refreshComplete(FAIL);
                if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover) {
                    onActionUp();
                }
                break;
        }
        new Handler()// 刷新结果停留500毫秒
        {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE, pullDownY);
                if (!isDown)
                    hideScroll();
            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        switch (refreshResult) {
            case SUCCEED:// 加载成功
                loadMoreComplete(SUCCEED);
                if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover) {
                    onActionUp();
                }
                break;
            case FAIL:
            default:// 加载失败
                loadMoreComplete(FAIL);
                if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover) {
                    onActionUp();
                }
                break;
        }
        new Handler()// 刷新结果停留500毫秒
        {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE, -pullUpY);
                if (!isDown)
                    hideScroll();
            }
        }.sendEmptyMessageDelayed(0, 500);
    }

    /**
     * 根据状态处理上拉头，下拉头的动画、显示等
     *
     * @param toState 要变到的状态
     * @param deltaY  Y轴偏移量
     */

    private void changeState(int toState, float deltaY) {
        changeRefreshState(state, toState, deltaY);
        state = toState;
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    public int getState() {
        return state;
    }

    /**
     * 子类请求获取滚动优先权
     *
     * @param requestFirstTouch 优先权
     */
    public void requestFirstTouch(boolean requestFirstTouch) {
        this.requestFirstTouch = requestFirstTouch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        int x = (int) e.getRawX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 首先拦截down事件,记录y坐标
                mLastMotionY = y;
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动< 0是向上运动
                int deltaY = y - mLastMotionY;
                int deltaX = x - mLastMotionX;
                if (Math.abs(deltaX * 3) > Math.abs(deltaY)) {
                    return false;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /*
     * 由父控件决定是否分发事件，防止事件冲突
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (requestFirstTouch) {//交给子类处理ACTION_MOVE
                    return super.dispatchTouchEvent(ev);
                }
                isDown = true;
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (requestFirstTouch) {//交给子类处理ACTION_MOVE
                    return super.dispatchTouchEvent(ev);
                }
                boolean isPullable = contentView instanceof Pullable;
                //Log.e("yy", "ACTION_MOVE:" +" canPullDownFromSet:"+canPullDownFromSet+"  isPullable:"+isPullable+"
                // canPullDown():"+ ((Pullable) contentView).canPullDown()+"  canPullDown:"+canPullDown+"
                // state:"+state);
                if (mEvents == 0) {
                    if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover && (state ==
                            REFRESHING || state == LOADING))
                        break;
                    if (canPullDownFromSet && (!isPullable || ((isPullable) && ((Pullable) contentView).canPullDown()
                    )) && canPullDown && state != LOADING) { //人工设置可下拉&&【(不继承Pullable||(继承Pullable &&view可下拉))
                        // 】&&下拉条件满足&&不是上拉加载状态
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {// 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (canPullUpFromSet && (!isPullable || (isPullable && ((Pullable) contentView).canPullUp
                            ())) && canPullUp && state != REFRESHING) {//人工设置可上拉&&【(不继承Pullable||(继承Pullable
                        // &&view可上拉))】&&上拉条件满足&&不是下拉刷新状态
                        // 可以上拉，正在刷新时不能上拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {// 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else {
                    mEvents = 0;
                }
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                //Log.e("yy", "ACTION_MOVE:" + lastY+"__pullDownY:"+pullDownY+"__pullUpY:"+pullUpY+"   state:"+state);
                requestLayout();//刷新布局 调用onLayout()
                // TODO: 2017/1/10 正在上拉刷新的时候不能下拉刷新,同理正在下拉刷新的时候不能上拉刷新，如需要可以修改
                if (pullDownY > 0 && pullDownY <= refreshDist && (state == INIT || state == RELEASE_TO_REFRESH ||
                        state == MOVING_TO_REFRESH_HEIGHT)) {// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                    changeState(MOVING_TO_REFRESH_HEIGHT, pullDownY);
                }
                if (pullDownY >= refreshDist && (state == INIT || state == MOVING_TO_REFRESH_HEIGHT)) {//
                    // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                    changeState(RELEASE_TO_REFRESH, pullDownY);
                }
                // 下面是判断上拉加载的，同上，注意pullUpY是负值
                if (pullUpY < 0 && -pullUpY <= loadmoreDist && (state == INIT || state == RELEASE_TO_LOAD || state ==
                        MOVING_TO_ONLOADING_HEIGHT)) {
                    changeState(MOVING_TO_ONLOADING_HEIGHT, -pullUpY);
                }
                if (-pullUpY >= loadmoreDist && (state == INIT || state == MOVING_TO_ONLOADING_HEIGHT)) {
                    changeState(RELEASE_TO_LOAD, -pullUpY);
                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {// 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private void onActionUp() {
        isDown = false;
        if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
            // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
            isTouch = false;
        if (state == RELEASE_TO_REFRESH) {
            changeState(REFRESHING, pullDownY);//实际距离很可能比阈值大
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh(this);
        } else if (state == RELEASE_TO_LOAD) {
            changeState(LOADING, -pullUpY);//实际距离很可能比阈值大
            // 加载操作
            if (mListener != null)
                mListener.onLoadMore(this);
        }
        if (pullDownY == 0f && pullUpY == 0f) {
            if (state == DONE || state == MOVING_TO_REFRESH_HEIGHT || state == MOVING_TO_ONLOADING_HEIGHT)
                changeState(INIT, 0);
        } else {
            hide();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {// 这里是第一次进来的时候做一些初始化
            initIndicator();
            if (getChildCount() >= 1) {
                contentView = getChildAt(0);
            } else {
                return;
            }
            refreshView = initRefreshView();//初始化上拉头
            if (refreshView != null) {
                addView(refreshView);
            } else {
                canPullDown = false;
            }
            loadmoreView = initLoadMoreView();//初始化下拉头
            if (loadmoreView != null) {
                addView(loadmoreView);
            } else {
                canPullUp = false;
            }
            isLayout = true;
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        if (pullDownY > 0) {//下拉
            refreshView.bringToFront();
        }
        if (pullUpY < 0) {
            if (null != loadmoreView)
                loadmoreView.bringToFront();
        }
        if (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeader) {
            if (null != emptyView)
                emptyView.bringToFront();
            if (null != contentView)
                contentView.bringToFront();
        }
        int distance = (int) (pullDownY + pullUpY);
        int refreshViewRight = distance - refreshView.getMeasuredHeight();
        int loadmoreViewTop = distance + contentView.getMeasuredHeight();
        int loadmoreViewBottom = loadmoreViewTop + loadmoreView.getMeasuredHeight();
//        Log.e("yy","onLayout:pullDownY"+pullDownY+"  是否:"+(pullDownY >= refreshDist - MOVE_SPEED / 2 && pullDownY
// <= refreshDist + MOVE_SPEED / 2)+"   refreshView.getBottom():"+refreshView.getBottom());
        if (refreshView != null
                && ((!(indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover && state ==
                REFRESHING && (pullDownY >= refreshDist - MOVE_SPEED / 2 && pullDownY <= refreshDist + MOVE_SPEED /
                2))))
                || refreshView.getBottom() < pullDownY)//下拉头部
        {
            refreshView.layout(0, indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeader ? 0 :
                            refreshViewRight,
                    refreshView.getMeasuredWidth(),
                    indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeader ? refreshView
                            .getMeasuredHeight() : distance);
//            Log.e("yy","dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        }
        if (emptyView == null)//中间内容区域
            contentView.layout(0, indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedContent ? 0 :
                            (distance), contentView.getMeasuredWidth(),
                    (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedContent ||
                            (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover &&
                                    pullDownY > 0) ? 0 : (distance)) + contentView.getMeasuredHeight());
        if (loadmoreView != null
                && (!(indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeaderHover && state ==
                LOADING
                && (-pullUpY >= loadmoreDist - MOVE_SPEED / 2 && -pullUpY <= loadmoreDist + MOVE_SPEED / 2)))
                )//上拉头部
            loadmoreView.layout(0, indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeader ? 0 :
                            loadmoreViewTop,
                    loadmoreView.getMeasuredWidth(),
                    indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedHeader ? contentView
                            .getMeasuredHeight() : loadmoreViewBottom);
        if (emptyView != null)//空view
            emptyView.layout(0, indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedContent ? 0 :
                            distance, emptyView.getMeasuredWidth(),
                    (indicatorDelegate.getFixedMode() == IndicatorDelegate.FixedMode.FixedContent ? 0 : distance) +
                            emptyView.getMeasuredHeight());
//				Log.e("yy", "onLayout" + "  top:" + t + "  right:" + r + " bottom:" + b + "__pullDownY:" + pullDownY +
// "__pullUpY:" + pullUpY
//						+ "__contentView.getMeasuredHeight():" + contentView.getMeasuredHeight());
    }

    /**
     * 初始化下拉头
     */
    protected abstract View initRefreshView();

    /**
     * 初始化上拉头
     */
    protected abstract View initLoadMoreView();

    /**
     * 根据状态处理上拉头，下拉头的动画、显示等
     * // INIT:// 下拉布局初始状态,上拉布局初始状态
     * // RELEASE_TO_REFRESH:// 释放刷新状态
     * // REFRESHING:// 正在刷新状态
     * // RELEASE_TO_LOAD:// 释放加载状态
     * // LOADING:// 正在加载状态
     * // DONE:// 加载完成，其实这个已经在刷新失败或刷新成功做了处理，这里不用做特别处理
     * // MOVING_TO_REFRESH_HEIGHT:// 0-->下拉刷新阈值
     * // MOVING_TO_ONLOADING_HEIGHT:// 0-->上拉刷新阈值
     *
     * @param lastState 上一个状态
     * @param toState   要变到的状态
     * @param deltaY    Y轴偏移量
     */
    protected abstract void changeRefreshState(int lastState, int toState, float deltaY);

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     *
     * @param refreshResult BaseAbstractPullToRefreshLayout.SUCCEED代表成功，BaseAbstractPullToRefreshLayout.FAIL代表失败
     */
    protected abstract void refreshComplete(int refreshResult);

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult BaseAbstractPullToRefreshLayout.SUCCEED代表成功，BaseAbstractPullToRefreshLayout.FAIL代表失败
     */
    protected abstract void loadMoreComplete(int refreshResult);

    /**
     * 刷新完成后清除上拉头或下拉头的动画
     */
    protected abstract void clearPullAnimation();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (timer != null) {
            timer.cancel();
        }
        if (updateHandler != null) {
            updateHandler = null;
        }
    }

    public IndicatorDelegate getIndicatorDelegate() {
        return indicatorDelegate;
    }

    public void setIndicatorDelegate(IndicatorDelegate indicatorDelegate) {
        this.indicatorDelegate = indicatorDelegate;
    }

    /**
     * 设置空view,要想监听点击emptyView，先调用setOnClickEmptyViewListener
     *
     * @param emptyView            无数据的view
     * @param forceUseNewEmptyView 强制使用新的emptyView替代已有emptyview
     */
    public void setEmptyView(@NonNull View emptyView, boolean forceUseNewEmptyView) {
        if (this.emptyView == null || forceUseNewEmptyView) {
            if (forceUseNewEmptyView)
                if (this.emptyView != null)
                    removeView(this.emptyView);
            addView(emptyView);
            this.emptyView = emptyView;
            if (onClickEmptyViewListener != null) {
                emptyView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (state == INIT)
                            onClickEmptyViewListener.OnClickEmptyView(BaseAbstractPullToRefreshLayout.this.emptyView);
                    }
                });
            }
        }
    }

    public View getEmptyView() {
        return emptyView;
    }

    /**
     * 设置空view,要想监听点击emptyView，先调用setOnClickEmptyViewListener
     *
     * @param emptyView 无数据的view
     */
    public void setEmptyView(View emptyView) {
        setEmptyView(emptyView, false);
    }

    /**
     * 还原到初始的view ，将emptyview移除
     */
    public void resetView() {
        if (emptyView != null) {
            removeView(emptyView);
            if (onClickEmptyViewListener != null) {
                onClickEmptyViewListener = null;
            }
            emptyView = null;
        }
    }

    /**
     * 自动刷新 布局自动下拉
     *
     * @param timeMillis 刷新持续时间300ms
     */
    public void autoRefresh(@IntRange(from = 10, to = 100000) long timeMillis) {
        indicatorDelegate.setAutoRefreshTimemillis(timeMillis);
        int period = (int) (indicatorDelegate.getMOVE_SPEED() * timeMillis / (indicatorDelegate.getRefreshDistance()));
        period = Math.max(period, 1);
        timer.schedule(indicatorDelegate.isChangeMoveSpeed() ? period : 50, true);
    }

    /**
     * 自动刷新 布局自动下拉 刷新持续时间 默认300ms
     */
    public void autoRefresh() {
        autoRefresh(indicatorDelegate.getAutoRefreshTimemillis());
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //Log.e("yy", "onScrollChanged:" + "  left:" + l + "  top:" + t + "  oldleft:" + oldl + "  oldtop:" + oldt);
    }

    /**
     * 刷新/加载回调接口
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh(BaseAbstractPullToRefreshLayout pullToRefreshLayout);

        /**
         * 加载操作
         */
        void onLoadMore(BaseAbstractPullToRefreshLayout pullToRefreshLayout);
    }

    /**
     * 点击emptyView监听
     */
    public interface OnClickEmptyViewListener {
        void OnClickEmptyView(View emptyView);
    }

    private class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTimerTask mTask;

        private MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        private void schedule(long period) {
            this.schedule(period, false);
        }

        private void schedule(long period, boolean autoRefresh) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTimerTask(handler, autoRefresh);
            timer.schedule(mTask, 0, period);
        }

        private void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        private class MyTimerTask extends TimerTask {
            private Handler handler;
            private boolean autoRefresh;

            private MyTimerTask(Handler handler, boolean autoRefresh) {
                this.handler = handler;
                this.autoRefresh = autoRefresh;
            }

            @Override
            public void run() {
                Message message = handler.obtainMessage();
                if (autoRefresh) {
                    message.arg1 = AUTO_REFRESH;
                }
                handler.sendMessage(message);
            }
        }
    }
}
