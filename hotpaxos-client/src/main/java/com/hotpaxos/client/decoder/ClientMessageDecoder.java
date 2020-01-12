package com.hotpaxos.client.decoder;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.netty.AbstractMessageDecoder;
import com.hotpaxos.pb.HotPaxMsg;
import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * 客户端解码器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class ClientMessageDecoder extends AbstractMessageDecoder {
    @Override
    public void writeContextBytes(ByteBuf in, HotpaxMessage message) throws UnsupportedEncodingException {
        byte[] msgs = new byte[in.readableBytes()];
        in.readBytes(msgs);
        message.setContentBytes(msgs);
        //此时这个消息可能是ACK 后续还需改造
        try {
            HotPaxMsg.Syn syn = HotPaxMsg.Syn.parseFrom(msgs);
            message.setContext(syn);
        } catch (InvalidProtocolBufferException e) {
            System.out.println("转换失败..." + e);
        }
    }
}
