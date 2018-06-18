package com.test.customrefresh;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.utils.ScreenUtils;
import com.library.pulltorefresh.BaseAbstractPullToRefreshLayout;
import com.library.pulltorefresh.IndicatorDelegate;
import com.library.pulltorefresh.storehouse.LocalDisplay;

/**
 * 头部可悬停,其他的跟ClassicalPullToRefreshLayout一样
 *
 * @author YobertJomi
 *         className CustomPullToRefreshLayout
 *         created at  2017/1/9  15:36
 * @see CustomPullToRefreshLayout
 */
public class CustomPullToRefreshLayout extends BaseAbstractPullToRefreshLayout implements BaseAbstractPullToRefreshLayout.OnClickEmptyViewListener {
    /**
     * 下拉头
     */
    private View refreshView;
    /**
     * 正在刷新的图标
     */
    private ImageView refreshingView;
    /**
     * 刷新结果：成功或失败
     */
    private TextView refreshStateTextView;
    /**
     * 上拉头
     */
    private View loadmoreView;
    /**
     * 正在加载的图标
     */
    private ImageView loadingView;

    /**
     * 加载结果：成功或失败
     */
    private TextView loadStateTextView;


    private CirclesDrawable circlesDrawable, circlesDrawableFooter;
    private IndicatorDelegate indicator;//参数设置
    private CharSequence emptyString;

    public CustomPullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initIndicatorDelegate();
    }

    public CustomPullToRefreshLayout(Context context) {
        super(context);
        initView(context);
        initIndicatorDelegate();
    }

    public CustomPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initIndicatorDelegate();
        initView(context);
    }

    private void initIndicatorDelegate() {
        IndicatorDelegate indicator = new IndicatorDelegate();
        indicator.setCanPullDown(true);//设置是否能下拉
        indicator.setCanPullUp(false);//设置是否能上拉
//        indicator.setLoadmoreDistcance(ScreenUtils.getInstance().dip2px(getContext(), 86));//上拉距离px
        indicator.setLoadmoreDistcance(ScreenUtils.getInstance().getScreenHeightPx(getContext())*10/100);//上拉距离px
        indicator.setRefreshDistance(ScreenUtils.getInstance().getScreenHeightPx(getContext())*10/100);//下拉距离px
        indicator.setResistance(1f);//设置阻力系数，内部按正余弦变化，设置可能无效
        indicator.setMOVE_SPEED(1);//设置每次滚动距离
        indicator.setResistanceTime(300);//设置释放回滚的时间
        indicator.setMonitorFinishScroll(false);//刷新完成时监控回滚状态
        indicator.setRollingTime(150);//从释放刷新到后面的时间
        indicator.setFixedMode(IndicatorDelegate.FixedMode.FixedHeaderHover);//设置中间内容区域固定
        setIndicatorDelegate(indicator);
//        refreshView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (refreshView.getHeight() > 0)
//                    com.framework.Utils.Y.y("onGlobalLayout:" + refreshView.getHeight());
//                getIndicatorDelegate().setRefreshDistance(refreshView.getHeight());
//                getIndicatorDelegate().setLoadmoreDistcance(refreshView.getHeight());
//                refreshView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
    }

    private void initView(Context context) {
        // 初始化下拉布局
        refreshView = LayoutInflater.from(getContext()).inflate(R.layout.custom_refresh_head, null, false);
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.refreshStateTextView);
        refreshingView = (ImageView) refreshView.findViewById(R.id.refreshingView);
        circlesDrawable = new CirclesDrawable(context);
//        int[] mColorSchemeColors = new int[]{Color.rgb(0xC9, 0x34, 0x37), Color.rgb(0x37, 0x5B, 0xF1), Color.rgb(0xF7, 0xD2, 0x3E), Color.rgb(0x34, 0xA3, 0x50)};
        //0799FC B6DA53 E78E94 6FCD78
        int[] mColorSchemeColors = new int[]{Color.rgb(0x07, 0x99, 0xFC), Color.rgb(0xB6, 0xDA, 0x53), Color.rgb(0xE7, 0x8E, 0x96), Color.rgb(0x6F, 0xCD, 0x78)};
        circlesDrawable.setColorSchemeColors(mColorSchemeColors);
//        circlesDrawable.start();
        refreshingView.setImageDrawable(circlesDrawable);

        // 初始化上拉布局
        loadmoreView = LayoutInflater.from(getContext()).inflate(R.layout.custom_load_more, null, false);
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadStateTextView);
        loadingView = (ImageView) loadmoreView.findViewById(R.id.loadingView);
        circlesDrawableFooter = new CirclesDrawable(context);
        circlesDrawableFooter.setColorSchemeColors(mColorSchemeColors);
//        circlesDrawable2.start();
        loadingView.setImageDrawable(circlesDrawableFooter);
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
                // 下拉布局初始状态
                if (!circlesDrawable.isRunning())
                    circlesDrawable.start();
                refreshingView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.pull_to_refresh);
                break;
            case MOVING_TO_ONLOADING_HEIGHT:
                // 上拉布局初始状态
                if (!circlesDrawableFooter.isRunning())
                    circlesDrawableFooter.start();
                loadingView.setVisibility(VISIBLE);
                loadStateTextView.setText(R.string.pullup_to_load);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshingView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.release_to_refresh);
                break;
            case REFRESHING:
                // 正在刷新状态
                refreshingView.clearAnimation();
                refreshingView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refreshing);
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadingView.setVisibility(VISIBLE);
                loadStateTextView.setText(R.string.release_to_load);
                break;
            case LOADING:
                // 正在加载状态
                loadingView.setVisibility(View.VISIBLE);
                loadStateTextView.setText(R.string.loading);
                break;
            case DONE:
                if (Math.abs(deltaY) < getIndicatorDelegate().getMOVE_SPEED()) {
                    circlesDrawable.stop();
//                    circlesDrawableFooter.stop();
                }
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
        switch (refreshResult) {
            case SUCCEED:
                // 刷新成功
                refreshingView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_succeed);
                resetView();
                break;
            case FAIL:
            default:
                // 刷新失败
                refreshingView.setVisibility(View.VISIBLE);
                refreshStateTextView.setText(R.string.refresh_fail);
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
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                loadStateTextView.setText(R.string.load_succeed);
                //测试
                resetView();
                break;
            case FAIL:
            default:
                // 加载失败
                loadStateTextView.setText(R.string.load_fail);
                break;
        }
    }

    /**
     * 刷新完成后清除上拉头或下拉头的动画
     */
    @Override
    protected void clearPullAnimation() {
        try {
            refreshingView.clearAnimation();
            loadingView.clearAnimation();
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
        if (null != loadStateTextView && !TextUtils.isEmpty(charSequence)) {
            loadStateTextView.setText(charSequence);
        }
    }

    public void setRefreshContentTextView(CharSequence charSequence) {
        if (null != refreshStateTextView && !TextUtils.isEmpty(charSequence)) {
            refreshStateTextView.setText(charSequence);
        }
    }
}
