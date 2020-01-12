package com.hotpaxos.client.heartbeat;

import com.hotpaxos.framework.common.propertychange.HotPaxPropertyChangeManagerSupport;
import com.hotpaxos.framework.common.utils.PathUtils;
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
        log.debug("客户端不活跃 ，重新连接服务器：{}", ctx.channel().remoteAddress().toString());
        String propertyName = PathUtils.toReConnectName(ctx.channel());
        //发布本地实践 调用事变变化监听器
        try {
            HotPaxPropertyChangeManagerSupport.getInstance().pushPropsEvent(propertyName, ctx.channel());
        } finally {
            //监听事件执行完毕后  移除  防止再次执行
            HotPaxPropertyChangeManagerSupport.getInstance().removePropertyChangeListener(propertyName);
        }
        super.channelInactive(ctx);
    }
}
