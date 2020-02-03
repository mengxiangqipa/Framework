package com.demo.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.demo.commonWebview.CommonFullWebViewActivity;
import com.demo.demo.R;
import com.framework.application.ProxyApplication;
import com.framework.customview.CustomADprogress;
import com.framework.util.ScreenUtil;
import com.framework2.baseEvent.BaseOnClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 闪屏页/广告页
 */
public class SplashActivity extends BaseActivity {
    @BindView(R.id.ivAD)
    ImageView ivAD;
    @BindView(R.id.customADprogress)
    CustomADprogress customADprogress;
    @BindView(R.id.relaAD)
    RelativeLayout relaAD;

    private ScheduledExecutorService scheduledExecutorService;

    private final long AD_DURATIVE = 3 * 1000;

    private final Map<String, Future> futures = new HashMap<>();
    private final String jobID = "jobID";

    private boolean autoJump = true;

    private boolean hasJumped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.getInstance().setSystemUiColorDark(this, false);
        Executors.newScheduledThreadPool(1).schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100*30);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (autoJump) {
                    enterActivity();
                }
            }
        }, AD_DURATIVE, TimeUnit.MILLISECONDS);
        scheduledExecutorService = Executors.newScheduledThreadPool(3);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        relaAD.setVisibility(View.INVISIBLE);
        requestCountDownAD();
    }

    private void enterActivity() {
        if (hasJumped) {
            return;
        }
        hasJumped = true;
        goBackHomepage();
    }

    /**
     * 倒计时广告
     */
    private void requestCountDownAD() {
        String adUrl = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1366794914,754127294&fm=26&gp=0.jpg";
        String jumpUrl = "https://www.baidu.com/index.php?tn=06074089_7_pg";
        int maxShowTime = 5;
        if (!TextUtils.isEmpty(adUrl)) {
            //最小一秒钟
            int showTime = Math.max(maxShowTime, 1);
            showSinglePictrueAD(adUrl, jumpUrl, showTime * 1000);
        } else {
            enterActivity();
        }
    }

    private void showSinglePictrueAD(String url, final String jumpUrl, long durativeTimeMillis) {
        if (!TextUtils.isEmpty(url) && null != ivAD) {
            final long finalDurativeTimeMillis1 = durativeTimeMillis;
            int defaultRes = R.mipmap.splash;
            Glide.with(this).setDefaultRequestOptions(RequestOptions.priorityOf(Priority.HIGH)
                    .placeholder(defaultRes)
                    .error(defaultRes).diskCacheStrategy(DiskCacheStrategy.ALL)).load(url)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            autoJump = false;
                            relaAD.setVisibility(View.VISIBLE);
                            showCountDown(finalDurativeTimeMillis1);
                            if (!TextUtils.isEmpty(jumpUrl)) {
                                ivAD.setOnClickListener(new BaseOnClickListener(1000) {
                                    @Override
                                    protected void onBaseClick(View v) {
                                        destroyExecutorService(futures.get(jobID));
                                        Bundle aboutBundle = new Bundle();
                                        aboutBundle.putString(CommonFullWebViewActivity.TITLE, "");
                                        aboutBundle.putString(CommonFullWebViewActivity.URL, jumpUrl);
                                        aboutBundle.putBoolean(CommonFullWebViewActivity.SHOWRIGHT, false);
                                        Intent intent = new Intent(SplashActivity.this,
                                                CommonFullWebViewActivity.class);
                                        intent.putExtras(aboutBundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                            return false;
                        }
                    }).into(ivAD);
        }
    }

    /**
     * 显示倒计时
     *
     * @param durativeTimeMillis 倒计时毫秒
     */
    private void showCountDown(long durativeTimeMillis) {
        durativeTimeMillis = Math.max(durativeTimeMillis, 1000);
        final long initTimeMillis = System.currentTimeMillis();
        final long finalDurativeTimeMillis = durativeTimeMillis;

        Future future = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ProxyApplication.getProxyHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        float progress =
                                (100F * (System.currentTimeMillis() - initTimeMillis) / finalDurativeTimeMillis);
                        progress = 100F - progress;
                        customADprogress.setProgress(progress <= 0.1F ? 0F : progress);
                        if (progress <= 0.1F) {
                            destroyExecutorService(futures.get(jobID));
                            enterActivity();
                        }
                    }
                });
            }
        }, 50, 10, TimeUnit.MILLISECONDS);
        futures.put(jobID, future);
    }

    private void destroyExecutorService(final Future future) {
        if (null != future) {
            future.cancel(true);
        }
        if (null != scheduledExecutorService) {
            if (!scheduledExecutorService.isShutdown()) {
                scheduledExecutorService.shutdown();
                scheduledExecutorService.shutdownNow();
                scheduledExecutorService = null;
            }
        }
    }

    @OnClick(R.id.customADprogress)
    public void onViewClicked() {
        destroyExecutorService(futures.get(jobID));
        enterActivity();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            int pid = Process.myPid();
            Process.killProcess(pid);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
