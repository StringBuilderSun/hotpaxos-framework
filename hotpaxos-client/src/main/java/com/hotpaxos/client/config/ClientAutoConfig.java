package com.hotpaxos.client.config;

import com.hotpaxos.client.HotPaxClientManager;
import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.registry.DiscoveryClient;
import com.hotpaxos.framework.common.registry.ServiceDiscovery;
import com.hotpaxos.framework.common.registry.ServiceNodeRegistry;
import com.hotpaxos.framework.common.registry.listener.DiscoveryServiceListener;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.netty.dispather.DefaultActionDispatcher;
import com.hotpaxos.netty.invoke.InvokerRegister;
import com.hotpaxos.netty.parser.DefaultParser;
import com.hotpaxos.statistical.StaticNodeImpl;
import com.hotpaxos.statistical.StatisticalNode;
import com.hotpaxos.zookeeper.registry.ZkClient;
import com.hotpaxos.zookeeper.registry.ZkConfig;
import com.hotpaxos.zookeeper.registry.ZkServiceDiscovery;
import com.hotpaxos.zookeeper.registry.ZkServiceNodeRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Configuration
@EnableConfigurationProperties(HotPaxClientProps.class)
@ConditionalOnBean(HotPaxClientMarkConfiguration.ClientMarker.class)
public class ClientAutoConfig {
    @Resource
    private HotPaxClientProps hotPaxClientProps;
    @Resource
    private ZkClient zkClient;


    @Bean
    public StatisticalNode statisticalNode() {
        return new StaticNodeImpl();
    }

    @Bean
    public Parser parser() {
        //这个需要改造等待 通过注解制定转码器
        return new DefaultParser();
    }


    @Configuration
    @EnableConfigurationProperties(ZkConfig.class)
    //默认开启  当属性中配置了 value 时  依据 vakue为准 未配置则为true
    @ConditionalOnProperty(value = "hotpaxos.zk.enable", matchIfMissing = true)
    public static class ZKConfiguration {
        @Resource
        private ZkConfig zkConfig;

        @Bean(destroyMethod = "shutdown")
        public DiscoveryClient<ZkClient.Cache> zkClient() {
            return new ZkClient(zkConfig);
        }

        //节点注册服务
        @Bean
        public ServiceNodeRegistry registry(DiscoveryClient zkClient) {
            return new ZkServiceNodeRegistry(zkClient);
        }

        //节点发现服务
        @Bean
        public ServiceDiscovery serviceDiscovery(DiscoveryClient<ZkClient.Cache> zkClient) {
            return new ZkServiceDiscovery(zkClient);
        }

        //服务监听器
        @Bean
        public ServiceNodeListener serviceNodeListener() {
            return DiscoveryServiceListener.defaultDiscoveryServiceListener();
        }
    }



    //消息处理器
    @Bean
    public ActionDispatcher actionDispatcher() {
        return new DefaultActionDispatcher();
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public HotPaxClientManager hotPaxClientManager(ServiceNodeRegistry registry,
                                                   ServiceDiscovery discovery,
                                                   ServiceNodeListener serviceNodeListener,
                                                   ActionDispatcher actionDispatcher, Parser parser,
                                                   StatisticalNode node) {
        return new HotPaxClientManager(zkClient,
                hotPaxClientProps,
                registry,
                discovery, serviceNodeListener, actionDispatcher, parser, node);
    }

}
