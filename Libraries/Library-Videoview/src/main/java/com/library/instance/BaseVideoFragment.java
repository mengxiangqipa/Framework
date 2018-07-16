package com.library.instance;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.library.videoview.BaseCustomVideoViewWithUI;
import com.library.videoview.R;
import com.library.videoview.R2;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author YobertJomi
 * className BaseVideoFragment
 * created at  2017/9/5  10:32
 */
public class BaseVideoFragment extends Fragment implements CacheListener {

    @BindView(R2.id.videoView)
    BaseCustomVideoViewWithUI baseCustomVideoViewWithUI;

    public static String HEIGHT = "height";
    public static String URL = "url";
    public static String TITLE = "title";
    public static String WITH_CACHE = "with_cache";

    private Context context;

    private Unbinder unbinder;

    private boolean hasInit;

    public static BaseVideoFragment build(String url, String title, int height) {
        return new Builder().url(url).title(title).height(height).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videoview_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!hasInit) {
            hasInit = true;
            if (getArguments().getInt(HEIGHT) > 0) {
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getArguments().getInt(HEIGHT));
                baseCustomVideoViewWithUI.setLayoutParams(params);
            }
            if (getArguments().getBoolean(WITH_CACHE)) {
                HttpProxyCacheServer proxy = HttpProxyCacheServerUtil.getInstance().getProxy(getContext());
                proxy.registerCacheListener(this, getArguments().getString(URL));
                String proxyUrl = proxy.getProxyUrl(getArguments().getString(URL));
                baseCustomVideoViewWithUI.start(proxyUrl);
            } else {
                baseCustomVideoViewWithUI.start(getArguments().getString(URL));
            }
            baseCustomVideoViewWithUI.setTitle(getArguments().getString(TITLE));
        }
        initEvent();
        return view;
    }

    private void initEvent() {

    }

    @Override
    public void onPause() {
        super.onPause();
        baseCustomVideoViewWithUI.resetVolumAndBrightness();
    }

    @Override
    public void onResume() {
        super.onResume();
        baseCustomVideoViewWithUI.resumeVolumAndBrightness();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int currentPosition = baseCustomVideoViewWithUI.getVideoView().getCurrentPosition();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            baseCustomVideoViewWithUI.setFullScreen();
        } else {
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
// .MATCH_PARENT, ScreenUtils.getInstance().getStatusBarHeightPx(VideoViewPlayingActivity.this));
//            titleTemp.setLayoutParams(layoutParams);
            baseCustomVideoViewWithUI.setNormalScreen();
        }
        baseCustomVideoViewWithUI.seekTo(currentPosition);
        super.onConfigurationChanged(newConfig);
    }

    public void onBackPressed_() {
        try {
            baseCustomVideoViewWithUI.onBackClicked();
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            baseCustomVideoViewWithUI.resetVolumAndBrightness();
            baseCustomVideoViewWithUI.stopPlayback();
            HttpProxyCacheServerUtil.getInstance().getProxy(context).unregisterCacheListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unbinder.unbind();
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        baseCustomVideoViewWithUI.setSecondaryProgress(percentsAvailable);
    }

    public static class Builder {
        private String url;
        private String title;
        private int height;
        private boolean withCache;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }
        public Builder withCache(boolean withCache) {
            this.withCache = withCache;
            return this;
        }

        public BaseVideoFragment build() {
            BaseVideoFragment videoFragment = new BaseVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putString(URL, TextUtils.isEmpty(url) ? "" : url);
            bundle.putString(TITLE, TextUtils.isEmpty(title) ? "" : title);
            bundle.putInt(HEIGHT, height);
            bundle.putBoolean(WITH_CACHE, withCache);
            videoFragment.setArguments(bundle);
            return videoFragment;
        }
    }
}
