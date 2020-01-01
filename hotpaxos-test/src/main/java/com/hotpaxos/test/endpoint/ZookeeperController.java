package com.hotpaxos.test.endpoint;

import com.hotpaxos.framework.common.registry.*;
import com.hotpaxos.framework.common.registry.listener.NotifyListener;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.zookeeper.registry.ZkServiceDiscovery;
import com.hotpaxos.zookeeper.registry.ZkServiceNodeRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class ZookeeperController {
    @Autowired
    private ServiceNodeRegistry zkRegistry;
    @Autowired
    private ServiceDiscovery zkServiceDiscovery;
    @Autowired
    private ServiceNodeListener serviceNodeListener;

    @RequestMapping("/zk/nodeRegister")
    public String zookeeperNodeAdd() throws InterruptedException {
        //注册服务节点数据
        CommonServiceNode serviceNode = new CommonServiceNode(1000, "192.168.164.10", 1388, UUID.randomUUID().toString().replace("-", ""), false, null);
        //注册客户端节点数据
        CommonServiceNode clientNode = new CommonServiceNode(1000, "192.168.162.20", 9000, UUID.randomUUID().toString().replace("-", ""), false, null);
        zkRegistry.register(serviceNode, NodeType.AVALIABLE_SERVER);
        Thread.sleep(4999);
        zkRegistry.register(clientNode, NodeType.CLIENT);
        return "ok";
    }


    @RequestMapping("/zk/subscribeNodes")
    public String subscribeNodes() {
        zkServiceDiscovery.subscribe("/", serviceNodeListener);
        return "subscribe zk path:/";
    }

    @RequestMapping("/zk/startService")
    public String startService() {
        serviceNodeListener.addServiceNodeCreatedListener((registerUrl, serviceNode) -> {
            log.info("已收到通知，我开始做自己的处理了，registerUrl:{} serviceNode:{}", registerUrl, serviceNode);
        });
        return "service is started!";
    }
}
