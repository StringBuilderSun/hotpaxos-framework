package com.hotpaxos.client;

import com.hotpaxos.client.config.HotPaxClientProps;
import com.hotpaxos.client.decoder.ClientMessageDecoder;
import com.hotpaxos.client.heartbeat.HeartBeatHandler;
import com.hotpaxos.client.heartbeat.ReconnectHandler;
import com.hotpaxos.client.heartbeat.TcpClientIdleHandler;
import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.netty.handler.DispatcherHandler;
import com.hotpaxos.netty.handler.NioReadBodyHandler;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * netty 客户端
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class TcpClientBootStrap {
    //客户端连接配置信息
    private final HotPaxClientProps hotPaxClientProps;
    //消息处理器
    private final DispatcherHandler dispatcherHandler;
    //消息处理器
    private final NioReadBodyHandler readBodyHandler;
    //重连处理器
    private final ReconnectHandler reconnectHandler;
    //断连触发器
    private final TcpClientIdleHandler idleHandler;
    //netty客户端
    private final Bootstrap bootstrap;

    public TcpClientBootStrap(HotPaxClientProps hotPaxClientProps,
                              ActionDispatcher dispatcher,
                              Parser parser,
                              StatisticalNode node) {
        this.hotPaxClientProps = hotPaxClientProps;
        this.dispatcherHandler = new DispatcherHandler(parser, dispatcher, node);
        this.readBodyHandler = new NioReadBodyHandler(parser, node);
        this.reconnectHandler = new ReconnectHandler();
        this.idleHandler = new TcpClientIdleHandler();
        EventLoopGroup workerGroup = new NioEventLoopGroup(hotPaxClientProps.getEventLoopThreads());
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, hotPaxClientProps.getConnectTimeOut())
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new IdleStateHandler(hotPaxClientProps.getReaderIdleTimeSeconds(), hotPaxClientProps.getWriterIdleTimeSeconds(), hotPaxClientProps.getAllIdleTimeSeconds()))
                                .addLast(idleHandler)
                                .addLast(new ClientMessageDecoder())
                                .addLast(new HeartBeatHandler(hotPaxClientProps.getPingInterval()))
                                .addLast(reconnectHandler)
                                .addLast(readBodyHandler)
                                .addLast(dispatcherHandler);
                    }
                });

    }
}
