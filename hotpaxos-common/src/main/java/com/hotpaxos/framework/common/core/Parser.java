package com.hotpaxos.framework.common.core;

import io.netty.buffer.ByteBuf;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
public interface Parser {
    /**
     * 将数据转换为ByteBuf
     *
     * @param obj
     * @return
     */
    ByteBuf write(Object obj);

    /**
     * 将hotpaxMessage转换为Object对象
     *
     * @param hotpaxMessage
     * @return
     */
    Object read(HotpaxMessage hotpaxMessage);
}
