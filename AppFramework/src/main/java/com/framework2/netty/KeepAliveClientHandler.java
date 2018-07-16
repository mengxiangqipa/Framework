package com.framework2.netty;

import android.content.Context;

import com.demo.configs.EventBusTag;
import com.framework.utils.ToastUtil;
import com.framework.utils.Y;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class KeepAliveClientHandler extends SimpleChannelInboundHandler<Object> {
    Context context;

    public KeepAliveClientHandler(Context context) {
        this.context = context;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Y.y("KeepAliveClientHandler-Client active:" + ctx.toString());
        NettyInfo info = new NettyInfo();
        info.setType(NettyInfo.TYPE.CHANNEL_ACTIVE);
        EventBus.getDefault().post(info, EventBusTag.nettyMsg);
//        Intent intent = new Intent();
//        intent.setAction(KeepAliveClientUtil.channelActive);
//        context.sendBroadcast(intent);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Y.y("KeepAliveClientHandler-Client close:" + ctx.toString());
        NettyInfo info = new NettyInfo();
        info.setType(NettyInfo.TYPE.CHANNEL_INACTIVE);
        EventBus.getDefault().post(info, EventBusTag.nettyMsg);
//        Intent intent = new Intent();
//        intent.setAction(KeepAliveClientUtil.channelInactive);
//        context.sendBroadcast(intent);
        super.channelInactive(ctx);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyInfo info = new NettyInfo();
        info.setMsg((String) msg);
        info.setType(NettyInfo.TYPE.MESSAGE_RECEIVED);
        EventBus.getDefault().post(info, EventBusTag.nettyMsg);
        ToastUtil.getInstance().showToast("messageReceived：" + (String) msg);
        Y.y("KeepAliveClientHandler-messageReceived：" + (String) msg);
//        Intent intent = new Intent();
//        intent.setAction(KeepAliveClientUtil.messageReceived);
//        intent.putExtra("msg", (String) msg);
//        context.sendBroadcast(intent);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Y.y("KeepAliveClientHandler-channelReadComplete");
        super.channelReadComplete(ctx);
        NettyInfo info = new NettyInfo();
        info.setType(NettyInfo.TYPE.CHANNEL_READCOMPLETE);
        EventBus.getDefault().post(info, EventBusTag.nettyMsg);
//        Intent intent = new Intent();
//        intent.setAction(KeepAliveClientUtil.channelReadComplete);
//        context.sendBroadcast(intent);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Y.y("KeepAliveClientHandler-exceptionCaught-客户端异常退出");
        super.exceptionCaught(ctx, cause);
        NettyInfo info = new NettyInfo();
        info.setType(NettyInfo.TYPE.EXCEPTION_CAUGHT);
        EventBus.getDefault().post(info, EventBusTag.nettyMsg);
//        Intent intent = new Intent();
//        intent.setAction(KeepAliveClientUtil.exceptionCaught);
//        context.sendBroadcast(intent);
    }
}