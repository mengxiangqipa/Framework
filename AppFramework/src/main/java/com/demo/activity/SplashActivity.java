package com.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.demo.R;
import com.framework.utils.ActivityTaskUtil;
import com.framework.utils.ScreenUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_guide)
    ImageView ivGuide;
    @BindView(R.id.tvTimer)
    TextView tvTimer;
    private MyCountDownTimer myCountDownTimer;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = "newMessage")
    public void receivedNewMessage(String info) {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityTaskUtil.getInstance().addActivity(this);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        myCountDownTimer = new MyCountDownTimer(3000, 100);
        myCountDownTimer.start();
        tvTimer.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tvTimer)
    public void onViewClicked() {
        gotoHomepage();
    }

    private void gotoHomepage() {
        if (null != myCountDownTimer) {
            myCountDownTimer.cancel();
            myCountDownTimer = null;
        }
        Intent intent = new Intent(SplashActivity.this, HomePageActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
        finish();
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            gotoHomepage();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            int seconds = (int) Math.round(millisUntilFinished / 1000d);
            if (seconds > 0)
                tvTimer.setText("跳过" + (int) Math.ceil(millisUntilFinished / 1000d) + "s");
        }
    }
}
