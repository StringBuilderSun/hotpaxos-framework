package com.hotpaxos.framework.common.core.invoke;

import com.hotpaxos.framework.common.core.TSession;

/**
 * 拦截处理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
public interface Invoke {

    /**
     * 执行器
     *
     * @param tSession
     */
    void invoke(TSession tSession);
}
