package com.demo.activity.netty_test;

import android.view.View;
import android.widget.Button;

import com.demo.activity.BaseAbsSlideFinishActivity;
import com.demo.activity.BaseAbsSwipeFinishActivity;
import com.demo.configs.EventBusTag;
import com.demo.demo.R;
import com.framework.util.ToastUtil;
import com.framework.util.Y;
import com.framework.widget.CustomTextSwitcher;
import com.framework.widget.OverScrollView;
import com.framework2.netty.KeepAliveClientUtil;
import com.framework2.netty.NettyInfo;
import com.library.swipefinish.SwipeFinishHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import custom.org.greenrobot.eventbus.Subscribe;
import custom.org.greenrobot.eventbus.ThreadMode;

public class NettyTestActivity extends BaseAbsSwipeFinishActivity {
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
        if (info.getType() == NettyInfo.TYPE.MESSAGE_RECEIVED) {
            try {
                tvNettyMsg.makeView();
                tvNettyMsg.next();
                tvNettyMsg.setText(info.getMsg());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void _onCreate() {
        setContentView(R.layout.activity_netty_test);
        ButterKnife.bind(this);
    }

    @Override
    public SwipeFinishHelper.Delegate initDelegate() {
        SwipeFinishHelper.Delegate delegate = new SwipeFinishHelper.Delegate() {
            /**
             * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
             *
             * @return
             */
            @Override
            public boolean isSupportSwipeFinish() {
                return true;
            }

            /**
             * 正在滑动返回
             *
             * @param slideOffset 从 0 到 1
             */
            @Override
            public void onSwipeFinishLayoutSlide(float slideOffset) {
            }

            /**
             * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
             */
            @Override
            public void onSwipeFinishLayoutCancel() {
            }

            /**
             * 滑动返回执行完毕，销毁当前 Activity
             */
            @Override
            public void onSwipeFinishLayoutExecuted() {
                mSwipeFinishHelper.swipeFinishward();
            }
        };
        return delegate;
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
