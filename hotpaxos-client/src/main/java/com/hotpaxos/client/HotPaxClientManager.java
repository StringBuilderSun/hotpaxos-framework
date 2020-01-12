package com.hotpaxos.client;

import com.hotpaxos.client.config.HotPaxClientProps;
import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.registry.*;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.framework.common.utils.AppIdUtils;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import com.hotpaxos.framework.common.utils.NetIPUtil;
import com.hotpaxos.framework.common.utils.PathUtils;
import com.hotpaxos.netty.HotPaxMananger;
import com.hotpaxos.statistical.StatisticalNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.List;

/**
 * netty客户端管理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/2
 */
@Slf4j
public class HotPaxClientManager extends HotPaxMananger {
    //客户端连接配置
    private final HotPaxClientProps hotPaxClientProps;
    //节点注册服务 用于服务节点到zk的注册
    private final ServiceNodeRegistry registry;
    //节点发现服务  用于客户端订阅服务节点 以及服务节点变化通知订阅节点
    private final ServiceDiscovery discovery;
    //节点监听器 节点变化 执行对应逻辑处理
    private final ServiceNodeListener listener;
    //netty客户端
    private final TcpClientBootStrap tcpClientBootStrap;
    @Getter
    private CommonServiceNode serviceNode;

    public HotPaxClientManager(DiscoveryClient client,
                               HotPaxClientProps hotPaxClientProps,
                               ServiceNodeRegistry registry,
                               ServiceDiscovery discovery,
                               ServiceNodeListener listener,
                               ActionDispatcher dispatcher,
                               Parser parser, StatisticalNode node) {
        super(client);
        this.hotPaxClientProps = hotPaxClientProps;
        this.registry = registry;
        this.discovery = discovery;
        this.listener = listener;
        this.tcpClientBootStrap = new TcpClientBootStrap(hotPaxClientProps, dispatcher, parser, node);
    }

    @Override
    public void shutdown() {
        if (this.started.compareAndSet(true, false)) {
            registry.deregister(serviceNode, NodeType.CLIENT);
            discovery.subscribe(hotPaxClientProps.getWatchUrl(), listener);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            //开启客户端
            if (this.started.compareAndSet(false, true)) {
                //与所有服务器建立连接
                List<ServiceNode> serviceNodes = getServers();
                tcpClientBootStrap.newChannel(serviceNodes);
                //添加监听器 保证有新服务节点注册进来 时候  能够及时连接
                listener.addServiceNodeCreatedListener((registerUrl, serviceNode) -> {
                    tcpClientBootStrap.newChannel(serviceNode);
                });
                log.debug("hotpaxos client start...");
                serviceNode = CommonServiceNode.builder()
                        .nodeId(CommonServiceNode.createNodeId())
                        .appId(AppIdUtils.loadAppId())
                        .host(NetIPUtil.getLocalIp())
                        .attrs(new HashMap<>())
                        .build();
                //将客户端配置的自定义属性都写入到节点信息
                if (null != hotPaxClientProps.getMetadataMap()) {
                    hotPaxClientProps.getMetadataMap().forEach(serviceNode::attr);
                }
                registry.register(serviceNode, NodeType.CLIENT);
                //监听网关服务 有变更执行上面定义的notifyListener
                discovery.subscribe(hotPaxClientProps.getWatchUrl() + HotpaxosConstans.PATH_SEPARATOR + hotPaxClientProps.getServerAppId()
                        , listener);

            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

    }


    //获取所有服务节点
    private List<ServiceNode> getServers() {
        String serverPath = PathUtils.toServicePath(hotPaxClientProps.getServerAppId(), NodeType.AVALIABLE_SERVER);
        return discovery.lookup(serverPath);
    }


}
