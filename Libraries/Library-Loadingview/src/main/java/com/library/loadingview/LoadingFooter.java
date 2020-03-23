package com.library.loadingview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingFooter extends RelativeLayout {

    protected State mState = State.Normal;
    private View mLoadingView;
    private View mNetworkErrorView;
    private View mTheEndView;
    private LoadingIndicatorView mLoadingProgress;
    private TextView mLoadingText;
    private TextView mNoMoreText;
    private TextView mNoNetWorkText;
    private String loadingHint;
    private String noMoreHint;
    private String noNetWorkHint;

    public LoadingFooter(Context context) {
        super(context);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.footer, this);
        setOnClickListener(null);
        setState(State.Normal, true);
    }

    public void setLoadingHint(String hint) {
        loadingHint = hint;
    }

    public void setNoMoreHint(String hint) {
        noMoreHint = hint;
    }

    public void setNoNetWorkHint(String hint) {
        noNetWorkHint = hint;
    }

    public void setProgressStyle(@NonNull Class cls) {
        if (null != mLoadingProgress) {
            mLoadingProgress.setIndicator(cls);
        }
    }

    /**
     * 需要在setState之后调用 才能生效
     *
     * @param color color
     */
    public void setIndicatorColor(@ColorInt int color) {
        if (mLoadingProgress != null) {
            mLoadingProgress.setIndicatorColor(color);
        }
        if (mLoadingText != null) {
            mLoadingText.setTextColor(color);
        }
        if (mNoMoreText != null) {
            mNoMoreText.setTextColor(color);
        }
        if (mNoNetWorkText != null) {
            mNoNetWorkText.setTextColor(color);
        }
    }

    /**
     * 需要在setState之后调用 才能生效
     *
     * @param height px
     */
    public void setIndicatorWidth(int height) {
        mLoadingProgress.setWidth(height);
    }

    /**
     * 需要在setState之后调用 才能生效
     *
     * @param height px
     */
    public void setIndicatorHeight(int height) {
        mLoadingProgress.setHeight(height);
    }

    public State getState() {
        return mState;
    }

    public void setState(State status) {
        setState(status, true);
    }

    /**
     * 设置状态
     *
     * @param status   State
     * @param showView 是否展示当前View
     */
    public void setState(State status, boolean showView) {
        if (mState == status) {
            return;
        }
        mState = status;

        switch (status) {

            case Normal:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                break;
            case Loading:
                setOnClickListener(null);
                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                if (mLoadingView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.loading_viewstub);
                    mLoadingView = viewStub.inflate();

                    mLoadingProgress =
                            (LoadingIndicatorView) mLoadingView.findViewById(R.id.loading_progress);
                    mLoadingText = (TextView) mLoadingView.findViewById(R.id.loading_text);
                } else {
                    mLoadingView.setVisibility(VISIBLE);
                }

                mLoadingView.setVisibility(showView ? VISIBLE : GONE);

                mLoadingProgress.setVisibility(View.VISIBLE);
                mLoadingText.setText(TextUtils.isEmpty(loadingHint) ?
                        getResources().getString(R.string
                        .footer_loading) : loadingHint);

                break;
            case NoMore:
                setOnClickListener(null);
                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mNetworkErrorView != null) {
                    mNetworkErrorView.setVisibility(GONE);
                }

                if (mTheEndView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.end_viewstub);
                    mTheEndView = viewStub.inflate();

                    mNoMoreText = (TextView) mTheEndView.findViewById(R.id.loading_end_text);
                } else {
                    mTheEndView.setVisibility(VISIBLE);
                }

                mTheEndView.setVisibility(showView ? VISIBLE : GONE);
                mNoMoreText.setText(TextUtils.isEmpty(noMoreHint) ?
                        getResources().getString(R.string.footer_end) :
                        noMoreHint);
                break;
            case NetWorkError:

                if (mLoadingView != null) {
                    mLoadingView.setVisibility(GONE);
                }

                if (mTheEndView != null) {
                    mTheEndView.setVisibility(GONE);
                }

                if (mNetworkErrorView == null) {
                    ViewStub viewStub = (ViewStub) findViewById(R.id.network_error_viewstub);
                    mNetworkErrorView = viewStub.inflate();
                    mNoNetWorkText =
                            (TextView) mNetworkErrorView.findViewById(R.id.network_error_text);
                } else {
                    mNetworkErrorView.setVisibility(VISIBLE);
                }

                mNetworkErrorView.setVisibility(showView ? VISIBLE : GONE);
                mNoNetWorkText.setText(TextUtils.isEmpty(noNetWorkHint) ?
                        getResources().getString(R.string.footer_network_error) : noNetWorkHint);

                break;
            default:
                break;
        }
    }

    public enum State {
        /**
         * 正常
         */
        Normal
        /**加载到最底了*/
        , NoMore
        /**加载中..*/
        , Loading
        /**网络异常*/
        , NetWorkError
    }
}