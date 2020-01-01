package com.hotpaxos.server.decoder;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.netty.AbstractMessageDecoder;
import io.netty.buffer.ByteBuf;

/**
 * netty 服务端解码器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class ServerMessageDecoder extends AbstractMessageDecoder {

    @Override
    public void writeContextBytes(ByteBuf in, HotpaxMessage message) {
        byte[] contextBytes = in.array();
        message.setContentBytes(contextBytes);
        in.release();
    }
}
