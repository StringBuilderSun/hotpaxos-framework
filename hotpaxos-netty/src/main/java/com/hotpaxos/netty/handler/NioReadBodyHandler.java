package com.hotpaxos.netty.handler;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty 传递数据 消息体 解码
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@ChannelHandler.Sharable
public class NioReadBodyHandler extends SimpleChannelInboundHandler<HotpaxMessage> {
    //解码器
    private Parser parser;
    private StatisticalNode node;

    public NioReadBodyHandler(Parser parser, StatisticalNode node) {
        this.parser = parser;
        this.node = node;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HotpaxMessage msg) throws Exception {
        //后续 在进行解码 目前未编码 直接放过去
        ctx.fireChannelRead(msg);
    }
}
