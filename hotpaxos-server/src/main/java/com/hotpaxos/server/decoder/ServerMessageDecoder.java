package com.hotpaxos.server.decoder;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.netty.AbstractMessageDecoder;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

/**
 * netty 服务端解码器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class ServerMessageDecoder extends AbstractMessageDecoder {

    @Override
    public void writeContextBytes(ByteBuf in, HotpaxMessage message) {
        byte[] contextBytes = new byte[in.readableBytes()];
        in.readBytes(contextBytes, in.readerIndex(), in.readableBytes());
        String context = "";
        try {
            context = new String(contextBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("失败");
        }
        message.setContext(context);
//        in.release();
    }
}
