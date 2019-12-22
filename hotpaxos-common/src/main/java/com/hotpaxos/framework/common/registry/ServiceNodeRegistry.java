package com.hotpaxos.framework.common.registry;

/**
 * 服务节点注册
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface ServiceNodeRegistry {
    /**
     * 服务节点注册
     * @param commonServiceNode
     * @param nodeType
     */
    void register(CommonServiceNode commonServiceNode,NodeType nodeType);

    /**
     * 移除服务注册节点
     * @param commonServiceNode
     * @param nodeType
     */
    void deregister(CommonServiceNode commonServiceNode,NodeType nodeType);
}
