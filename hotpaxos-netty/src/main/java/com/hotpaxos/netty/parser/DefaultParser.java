package com.hotpaxos.netty.parser;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.Parser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
@Slf4j
public class DefaultParser implements Parser {
    @Override
    public ByteBuf write(Object obj) {
        //使用直接内存
        ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer();
        if (null != obj) {
            if (obj instanceof HotpaxMessage) {
                try {
                    HotpaxMessage message = (HotpaxMessage) obj;
                    //后续需要加Parser转换器 加入报文头 协议等信息
                    if (message.getContentBytes() == null) {
                        out.writeBytes(message.getContext().toString().getBytes());
                    } else {
                        out.writeBytes(message.getContentBytes());
                    }
                } catch (Exception ex) {
                    log.error("parser encode header and boyd error", ex);
                }
            }
        }
        return out;
    }

    @Override
    public Object read(HotpaxMessage hotpaxMessage) {
        //扩展后续通过Parser 将字节数据 转换为实例对象 现阶段只返回字节数组
        return hotpaxMessage.getContentBytes();
    }
}
