package com.hotpaxos.framework.common.core;

/**
 * 管理类接口
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface Admin {
    /**
     * 检查服务是否启动
     * @return <code>true</code>如果服务已关闭，其他情况返回<code>false</code>
     * @see Admin#isClosed()
     */
    boolean isStarted();

    /**
     * 检查服务是否关闭
     * @return
     */
    boolean isClosed();

    /**
     * 启动服务
     * @throws Exception
     */
    void  start() throws Exception;

    /**
     * 关闭服务
     */
    void  shutdown();
}
