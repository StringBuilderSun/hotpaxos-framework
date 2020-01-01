package com.hotpaxos.framework.common.registry;

import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;

import java.util.List;

/**
 * 节点发现服务
 */
public interface ServiceDiscovery {

    /**
     * 获取该路径下所有节点信息
     * @param path
     * @return
     */
    List<CommonServiceNode> lookup(String path);

    /**
     * 订阅监控该路径下节点变化时通知
     * @param path
     * @param serviceNodeListener
     */
    void subscribe(String path, ServiceNodeListener serviceNodeListener);

    /**
     * 接触订阅监控该路径下节点变化时通知
     * @param path
     * @param serviceNodeListener
     */
    void unsubscribe(String path, ServiceNodeListener serviceNodeListener);
}
