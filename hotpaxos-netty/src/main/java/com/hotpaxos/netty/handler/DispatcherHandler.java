package com.hotpaxos.netty.handler;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import com.hotpaxos.netty.TSessionDefault;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理的默认分发器
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
//允许线程复用
@ChannelHandler.Sharable
@Slf4j
public class DispatcherHandler extends SimpleChannelInboundHandler<HotpaxMessage> {
    //消息转换器
    private Parser parser;
    //消息处理器
    private ActionDispatcher dispatcher;
    //节点
    private final StatisticalNode node;


    public DispatcherHandler(Parser parser, ActionDispatcher dispatcher, StatisticalNode node) {
        this.dispatcher = dispatcher;
        this.parser = parser;
        this.node = node;
    }

    //服务端收到客户端信息
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HotpaxMessage hotpaxMessage) throws Exception {
        //有消息发送 找到默认的handler处理消息
        dispatcher.handleAction(getTSession(channelHandlerContext.channel()), hotpaxMessage);
    }

    //服务端被激活时
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //服务端激活 将Tsession放入到Channel自定义属性中
        if (!createTSession(ctx.channel())) {
            //属性赋值不成功 直接 关闭连接
            ctx.channel().close();
        }
        //打开连接
        dispatcher.open(getTSession(ctx.channel()));
    }


    //服务端不活跃 关闭连接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        TSession session = getTSession(ctx.channel());
        if (null != session) {
            dispatcher.disconnect(session);
        }
        //这下面还有逻辑处理
    }

    //将Tsession放到channel的attr保存
    private boolean createTSession(Channel channel) {
        return channel.attr(HotpaxosConstans.SESSION_ID).compareAndSet(null, new TSessionDefault(channel, parser, node));
    }

    //通信异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        if (channel.isOpen() | channel.isActive()) {
            if (cause instanceof Exception) {
                //系统异常直接关闭
                channel.close();
            }
        }
        log.error("DispatherHandler error,", cause);
    }

    private TSession getTSession(Channel channel) {
        //服务端激活时 已将TSession 保存到channel自定义属性中
        return channel.attr(HotpaxosConstans.SESSION_ID).get();
    }
}
