package com.hotpaxos.framework.common.core;

/**
 * 消息处理接口
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
public interface ActionDispatcher {
    /**
     * 打开连接
     *
     * @param tSession
     */
    void open(TSession tSession);

    /**
     * 消息处理
     *
     * @param tSession
     * @param hotpaxMessage
     */
    void handleAction(TSession tSession, HotpaxMessage hotpaxMessage);

    /**
     * 关闭连接
     *
     * @param tSession
     */
    void disconnect(TSession tSession);
}
