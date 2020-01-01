package com.hotpaxos.netty.handler;

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
public class TcpServerIdleHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.ALL_IDLE.equals(event.state())) {
                //规定时间内没有收到客户端上行数据 也不发送数据  主动断开连接
                log.warn("channel:{} state:{} auto close", ctx.channel().remoteAddress(), event.state());
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
