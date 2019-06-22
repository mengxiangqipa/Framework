package com.library.adapter.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.library.adapter.R;

/**
 * @author YobertJomi
 * className DefaultLoadMoreView
 * created at  2019/6/23  0:11
 */
public class DefaultLoadMoreView extends LinearLayout implements UniversalRecyclerView.LoadMoreView,
        View.OnClickListener {

    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    private UniversalRecyclerView.OnLoadMoreListener mLoadMoreListener;

    public DefaultLoadMoreView(Context context) {
        this(context, null);
    }

    public DefaultLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        setGravity(Gravity.CENTER);
        setVisibility(GONE);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int minHeight = (int) (displayMetrics.density * 60 + 0.5);
        setMinimumHeight(minHeight);

        inflate(getContext(), R.layout.library_adapter_recycler_view_load_more, this);
        mProgressBar = findViewById(R.id.progress_bar);
        mTvMessage = findViewById(R.id.tv_load_more_message);
        setOnClickListener(this);
    }

    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(R.string.library_adapter_recycler_load_more_message);
    }

    @Override
    public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
        if (!hasMore) {
            setVisibility(VISIBLE);

            if (dataEmpty) {
                mProgressBar.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText(R.string.library_adapter_recycler_data_empty);
            } else {
                mProgressBar.setVisibility(GONE);
                mTvMessage.setVisibility(VISIBLE);
                mTvMessage.setText(R.string.library_adapter_recycler_more_not);
            }
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(UniversalRecyclerView.OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;

        setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(R.string.library_adapter_recycler_click_load_more);
    }

    @Override
    public void onLoadError(int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
        mTvMessage.setVisibility(VISIBLE);
        mTvMessage.setText(TextUtils.isEmpty(errorMessage)
                ? getContext().getString(R.string.library_adapter_recycler_load_error)
                : errorMessage);
    }

    @Override
    public void onClick(View v) {
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
    }
}