package com.hotpaxos.client.decoder;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.netty.AbstractMessageDecoder;
import io.netty.buffer.ByteBuf;

/**
 * 客户端解码器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class ClientMessageDecoder extends AbstractMessageDecoder {
    @Override
    public void writeContextBytes(ByteBuf in, HotpaxMessage message) {
        message.setContentBytes(in.array());
    }
}
