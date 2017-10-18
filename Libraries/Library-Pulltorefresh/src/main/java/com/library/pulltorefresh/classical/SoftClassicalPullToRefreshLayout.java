package com.library.pulltorefresh.classical;

import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.R;
import com.library.pulltorefresh.storehouse.LocalDisplay;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * 头部可悬停,其他的跟ClassicalPullToRefreshLayout一样
 *
 * @author Yangjie
 *         className SoftClassicalPullToRefreshLayout
 *         created at  2017/1/9  15:36
 * @see SoftClassicalPullToRefreshLayout
 */
public class SoftClassicalPullToRefreshLayout extends BaseAbstractPullToRefreshLayout implements BaseAbstractPullToRefreshLayout.OnClickEmptyViewListener {
    // 下拉箭头的转180°动画
    private Animation rotateAnimation;
    // 均匀旋转动画
    private Animation refreshingAnimation;

    /**
     * 下拉头
     */
    private View refreshView;
    /**
     * 下拉的箭头
     */
    private View pullDownView;
    /**
     * 正在刷新的图标
     */
    private View refreshingView;
    /**
     * 刷新结果图标
     */
    private View refreshStateImageView;
    /**
     * 刷新结果：成功或失败
     */
    private TextView refreshStateTextView;
    /**
     * txt:让更新来的更猛烈些吧
     */
    private TextView refreshContentTextView;
    /**
     * 上拉头
     */
    private View loadmoreView;
    /**
     * 上拉的箭头
     */
    private View pullUpView;
    /**
     * 正在加载的图标
     */
    private View loadingView;
    /**
     * 加载结果图标
     */
    private View loadStateImageView;
    /**
     * 加载结果：成功或失败
     */
    private TextView loadStateTextView;
    /**
     * txt:让更新来的更猛烈些吧
     */
    private TextView loadContentTextView;
    private IndicatorDelegate indicator;//参数设置
    private CharSequence emptyString;

