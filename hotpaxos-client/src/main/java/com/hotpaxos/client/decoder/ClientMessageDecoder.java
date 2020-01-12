package com.hotpaxos.client.decoder;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.netty.AbstractMessageDecoder;
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
        message.setContext(new String(message.getContentBytes(), "utf-8"));
    }
}
