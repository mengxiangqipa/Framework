package com.library.pulltorefresh.pullableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.library.pulltorefresh.R;

import java.lang.reflect.Field;

/**
 * @author Administrator 自动加载更多
 */
public class PullableListView2 extends ListView implements Pullable {
    public static final int INIT = 0;
    public static final int LOADING = 1;
    public static final int NO_MORE_DATA = 2;
    // ////////////////////
    // 初始可拉动Y轴方向距离
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 500;
    Context mContext;
    boolean isTouch;
    int mScrollY = 0;
    int lastScrollY = 0;
    boolean isManControl = false;
    boolean canPullUp = true;
    boolean canPullDown = true;
    int backgrounColor;
    float textSize;
    int textColor;
    CharSequence cha;
    CharSequence chaNoMore;
    private OnLoadListener mOnLoadListener;
    private View mLoadmoreView;
    private ImageView mLoadingView;
    private TextView mStateTextView;
    private int state = INIT;
    private boolean canLoad = true;
    private boolean autoLoad = true;
    private boolean hasMoreData = false;
    private Animation mLoadAnim;
    // 实际可上下拉动Y轴上的距离
    private int mMaxYOverscrollDistance;

    public PullableListView2(Context context) {
        super(context);
        initMaxYOverscrollDistance(context);
        mContext = context;
        init(context);
    }