    public SoftClassicalPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initIndicatorDelegate();
    }

    public SoftClassicalPullToRefreshLayout(Context context) {
        super(context);
        initView(context);
        initIndicatorDelegate();
    }

    public SoftClassicalPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initIndicatorDelegate();
        initView(context);
    }

    private void initIndicatorDelegate() {
        IndicatorDelegate indicator = new IndicatorDelegate();
        indicator.setCanPullDown(true);//设置是否能下拉
        indicator.setCanPullUp(true);//设置是否能上拉
        indicator.setLoadmoreDistcance(150);//上拉距离px
        indicator.setRefreshDistance(150);//下拉距离px
        indicator.setResistance(1f);//设置阻力系数，内部按正余弦变化，设置可能无效
        indicator.setMOVE_SPEED(1);//设置每次滚动距离
        indicator.setResistanceTime(300);//设置释放回滚的时间
        indicator.setMonitorFinishScroll(false);//刷新完成时监控回滚状态
        indicator.setRollingTime(150);//从释放刷新到后面的时间
        indicator.setFixedMode(IndicatorDelegate.FixedMode.FixedNothing);//设置中间内容区域固定
        setIndicatorDelegate(indicator);
    }

    private void initView(Context context) {
        Log.i("yy", "initView");
        // 初始化下拉布局
        refreshView = LayoutInflater.from(getContext()).inflate(R.layout.allview_refresh_head_soft_classical, null, false);
        pullDownView = refreshView.findViewById(R.id.pulldown_icon);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.tv_state);
        refreshContentTextView = (TextView) refreshView.findViewById(R.id.tv_content);
        refreshingView = refreshView.findViewById(R.id.refreshing_icon);
        refreshStateImageView = refreshView.findViewById(R.id.iv_state);
        // 初始化上拉布局
        loadmoreView = LayoutInflater.from(getContext()).inflate(R.layout.allview_load_more_classical, null, false);
        pullUpView = loadmoreView.findViewById(R.id.pullup_icon);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.tv_loadstate);
        loadContentTextView = (TextView) loadmoreView.findViewById(R.id.tv_content);
        loadingView = loadmoreView.findViewById(R.id.loading_icon);
        loadStateImageView = loadmoreView.findViewById(R.id.iv_loadstate);

        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    /**
     * 初始化上拉头
     *
     * @return initRefreshView
     */
    @Override
    protected View initRefreshView() {
        return refreshView;
    }

    /**
     * 初始化下拉头
     *
     * @return initLoadMoreView
     */
    @Override
    protected View initLoadMoreView() {
        return loadmoreView;
    }


    /**
     * 各种改变状态的，动画等
     *
     * @param toState 要变到的状态
     */
    @Override
    protected void changeRefreshState(int lastState, int toState, float deltaY) {
        switch (toState) {
            case INIT:
            case MOVING_TO_REFRESH_HEIGHT:
                //			Log.e("yy", "MOVING_TO_REFRESH_HEIGHT");
            case MOVING_TO_ONLOADING_HEIGHT:
                // 下拉布局初始状态
                refreshingView.setVisibility(View.INVISIBLE);
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                pullDownView.clearAnimation();
                pullDownView.setVisibility(View.VISIBLE);
                // 上拉布局初始状态
                loadingView.setVisibility(INVISIBLE);
                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.pullup_to_load);
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshingView.setVisibility(View.INVISIBLE);
                refreshStateImageView.setVisibility(View.GONE);
                refreshStateTextView.setText(R.string.release_to_refresh);
                pullDownView.startAnimation(rotateAnimation);
                pullDownView.setVisibility(View.VISIBLE);
                break;
            case REFRESHING:
                // 正在刷新状态
                pullDownView.clearAnimation();
                pullDownView.setVisibility(View.INVISIBLE);
                refreshStateImageView.setVisibility(View.GONE);
                refreshingView.setVisibility(View.VISIBLE);
                refreshingView.startAnimation(refreshingAnimation);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadingView.setVisibility(INVISIBLE);
                loadStateImageView.setVisibility(View.GONE);
                loadStateTextView.setText(R.string.release_to_load);
                pullUpView.startAnimation(rotateAnimation);
                pullUpView.setVisibility(VISIBLE);
                break;
            case LOADING:
                // 正在加载状态
                pullUpView.clearAnimation();
                pullUpView.setVisibility(View.INVISIBLE);
                loadStateImageView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText(R.string.loading);
                break;
        }
    }

    /**
     * 下拉刷新完成
     *
     * @param refreshResult BaseAbstractPullToRefreshLayout.SUCCEED代表成功，BaseAbstractPullToRefreshLayout.FAIL代表失败
     */
    @Override
    protected void refreshComplete(int refreshResult) {
        refreshingView.clearAnimation();
        refreshingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_succeed);
                refreshStateImageView.setBackgroundResource(R.drawable.allview_refresh_succeed);
                resetView();
                break;
            case FAIL:
            default:
                // 刷新失败
                refreshStateImageView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_fail);
                refreshStateImageView.setBackgroundResource(R.drawable.allview_refresh_failed);
                //
                showEmptyView();
                break;
        }
    }

    public void showEmptyView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.allview_empty_view, null);
        if (!TextUtils.isEmpty(emptyString)) {
            ((TextView) inflate.findViewById(R.id.tv_empty)).setText(emptyString);
        }
        inflate.setMinimumWidth(LocalDisplay.getScreenWidthPixels(getContext()));
        inflate.setMinimumHeight(LocalDisplay.getScreenHeightPixels(getContext()));
        setOnClickEmptyViewListener(this);//先设置监听器，再addview
        setEmptyView(inflate);
    }

    /**
     * 上拉刷新 完成
     *
     * @param refreshResult BaseAbstractPullToRefreshLayout.SUCCEED代表成功，BaseAbstractPullToRefreshLayout.FAIL代表失败
     */
    @Override
    protected void loadMoreComplete(int refreshResult) {
        loadingView.clearAnimation();
        loadingView.setVisibility(View.GONE);
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_succeed);
                loadStateImageView.setBackgroundResource(R.drawable.allview_load_succeed);
                //测试
                resetView();
                break;
            case FAIL:
            default:
                // 加载失败
                loadStateImageView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.load_fail);
                loadStateImageView.setBackgroundResource(R.drawable.allview_load_failed);
                break;
        }
    }

    /**
     * 刷新完成后清除上拉头或下拉头的动画
     */
    @Override
    protected void clearPullAnimation() {
        try {
            pullDownView.clearAnimation();
            pullUpView.clearAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnClickEmptyView(View emptyView) {
        autoRefresh(500);
    }

    public void setEmptyTxt(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            emptyString = charSequence;
        }
    }

    public void setLoadContentTextView(CharSequence charSequence) {
        if (null != loadContentTextView && !TextUtils.isEmpty(charSequence)) {
            loadContentTextView.setText(charSequence);
        }
    }

    public void setRefreshContentTextView(CharSequence charSequence) {
        if (null != refreshContentTextView && !TextUtils.isEmpty(charSequence)) {
            refreshContentTextView.setText(charSequence);
        }
    }
}
