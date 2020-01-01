package com.hotpaxos.server;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.netty.handler.DispatcherHandler;
import com.hotpaxos.netty.handler.NioReadBodyHandler;
import com.hotpaxos.netty.handler.TcpServerIdleHandler;
import com.hotpaxos.server.config.HotPaxosServerProps;
import com.hotpaxos.server.decoder.ServerMessageDecoder;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
@Slf4j
public class TcpSeverBootStrap {
    //心跳检测 断连触发器
    private final TcpServerIdleHandler tcpServerIdleHandler;
    //消息体解析
    private final NioReadBodyHandler readBodyHandler;
    //消息分发处理器
    private final DispatcherHandler dispatcherHandler;
    private final ServerBootstrap server = new ServerBootstrap();
    private EventLoopGroup workerGroup;
    private EventLoopGroup bossGroup;

    public TcpSeverBootStrap(HotPaxosServerProps hotPaxosServerProps, ActionDispatcher actionDispatcher, Parser parser, StatisticalNode node) {
        this.tcpServerIdleHandler = new TcpServerIdleHandler();
        this.dispatcherHandler = new DispatcherHandler(parser, actionDispatcher, node);
        this.readBodyHandler = new NioReadBodyHandler(parser, node);
        bossGroup = new NioEventLoopGroup(hotPaxosServerProps.getBossThreads());
        workerGroup = new NioEventLoopGroup(hotPaxosServerProps.getWorkThreads());
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ctx) throws Exception {
                        ChannelPipeline pipeline = ctx.pipeline();
                        //设置事件执行流程
                        pipeline.addLast(new IdleStateHandler(hotPaxosServerProps.getReaderIdleTimeSeconds(), hotPaxosServerProps.getWriterIdleTimeSeconds(), hotPaxosServerProps.getAllIdleTimeSeconds()))
                                .addLast(tcpServerIdleHandler)
                                .addLast(new ServerMessageDecoder())
                                .addLast(readBodyHandler)
                                .addLast(dispatcherHandler);
                    }
                });


    }


    //绑定netty服务器端口信息  启动
    public void connect(int port) {
        try {
            server.bind(new InetSocketAddress(port)).sync();
            log.warn("server bind on port:{}", port);
        } catch (InterruptedException e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.error("tcp server init error", e);
            throw new Error(e);
        }
    }

    //由于当前服务线程是守护进程  无需手动关闭
    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
