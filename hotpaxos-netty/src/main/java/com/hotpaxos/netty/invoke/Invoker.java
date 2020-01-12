package com.hotpaxos.netty.invoke;

import com.hotpaxos.framework.common.core.TSession;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
public interface Invoker {

    /**
     * 执行器
     * @param tSession
     */
    void invoke(TSession tSession);
}