    public PullableListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMaxYOverscrollDistance(context);
        mContext = context;
        init(context);
    }

    public PullableListView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initMaxYOverscrollDistance(context);
        mContext = context;
        init(context);
    }

    private void initMaxYOverscrollDistance(Context mContext) {
        // TODO Auto-generated method stub
        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
        try {
            Class<?> c = Class.forName(AbsListView.class.getName());
            Field egtField = c.getDeclaredField("mEdgeGlowTop");
            Field egbBottom = c.getDeclaredField("mEdgeGlowBottom");
            egtField.setAccessible(true);
            egbBottom.setAccessible(true);
            Object egtObject = egtField.get(this); // this 指的是ListiVew实例
            Object egbObject = egbBottom.get(this);

            // egtObject.getClass() 实际上是一个 EdgeEffect 其中有两个重要属性 mGlow mEdge
            // 并且这两个属性都是Drawable类型
            Class<?> cc = Class.forName(egtObject.getClass().getName());
            Field mGlow = cc.getDeclaredField("mGlow");
            mGlow.setAccessible(true);
            mGlow.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mGlow.set(egbObject, new ColorDrawable(Color.TRANSPARENT));

            Field mEdge = cc.getDeclaredField("mEdge");
            mEdge.setAccessible(true);
            mEdge.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mEdge.set(egbObject, new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) {
        mLoadmoreView = LayoutInflater.from(context).inflate(R.layout.allview_auto_data, null);
        mLoadingView = (ImageView) mLoadmoreView.findViewById(R.id.iv_loading);
        mLoadingView.setImageResource(R.drawable.allview_loading);
        mLoadAnim = AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        mLoadAnim.setInterpolator(lir);
        mStateTextView = (TextView) mLoadmoreView.findViewById(R.id.tv_loadstate);
        mLoadmoreView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击加载
                if (state != LOADING && hasMoreData) {
                    load();
                }
            }
        });
        initData();
        addFooterView(mLoadmoreView, null, false);
        this.setOverScrollMode(ListView.OVER_SCROLL_NEVER);//设置置顶/置底阴影
    }

    /**
     * 是否开启自动加载
     *
     * @param enable true启用，false禁用
     */
    public void setAutoLoad(boolean enable) {
        autoLoad = enable;
    }

    /**
     * 是否显示加载更多
     *
     * @param visible true显示，false不显示
     */
    public void setLoadmoreVisible(boolean visible) {
        if (visible) {
            if (getFooterViewsCount() == 0) {
                addFooterView(mLoadmoreView, null, false);
            }
        } else {
            removeFooterView(mLoadmoreView);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 按下的时候禁止自动加载
                canLoad = false;
                break;
            case MotionEvent.ACTION_UP:
                // 松开手判断是否自动加载
                canLoad = true;
                checkLoad();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 在滚动中判断是否满足自动加载条件
        checkLoad();
    }

    /**
     * 判断是否满足自动加载条件
     */
    private void checkLoad() {
        if (reachBottom() && mOnLoadListener != null && state != LOADING && canLoad && autoLoad && hasMoreData)
            load();
    }

    private void load() {
        if (null != mOnLoadListener) {
            changeState(LOADING);
            mOnLoadListener.onLoad(this);
        }
    }

    private void changeState(int state) {
        this.state = state;
        switch (state) {
            case INIT:
                // mLoadAnim.stop();
                mLoadingView.clearAnimation();
                mLoadingView.setVisibility(View.INVISIBLE);
                mStateTextView.setText("更多");
                if (backgrounColor != 0) {
                    mLoadmoreView.setBackgroundColor(backgrounColor);
                }
                if (textSize > 0) {
                    mStateTextView.setTextSize(textSize);
                }
                if (textColor != 0) {
                    mStateTextView.setTextColor(textColor);
                }
                if (null != cha) {
                    mStateTextView.setText(cha);
                }
                break;

            case LOADING:
                // mLoadAnim.start();
                mLoadingView.startAnimation(mLoadAnim);
                mLoadingView.setVisibility(View.VISIBLE);
                mStateTextView.setText(R.string.loading);
                if (backgrounColor != 0) {
                    mLoadmoreView.setBackgroundColor(backgrounColor);
                }
                if (textSize > 0) {
                    mStateTextView.setTextSize(textSize);
                }
                if (textColor != 0) {
                    mStateTextView.setTextColor(textColor);
                }
                if (null != cha) {
                    mStateTextView.setText(cha);
                }
                break;

            case NO_MORE_DATA:
                // mLoadAnim.stop();
                mLoadingView.clearAnimation();
                mLoadingView.setVisibility(View.INVISIBLE);
                mStateTextView.setText("没有更多的数据了");
                if (backgrounColor != 0) {
                    mLoadmoreView.setBackgroundColor(backgrounColor);
                }
                if (textSize > 0) {
                    mStateTextView.setTextSize(textSize);
                }
                if (textColor != 0) {
                    mStateTextView.setTextColor(textColor);
                }
                if (null != chaNoMore) {
                    mStateTextView.setText(chaNoMore);
                }
                break;
        }
    }

    /**
     * 完成加载
     */
    public void finishLoading() {
        changeState(INIT);
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }

    /**
     * @return footerview可见时返回true，否则返回false
     */
    private boolean reachBottom() {
        if (getCount() == 0) {
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部，且頂部不是第0个，也就是说item数超过一屏后才能自动加载，否则只能点击加载
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getTop() < getMeasuredHeight
                    () && !canPullDown())
                return true;
        }
        return false;
    }

    public boolean getHasMoreData() {
        return hasMoreData;
    }

    public void setHasMoreData(boolean hasMoreData) {
        this.hasMoreData = hasMoreData;
        if (!hasMoreData) {
            changeState(NO_MORE_DATA);
        } else {
            changeState(INIT);
        }
    }

    @Override
    public boolean canPullDown() {
        if (!canPullDown) {
            return false;
        }
        try {
            return getCount() == 0 || (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (!canPullUp) {
            return false;
        }
        try {
            if (getCount() == 0) {
                return true;
            } else if (getLastVisiblePosition() == (getCount() - 1)) {
                if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                        && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <=
                        getMeasuredHeight())
                    return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                   int scrollRangeX, int
            scrollRangeY, int maxOverScrollX, int maxOverScrollY,
                                   boolean isTouchEvent) {
        deltaY =
                (int) Math.abs(deltaY * Math.cos(Math.PI * (deltaY + scrollY) / mMaxYOverscrollDistance));
        mScrollY = scrollY;
        isTouch = isTouchEvent;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY,
                maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onOverScrolled(int scrollX, final int scrollY, boolean clampedX,
                                  boolean clampedY) {
        // TODO Auto-generated method stub
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (!isTouch) {
            scrollTo(0, 0);
            awakenScrollBars();
        }
    }

    /**
     * 手动设置是否能上拉
     *
     * @param canPullUp canPullUp
     */
    public void setCanPullUp(boolean canPullUp) {
        this.canPullUp = canPullUp;
    }

    /**
     * 手动设置是否能下拉
     *
     * @param canPullDown canPullDown
     */
    public void setCanPullDown(boolean canPullDown) {
        this.canPullDown = canPullDown;
    }

    private void initData() {
        // TODO Auto-generated method stub
        if (backgrounColor != 0) {
            mLoadmoreView.setBackgroundColor(backgrounColor);
        }
        if (textSize > 0) {
            mStateTextView.setTextSize(textSize);
        }
        if (textColor != 0) {
            mStateTextView.setTextColor(textColor);
        }
        if (null != cha) {
            mStateTextView.setText(cha);
        }
    }

    public void setLoadMoreBackgroundColor(int color) {
        this.backgrounColor = color;
        initData();
        invalidate();
        invalidate();
    }

    public void setLoadMoreTextSize(float textSize) {
        this.textSize = textSize;
        initData();
        invalidate();
    }

    public void setLoadMoreTextColor(int textColor) {
        this.textColor = textColor;
        initData();
    }

    public void setLoadMoreText(CharSequence cha) {
        this.cha = cha;
        initData();
    }

    public void setLoadMoreTextNoMore(CharSequence chaNoMore) {
        this.chaNoMore = chaNoMore;
        initData();
    }

    /**
     * @author Administrator 自动加载数据
     */
    public interface OnLoadListener {
        /**
         * 自动加载数据的方法
         *
         * @param pullableListView2 pullableListView2
         */
        void onLoad(PullableListView2 pullableListView2);
    }
}