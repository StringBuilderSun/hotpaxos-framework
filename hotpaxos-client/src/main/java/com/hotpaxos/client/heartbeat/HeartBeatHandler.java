package com.hotpaxos.client.heartbeat;

import com.hotpaxos.framework.common.exception.HotPaxosException;
import com.hotpaxos.framework.common.utils.ChannelUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 心跳触发器
 * 客户端连接服务器之后 定时ping服务器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    //ping的时间间隔
    private int pingInterval;

    public HeartBeatHandler(int pingInterval) {
        this.pingInterval = pingInterval;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ping(ctx.channel());

    }

    private void ping(Channel channel) {
        //开启一个延迟执行任务 ping服务端
        ScheduledFuture<?> future = channel.eventLoop().schedule(() ->
                {
                    log.info("ping");
                    if (channel.isActive()) {
                        ChannelUtil.sendHeartPing(channel, true);
                    } else {
                        //客户端不活跃 可能连接断开  关闭当前通道
                        log.warn("the connection may be broken,cancle the schedule task and send a heart beat.");
                        channel.closeFuture();
                        throw new HotPaxosException("the connection may be broken,cancle the schedule task and send a heart beat.");
                    }
                },
                pingInterval, TimeUnit.SECONDS);
        //心跳发送成功后 继续间隔时间发送
        future.addListener(f -> {
            if (f.isSuccess()) {
                ping(channel);
            }
        });
    }
}
