package com.hotpaxos.netty;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public abstract class AbstractMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //客户端发送过来的是可能是byteBuf ,将byteBuf 转换成 hotpaxMessage 即可  后面扩展
        HotpaxMessage hotpaxMessage = new HotpaxMessage();
        writeContextBytes(in, hotpaxMessage);
        ctx.fireChannelRead(hotpaxMessage);
    }

    public abstract void writeContextBytes(ByteBuf in, HotpaxMessage message) throws UnsupportedEncodingException;
}
