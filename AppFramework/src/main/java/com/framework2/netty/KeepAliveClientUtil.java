package com.framework2.netty;

import android.content.Context;

import com.framework.utils.ProcessUtil;
import com.framework.utils.ThreadPoolUtil;
import com.framework.utils.Y;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author YobertJomi
 * className KeepAliveClientUtil
 * created at  2017/9/11  9:15
 */
public class KeepAliveClientUtil {
    public static final String channelActive = "channelActive_demo";
    public static final String channelInactive = "channelInactive_demo";
    public static final String messageReceived = "messageReceived_demo";
    public static final String channelReadComplete = "channelReadComplete_demo";
    public static final String exceptionCaught = "exceptionCaught_demo";
    private static volatile KeepAliveClientUtil instance;
    private static EventLoopGroup group = null;
    // 客户端辅助启动类 对客户端配置
    private static Bootstrap bootstrap = null;
    private static Channel channel;

    public KeepAliveClientUtil() {
    }

    /**
     * 获取实例
     **/
    public static KeepAliveClientUtil getInstance() {
        if (null == instance) {
            synchronized (KeepAliveClientUtil.class) {
                if (null == instance) {
                    instance = new KeepAliveClientUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 连接服务器
     *
     * @param host          连接IP
     * @param port          连接端口
     * @param objectContent 写入内容
     * @throws Exception
     */
    public void connect(final String host, final int port, final Context context, final Object objectContent) throws
            Exception {
        ThreadPoolUtil.getInstanceSingleTaskExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Y.y("getInstanceSingleTaskExecutor进程：" + ProcessUtil.getInstance().getCurrentProcessName(context));
                Y.y("线程名：" + Thread.currentThread().getName() + "线程id：" + Thread.currentThread().getId());
                // 配置客户端NIO线程组
                if (null == group) {
                    group = new NioEventLoopGroup();
                }
                try {
                    // 客户端辅助启动类 对客户端配置
                    if (null == bootstrap) {
                        bootstrap = new Bootstrap();
                        bootstrap.group(group);
                        bootstrap.channel(NioSocketChannel.class);
                        bootstrap.option(ChannelOption.TCP_NODELAY, true);//2017/09/11 保持不掉线
                        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);//2017/09/11 保持不掉线
                        bootstrap.handler(new KeepAliveClientInitializer(context));
                    }
                    if (null == channel || !channel.isOpen()) {
                        Y.y("channel初始化");
                        channel = bootstrap.connect(new InetSocketAddress(host, port))
                                .sync().channel();
                    }
                    if (null == objectContent) {
                        channel.writeAndFlush("首次链接\n");
                    } else {
                        channel.writeAndFlush(objectContent);
                    }
                    channel.read();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Y.y("connect_finally");
//                    group.shutdownGracefully();
                }
            }
        });
    }

    /**
     * 连接服务器
     *
     * @param host          连接IP
     * @param port          连接端口
     * @param objectContent 写入内容
     * @param HeartBeat     毫秒数
     * @throws Exception
     */
    public void connectHeartBeat(final String host, final int port, final Context context, final Object
            objectContent, final int HeartBeat) throws Exception {

        ThreadPoolUtil.getInstanceSingleTaskExecutor().submit(new Runnable() {
            @Override
            public void run() {
//                Utils.showToast("线程名：" + Thread.currentThread().getName() + "线程id：" + Thread.currentThread().getId());
                // 配置客户端NIO线程组
                if (null == group) {
                    group = new NioEventLoopGroup();
                }
                try {
                    // 客户端辅助启动类 对客户端配置
                    if (null == bootstrap) {
                        bootstrap = new Bootstrap();
                        bootstrap.group(group);
                        bootstrap.channel(NioSocketChannel.class);
                        bootstrap.option(ChannelOption.TCP_NODELAY, true);//2017/09/11 保持不掉线
                        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);//2017/09/11 保持不掉线
                        bootstrap.handler(new KeepAliveClientInitializer(context));
                    }
                    if (null == channel) {
                        channel = bootstrap.connect(new InetSocketAddress(host, port))
                                .sync().channel();
                    }
                    if (null == objectContent) {
                        channel.writeAndFlush("首次链接\n");
                    } else
                        channel.writeAndFlush(objectContent);
                    Thread.sleep(HeartBeat);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
//            group.shutdownGracefully();
                }
            }
        });
    }

    public void disConnect() {
        ThreadPoolUtil.getInstanceSingleTaskExecutor().submit(new Runnable() {
            @Override
            public void run() {
                if (null != bootstrap)//先释放bootstrap
                {
                    bootstrap = null;
                }

                if (null != group)//先释放group，再释放channel
                {
                    try {
                        group.shutdownGracefully();
                        group = null;
                        Y.y("group==null;" + (group == null));
                        Y.y("客户端优雅的释放了线程资源...");
                    } catch (Exception e) {
                        Y.y("disConnect_group:" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (null != channel) {
                    try {
                        channel.closeFuture().sync();
                        channel = null;
                        Y.y("channel==null;" + (channel == null));
                        Y.y("等待链接关闭.");
                    } catch (Exception e) {
                        Y.y("disConnect_channel:" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
