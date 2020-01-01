package com.hotpaxos.framework.common.core;

import lombok.Data;

/**
 * 消息传递主体
 * User: lijinpeng
 * Created by Shanghai on 2019/12/29
 */
@Data
public class HotpaxCon {
    //消息体字节码
    private byte[] contentBytes;
    //消息体
    private Object context;
}
