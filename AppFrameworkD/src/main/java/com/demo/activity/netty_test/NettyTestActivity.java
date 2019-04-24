package com.demo.activity.netty_test;

import android.view.View;
import android.widget.Button;

import com.demo.activity.BaseSlideFinishActivity;
import com.demo.configs.EventBusTag;
import com.demo.demo.R;
import com.framework.customviews.CustomTextSwitcher;
import com.framework.customviews.OverScrollView;
import com.framework.utils.ScreenUtils;
import com.framework.utils.ToastUtil;
import com.framework.utils.Y;
import com.framework2.netty.KeepAliveClientUtil;
import com.framework2.netty.NettyInfo;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

public class NettyTestActivity extends BaseSlideFinishActivity {
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnEnd)
    Button btnEnd;
    @BindView(R.id.btnRetry)
    Button btnRetry;
    @BindView(R.id.tvNettyMsg)
    CustomTextSwitcher tvNettyMsg;
    @BindView(R.id.overScrollView)
    OverScrollView overScrollView;
    private String host = "192.168.0.101";
    private int port = 8090;

    //eventBus通知新消息
    @Subscribe(threadMode = ThreadMode.MAIN, tag = EventBusTag.nettyMsg)
    public void receivedNettyMessage(NettyInfo info) {
        if (info.getType() == NettyInfo.TYPE.MESSAGE_RECEIVED)
            try {
                tvNettyMsg.makeView();
                tvNettyMsg.next();
                tvNettyMsg.setText(info.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    public void _onCreate() {
        setContentView(R.layout.activity_netty_test);
        ButterKnife.bind(this);
        ScreenUtils.getInstance().setTranslucentStatus(this, true);
        ScreenUtils.getInstance().setStatusBarTintColor(this,
                getResources().getColor(R.color.white));
    }

    @OnClick({R.id.btnStart, R.id.btnEnd, R.id.btnRetry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                ToastUtil.getInstance().showToast("netty开始连接");
                try {
                    KeepAliveClientUtil.getInstance().connect(host, port, NettyTestActivity.this,
                            "我是Android-Netty测试数据" + "\n");
                } catch (Exception e) {
                    Y.y("netty开始连接-Exception:" + e.getMessage());
                    e.printStackTrace();
                }
                break;
            case R.id.btnEnd:
                ToastUtil.getInstance().showToast("netty断开连接");
                KeepAliveClientUtil.getInstance().disConnect();
                break;
            case R.id.btnRetry:
                ToastUtil.getInstance().showToast("netty重试...");
                try {
                    KeepAliveClientUtil.getInstance().connect(host, port, NettyTestActivity.this,
                            "我是Android-Netty测试数据--重试" + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
