package com.hotpaxos.client.heartbeat;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * 重连处理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@ChannelHandler.Sharable
@Slf4j
public class ReconnectHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //客户端不活跃时 重连服务器
        log.info("客户端不活跃 ，重新连接服务器，业务逻辑等待开发.....{}",ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }
}
