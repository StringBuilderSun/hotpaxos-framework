package com.hotpaxos.client.heartbeat;

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

    }

    private void ping(Channel channel) {
        //开启一个定时任务 ping服务端
        ScheduledFuture<?> future = channel.eventLoop().schedule(() ->
                        log.info("往服务端发送心跳  等待开发"),
                pingInterval, TimeUnit.SECONDS);
        future.addListener(f -> {
            if (f.isSuccess()) {
                //发送ping消息
                log.info("往服务器发送ping 等待开发");
            }
        });
    }
}
