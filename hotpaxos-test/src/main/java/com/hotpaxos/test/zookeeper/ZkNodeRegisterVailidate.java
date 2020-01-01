package com.hotpaxos.test.zookeeper;

import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.NodeType;
import com.hotpaxos.test.SpringBaseTest;
import com.hotpaxos.zookeeper.registry.ZkServiceNodeRegistry;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/24.
 */

@Slf4j
public class ZkNodeRegisterVailidate extends SpringBaseTest {

    @Autowired
    private ZkServiceNodeRegistry zkRegistry;

    @Test
    public void registerTest() throws InterruptedException {
        //注册服务节点数据
        CommonServiceNode serviceNode = new CommonServiceNode(1000, "192.168.164.10", 1388, UUID.randomUUID().toString().replace("-",""), false, null);
        //注册客户端节点数据
        CommonServiceNode clientNode = new CommonServiceNode(1000, "192.168.162.20", 9000, UUID.randomUUID().toString().replace("-",""), false, null);
        zkRegistry.register(serviceNode, NodeType.AVALIABLE_SERVER);
        zkRegistry.register(clientNode, NodeType.CLIENT);
    }

}
