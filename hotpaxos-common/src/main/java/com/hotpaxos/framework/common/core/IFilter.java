package com.hotpaxos.framework.common.core;

/**
 * socket 连接 消息处理过滤器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public interface IFilter {

    /**
     * 当soket连接 Channel激活时候执行该方法
     *
     * @param tSession
     * @return
     */
    boolean onConnect(TSession tSession);

    /**
     * 消息接收到后 会调用该过滤器方法
     *
     * @param tSession
     * @param handler
     * @param req
     * @return
     */
    boolean doHandle(TSession tSession, IHandler handler, Object req);

    /**
     * 当Channel关闭时 会调用该过滤器方法
     *
     * @param tSession
     * @return
     */
    boolean onClose(TSession tSession);

}
