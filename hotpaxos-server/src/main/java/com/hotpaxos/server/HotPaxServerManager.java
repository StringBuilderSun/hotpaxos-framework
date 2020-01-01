package com.hotpaxos.server;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.registry.*;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.framework.common.utils.AppIdUtils;
import com.hotpaxos.framework.common.utils.NetIPUtil;
import com.hotpaxos.netty.HotPaxMananger;
import com.hotpaxos.server.config.HotPaxosServerProps;
import com.hotpaxos.statistical.StatisticalNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty 服务端管理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class HotPaxServerManager extends HotPaxMananger {
    //服务端配置属性 用于实例化netty服务端
    private final HotPaxosServerProps hotPaxosServerProps;
    //netty服务端
    private final TcpSeverBootStrap severBootStrap;
    //节点注册服务 用于服务节点到zk的注册
    private final ServiceNodeRegistry registry;
    //节点发现服务  用于客户端订阅服务节点 以及服务节点变化通知订阅节点
    private final ServiceDiscovery discovery;
    //已启动的节点 保存到内存中
    private final Map<String, CommonServiceNode> commonServiceNodeMap = new ConcurrentHashMap<>();
    //节点监听器 节点变化 执行对应逻辑处理
    private final ServiceNodeListener listener;

    public HotPaxServerManager(DiscoveryClient client,
                               HotPaxosServerProps hotPaxosServerProps,
                               ServiceNodeRegistry registry,
                               ServiceDiscovery discovery,
                               ServiceNodeListener listener,
                               ActionDispatcher dispatcher,
                               Parser parser, StatisticalNode node) {
        super(client);
        this.hotPaxosServerProps = hotPaxosServerProps;
        this.registry = registry;
        this.discovery = discovery;
        this.listener = listener;
        this.severBootStrap = new TcpSeverBootStrap(hotPaxosServerProps, dispatcher, parser, node);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //开启服务
        if (!this.started.compareAndSet(false, true)) {
            return;
        }
        //开启netty服务端
        for (int port : hotPaxosServerProps.getBinds()) {
            severBootStrap.connect(port);
            //将服务节点注册到zk
            registerServiceNode(port);
        }
        //url 变化的时候执行监听器
        listener.addServiceNodeUpdatedListener((registerUrl, serviceNode) -> {
            log.info("监听到路径节点变化:registerUrl:{} serviceNode:{}  待后续扩展", registerUrl, serviceNode);
        });
        //当url变化时  会通知所有订阅了这个节点的监听器
        discovery.subscribe(hotPaxosServerProps.getWatchUrl(), listener);
        log.info("netty server start......");
    }

    //注册服务节点
    private void registerServiceNode(int port) {
        CommonServiceNode node = CommonServiceNode.builder()
                .host(NetIPUtil.getLocalIp())
                .appId(AppIdUtils.loadAppId())
                .nodeId(CommonServiceNode.createNodeId())
                .port(port)
                .attrs(new HashMap<>())
                .build();
        registry.register(node, NodeType.AVALIABLE_SERVER);
        commonServiceNodeMap.put(node.hostAndPort(), node);
    }

    @Override
    public void shutdown() {
        if (this.started.compareAndSet(true, false)) {
            //服务节点从zk节点移除
            for (Map.Entry<String, CommonServiceNode> entry : commonServiceNodeMap.entrySet()) {
                //从zk节点移除
                registry.deregister(entry.getValue(), NodeType.AVALIABLE_SERVER);
                //取消监听器订阅
                discovery.unsubscribe(hotPaxosServerProps.getWatchUrl(), listener);
                //关闭服务
                severBootStrap.shutdown();
            }
        }
    }
}
