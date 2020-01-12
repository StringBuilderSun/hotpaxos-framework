package com.hotpaxos.client;

import com.hotpaxos.client.config.HotPaxClientProps;
import com.hotpaxos.client.decoder.ClientMessageDecoder;
import com.hotpaxos.client.heartbeat.HeartBeatHandler;
import com.hotpaxos.client.heartbeat.ReconnectHandler;
import com.hotpaxos.client.heartbeat.TcpClientIdleHandler;
import com.hotpaxos.client.retry.RetryFactory;
import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.core.retry.Retry;
import com.hotpaxos.framework.common.core.retry.RetryPolicy;
import com.hotpaxos.framework.common.core.retry.RetrySleeper;
import com.hotpaxos.framework.common.propertychange.HotPaxPropertyChangeManagerSupport;
import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.ServiceNode;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import com.hotpaxos.framework.common.utils.PathUtils;
import com.hotpaxos.netty.handler.DispatcherHandler;
import com.hotpaxos.netty.handler.NioReadBodyHandler;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeListenerProxy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty 客户端
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
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
    private final Map<String, AtomicInteger> tids = PlatformDependent.newConcurrentHashMap();

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


    //连接zk上所有服务节点
    public void newChannel(List<ServiceNode> serviceNodes) {
        for (ServiceNode serviceNode : serviceNodes) {
            newChannel(serviceNode);
        }
    }

    public void newChannel(ServiceNode serviceNode) {
        //根据客户端配置的连接数 与服务端建立连接
        while (hotPaxClientProps.getChannelSize() > tids.computeIfAbsent(serviceNode.hostAndPort(), (k) -> new AtomicInteger(0)).get()) {
            //建立连接
            Channel channel = newChannel(serviceNode.getHost(), serviceNode.getPort());
            //更改连接数
            initTid(channel, serviceNode.hostAndPort());
            //初始化重试策略
            initRetry(channel, serviceNode);
        }
    }

    //设置重试策略
    private void initRetry(Channel channel, ServiceNode serviceNode) {
        RetryPolicy retryPolicy = RetryPolicy.builder()
                .host(serviceNode.getHost())
                .port(serviceNode.getPort())
                .maxRetryCount(hotPaxClientProps.getMaxReconnectRetryCount())
                .reConnectSleepTimeMs(hotPaxClientProps.getReconnectSleepTimeMs())
                .retryType(hotPaxClientProps.getRetryType())
                .build();
        channel.attr(HotpaxosConstans.RETRY_POLICY_KEY).compareAndSet(null, retryPolicy);
    }

    private void initTid(Channel channel, String hostAndPort) {
        channel.attr(HotpaxosConstans.TID_KEY).compareAndSet(null, this.tids.get(hostAndPort).incrementAndGet());
    }

    public synchronized Channel newChannel(String host, int port) {
        ChannelFuture future = bootstrap.connect(host, port);
        //添加监听器
        addPropertyChangeListener(future.channel());
        future.addListener(f -> {
            if (!f.isSuccess()) {
                try {
                    log.info("连接不成功...尝试重连.....");
                    //一直未收到服务的响应数据  再次连接 会触发addPropertyChangeListener中定义重连
                    HotPaxPropertyChangeManagerSupport.getInstance().
                            pushPropsEvent(
                                    PathUtils.toReConnectName(future.channel()),
                                    future.channel());

                } finally {
                    //重连成功后移除属性监听器
                    HotPaxPropertyChangeManagerSupport.getInstance().removePropertyChangeListener(PathUtils.toReConnectName(future.channel()));
                }
            } else {
                log.info("连接成功.....");
                //连接成功重置 连接重试时间
                resetReConnectCount(future.channel());

            }
        });
        return future.channel();
    }

    private void resetReConnectCount(Channel channel) {
        RetryPolicy retryPolicy = channel.attr(HotpaxosConstans.RETRY_POLICY_KEY).get();
        if (null != retryPolicy) {
            retryPolicy.resetReconnectRetryCount();
        }
    }

    //添加重试监听器
    private void addPropertyChangeListener(Channel channel) {
        String propertyName = PathUtils.toReConnectName(channel);
        HotPaxPropertyChangeManagerSupport.getInstance().addPropertyChangeListener(new PropertyChangeListenerProxy(propertyName, evt -> {
            //订阅了该propertyName的服务会使用到下面的逻辑处理  newChannel 触发重连
            Object newValue = evt.getNewValue();
            if (newValue instanceof Channel) {
                Channel reChannel = (Channel) newValue;
                RetryPolicy retryPolicy = reChannel.attr(HotpaxosConstans.RETRY_POLICY_KEY).get();
                Retry retry = RetryFactory.apply(retryPolicy);
                if (null == retry) {
                    log.warn("try to reConnect server but Retry policy can find.");
                    return;
                }
                //是否允许重试
                boolean allowRetry = retry.allowRetry(retryPolicy.getMaxRetryCount(), 0, RetrySleeper.DEFAULT_RETRY_SLEEPER);
                if (allowRetry) {
                    log.debug("Try to the {} times reconnect server,Retry address:{}", retryPolicy.getReConnectRetryCount(), retryPolicy.hostAndPort());
                    //重连
                    Channel newChannel = newChannel(retryPolicy.getHost(), retryPolicy.getPort());
                    //增加重连次数
                    incrementReconnectCount(newChannel, retryPolicy);
                    //移交tid
                    newChannel.attr(HotpaxosConstans.TID_KEY).compareAndSet(null, reChannel.attr(HotpaxosConstans.TID_KEY).get());
                } else {
                    //当前连接数减1
                    tids.get(retryPolicy.hostAndPort()).decrementAndGet();
                }

            }
        }));
    }

    private void incrementReconnectCount(Channel reChannel, RetryPolicy retryPolicy) {
        if (null != retryPolicy) {
            retryPolicy.incrementReConnectRetryCount();
        }
        reChannel.attr(HotpaxosConstans.RETRY_POLICY_KEY).compareAndSet(null, retryPolicy);
    }


}
