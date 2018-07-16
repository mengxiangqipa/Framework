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
 * <p>
 *
 * @author YobertJomi
 * className BaseVideoFragment
 * created at  2017/9/5  10:32
 * <p>
 * Bundle bundle=new Bundle();
 * bundle.putString(BaseVideoFragment.URL,"http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc
 * .yinyuetai" +
 * ".com/D046015255134077DDB3ACA0D7E68D45.flv");
 * bundle.putString(BaseVideoFragment.TITLE,"我是title");
 * bundle.putInt(BaseVideoFragment.HEIGHT,400);
 * bundle.putBoolean(BaseVideoFragment.WITH_CACHE,false);
 * bundle.putBoolean(BaseVideoFragment.SHOW_CLOCK,false);
 * bundle.putBoolean(BaseVideoFragment.SHOW_FULL_SCREENB,false);
 * bundle.putBoolean(BaseVideoFragment.SHOW_BOTTOM,true);
 * bundle.putBoolean(BaseVideoFragment.SHOW_TOP,false);
 * bundle.putBoolean(BaseVideoFragment.AUTO_PLAY,false);
 * baseVideoFragment = BaseVideoFragment.build(bundle);
 * </p>
 */
public class BaseVideoFragment extends Fragment implements CacheListener {

    @BindView(R2.id.videoView)
    BaseCustomVideoViewWithUI baseCustomVideoViewWithUI;

    public static String HEIGHT = "height";
    public static String URL = "url";
    public static String TITLE = "title";
    public static String WITH_CACHE = "WITH_CACHE";
    public static String SHOW_FULL_SCREENB = "SHOW_CLOCK";
    public static String SHOW_CLOCK = "SHOW_CLOCK";
    public static String SHOW_TOP = "SHOW_TOP";
    public static String SHOW_BOTTOM = "SHOW_BOTTOM";
    public static String AUTO_PLAY = "AUTO_PLAY";

    private Context context;

    private Unbinder unbinder;

    private boolean hasInit;

    public static BaseVideoFragment build(Bundle bundle) {
        if (null == bundle || TextUtils.isEmpty(bundle.getString(BaseVideoFragment.URL))) {
            Bundle bundleT = new Bundle();
            bundleT.putString(URL, "http://www.baidu.com");
            return new Builder().bundle(bundleT).build();
        }
        return new Builder().bundle(bundle).build();
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
            if (getArguments().getInt(HEIGHT) > 0) {//设置高度播放高度
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getArguments().getInt(HEIGHT));
                baseCustomVideoViewWithUI.setLayoutParams(params);
            }
            if (getArguments().getBoolean(SHOW_FULL_SCREENB)) {//是否显示右下角全屏按钮
                baseCustomVideoViewWithUI.showFullScreen(true);
            } else {
                baseCustomVideoViewWithUI.showFullScreen(false);
            }
            if (getArguments().getBoolean(SHOW_CLOCK)) {//是否显示右上角系统时间
                baseCustomVideoViewWithUI.showClock(true);
            } else {
                baseCustomVideoViewWithUI.showClock(false);
            }
            if (getArguments().getBoolean(SHOW_TOP)) {//是否显示顶部
                baseCustomVideoViewWithUI.showTopView(true);
            } else {
                baseCustomVideoViewWithUI.showTopView(false);
            }
            if (getArguments().getBoolean(SHOW_BOTTOM)) {//是否显示底部
                baseCustomVideoViewWithUI.showBottomView(true);
            } else {
                baseCustomVideoViewWithUI.showBottomView(false);
            }
            if (getArguments().getBoolean(AUTO_PLAY)) {//是否自动播放
                baseCustomVideoViewWithUI.autoPlay(true);
                if (getArguments().getBoolean(WITH_CACHE)) {
                    HttpProxyCacheServer proxy = HttpProxyCacheServerUtil.getInstance().getProxy(getContext());
                    proxy.registerCacheListener(this, getArguments().getString(URL));
                    String proxyUrl = proxy.getProxyUrl(getArguments().getString(URL));
                    baseCustomVideoViewWithUI.showFullScreen(false);
                    baseCustomVideoViewWithUI.start(proxyUrl);
                } else {
                    baseCustomVideoViewWithUI.start(getArguments().getString(URL));
                }
            } else {
                baseCustomVideoViewWithUI.autoPlay(false);
                baseCustomVideoViewWithUI.setTempUrl(getArguments().getString(URL));
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
        private Bundle bundle;

        public Builder bundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public BaseVideoFragment build() {
            BaseVideoFragment videoFragment = new BaseVideoFragment();
            Bundle bundleT;
            if (bundle == null) {
                bundleT = new Bundle();
            } else {
                bundleT = bundle;
            }
            videoFragment.setArguments(bundleT);
            return videoFragment;
        }
    }
}
