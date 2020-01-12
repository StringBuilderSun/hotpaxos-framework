package com.hotpaxos.netty.dispather;

import com.hotpaxos.netty.invoke.InvokerManager;

/**
 * 默认的消息处理接口
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class DefaultActionDispatcher extends AbstractActionDispatcher {

    public DefaultActionDispatcher(InvokerManager invokerManager) {
        super(invokerManager);
    }
}
