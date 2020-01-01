package com.hotpaxos.client.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * 断连触发器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class TcpClientIdleHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.ALL_IDLE.equals(event.state())) {
                //无法在规定时间内读取或者写出数据，可能通道异常 直接关闭通道
                log.warn("channel:{} state:{} auto close", ctx.channel().remoteAddress(), event.state());
                ctx.channel().close();
            } else if (IdleState.WRITER_IDLE.equals(event.state())) {
                //客户端可能写入逻辑过长 导致无法及时发送心跳 手动发送心跳包
                log.info("发送心跳包   等待后续开发");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
