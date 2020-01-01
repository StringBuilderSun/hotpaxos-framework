package com.hotpaxos.netty;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.statistical.StatisticalNode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * netty信息传递默认实现
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
public class TSessionDefault extends TSession {

    private final StatisticalNode node;

    public TSessionDefault(Channel channel, Parser parser, StatisticalNode node) {
        super(channel, parser);
        this.node = node;
    }

    @Override
    public void send(HotpaxMessage hotpaxMessage) {
        ByteBuf byteBuf = parserByteBuf(hotpaxMessage);
        getChannel().writeAndFlush(byteBuf.slice());
    }

    @Override
    public void write(HotpaxMessage hotpaxMessage) {
        send(hotpaxMessage);
    }

    @Override
    public void writeAndFlush(ByteBuf byteBuf) {
        getChannel().writeAndFlush(byteBuf);
    }

    @Override
    public void writeAndFlush(ByteBuf byteBuf, GenericFutureListener<? extends Future<? super Void>> listener) {
        getChannel().writeAndFlush(byteBuf).addListener(listener);
    }

    @Override
    public ByteBuf parserByteBuf(HotpaxMessage hotpaxMessage) {
        ByteBuf byteBuf = parser.write(hotpaxMessage);
        return byteBuf;
    }
}
