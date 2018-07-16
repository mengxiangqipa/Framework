package com.framework2.netty;

import android.content.Context;

import com.framework.utils.Y;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class KeepAliveClientInitializer extends ChannelInitializer<SocketChannel> {
    private Context context;

    public KeepAliveClientInitializer(Context context) {
        this.context = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        Y.y("KeepAliveClientInitializer---initChannel--- " + ch.toString());
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder(
                CharsetUtil.UTF_8));
        pipeline.addLast("encoder", new StringEncoder(
                CharsetUtil.UTF_8));
        pipeline.addLast("handler", new KeepAliveClientHandler(context));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Y.y("KeepAliveClientInitializer---channelRead--- msg=" + msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        Y.y("KeepAliveClientInitializer---channelReadComplete---");
        super.channelReadComplete(ctx);
    }
}
