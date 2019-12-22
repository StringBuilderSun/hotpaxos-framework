package com.hotpaxos.zookeeper.registry;

import com.alibaba.fastjson.JSON;
import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.NodeType;
import com.hotpaxos.framework.common.registry.ServiceNodeRegistry;
import com.hotpaxos.framework.common.utils.PathUtils;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public class ZkServiceNodeRegistry implements ServiceNodeRegistry {
    private ZkClient zkClient;

    public ZkServiceNodeRegistry(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public void register(CommonServiceNode commonServiceNode, NodeType nodeType) {
        if (zkClient.isClosed()) {
            return;
        }
        if (commonServiceNode.isPersistent()) {
            zkClient.registerPersistNode(PathUtils.toNodePath(commonServiceNode, nodeType), JSON.toJSONString(commonServiceNode));
        } else {
            zkClient.registerEphemeralNode(PathUtils.toNodePath(commonServiceNode, nodeType), JSON.toJSONString(commonServiceNode), true);
        }
    }

    @Override
    public void deregister(CommonServiceNode commonServiceNode, NodeType nodeType) {
        if (zkClient.isStarted()) {
            zkClient.removeNode(PathUtils.toNodePath(commonServiceNode, nodeType));
        }
    }
}
