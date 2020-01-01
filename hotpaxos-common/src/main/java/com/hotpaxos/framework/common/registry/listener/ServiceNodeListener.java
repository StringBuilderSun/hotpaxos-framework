package com.hotpaxos.framework.common.registry.listener;

import com.hotpaxos.framework.common.registry.ServiceNode;

/**
 * Zk节点监听
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface ServiceNodeListener {
    /**
     * 有新节点创建时触发
     * @param path
     * @param serviceNode
     */
    void onServiceNodeCreated(String path, ServiceNode serviceNode);

    /**
     * zk 节点变化时候触发
     * @param path
     * @param serviceNode
     */
    void onServiceNodeUpdated(String path,ServiceNode serviceNode);

    /**
     * zk 节点被删除的时候触发
     * @param path
     * @param serviceNode
     */
    void onServiceNodeRemoved(String path,ServiceNode serviceNode);

    /**
     * zk新节点创建时 添加监听器
     * @param notifyListener
     */
    void addServiceNodeCreatedListener(NotifyListener notifyListener);

    /**
     * zk 节点更新时  添加监听器
     * @param notifyListener
     */
    void addServiceNodeUpdatedListener(NotifyListener notifyListener);

    /**
     * zk 节点移除时 添加监听器
     * @param notifyListener
     */
    void addServiceNodeRemovedListener(NotifyListener notifyListener);
}
