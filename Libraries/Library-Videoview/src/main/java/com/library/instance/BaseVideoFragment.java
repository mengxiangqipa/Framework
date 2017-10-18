package com.library.instance;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 *         className BaseVideoFragment
 *         created at  2017/9/5  10:32
 */
public class BaseVideoFragment extends Fragment implements CacheListener
{

    @BindView(R2.id.videoView)
    BaseCustomVideoViewWithUI baseCustomVideoViewWithUI;

    private Context context;

    private Unbinder unbinder;


    private boolean hasInit;


    public static BaseVideoFragment build(String url)
    {
        return new Builder().url(url).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View  view = inflater.inflate(R.layout.fragment_videoview_library, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (!hasInit)
        {
            hasInit = true;
            HttpProxyCacheServer proxy = HttpProxyCacheServerUtil.getInstance().getProxy(getContext());
            proxy.registerCacheListener(this, getArguments().getString("url"));
            String proxyUrl = proxy.getProxyUrl(getArguments().getString("url"));
            baseCustomVideoViewWithUI.start(proxyUrl);
        }
        initEvent();
        return view;
    }

    private void initEvent()
    {

    }


    @Override
    public void onPause()
    {
        super.onPause();
        baseCustomVideoViewWithUI.resetVolumAndBrightness();
    }


    @Override
    public void onResume()
    {
        super.onResume();
        baseCustomVideoViewWithUI.resumeVolumAndBrightness();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        int currentPosition = baseCustomVideoViewWithUI.getVideoView().getCurrentPosition();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            baseCustomVideoViewWithUI.setFullScreen();
        } else
        {
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getInstance().getStatusBarHeightPx(VideoViewPlayingActivity.this));
//            titleTemp.setLayoutParams(layoutParams);
            baseCustomVideoViewWithUI.setNormalScreen();
        }
        baseCustomVideoViewWithUI.seekTo(currentPosition);
        super.onConfigurationChanged(newConfig);
    }


    public void onBackPressed_()
    {
        try
        {
            baseCustomVideoViewWithUI.onBackClicked();
        } catch (Exception e)
        {
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        try
        {
            baseCustomVideoViewWithUI.resetVolumAndBrightness();
            baseCustomVideoViewWithUI.stopPlayback();
            HttpProxyCacheServerUtil.getInstance().getProxy(context).unregisterCacheListener(this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        unbinder.unbind();
    }


    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable)
    {
        baseCustomVideoViewWithUI.setSecondaryProgress(percentsAvailable);
    }

    public static class Builder
    {
        private String url;

        public Builder url(String url)
        {
            this.url = url;
            return this;
        }

        public BaseVideoFragment build()
        {
            BaseVideoFragment videoFragment = new BaseVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", TextUtils.isEmpty(url) ? "" : url);
            videoFragment.setArguments(bundle);
            return videoFragment;
        }
    }
}
