package com.hotpaxos.server.config;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.registry.DiscoveryClient;
import com.hotpaxos.framework.common.registry.ServiceDiscovery;
import com.hotpaxos.framework.common.registry.ServiceNodeRegistry;
import com.hotpaxos.framework.common.registry.listener.DiscoveryServiceListener;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.netty.HotPaxNettyApplicationContext;
import com.hotpaxos.netty.dispather.DefaultActionDispatcher;
import com.hotpaxos.netty.invoke.InvokerManager;
import com.hotpaxos.netty.parser.DefaultParser;
import com.hotpaxos.server.HotPaxServerManager;
import com.hotpaxos.statistical.StatisticalNode;
import com.hotpaxos.zookeeper.registry.ZkClient;
import com.hotpaxos.zookeeper.registry.ZkConfig;
import com.hotpaxos.zookeeper.registry.ZkServiceDiscovery;
import com.hotpaxos.zookeeper.registry.ZkServiceNodeRegistry;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
@Configuration
@EnableConfigurationProperties({HotPaxServerProps.class})
@ConditionalOnBean(HotPaxServerMarkerConfiguration.ServerMarker.class)
public class ServerAutoConfig {
    @Resource
    private HotPaxServerProps hotPaxosServerProps;
    @Resource
    private ZkClient zkClient;

    @Resource
    private StatisticalNode node;

    @Bean
    public Parser parser() {
        //这个需要改造等待 通过注解制定转码器
        return new DefaultParser();
    }


    @Configuration
    @EnableConfigurationProperties(ZkConfig.class)
    //默认开启  当属性中配置了 value 时  依据 vakue为准 未配置则为true
    @ConditionalOnProperty(value = "hotpaxos.zk.enable", matchIfMissing = true)
    public static class ZkConfiguration {
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
    public ActionDispatcher actionDispatcher(InvokerManager invokerManager) {
        return new DefaultActionDispatcher(invokerManager);
    }

    //netty 服务端管理器 初始化配置连接等信息入口
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public HotPaxServerManager hotPaxServerManager(ServiceNodeRegistry registry,
                                                   ServiceDiscovery discovery,
                                                   ServiceNodeListener listener,
                                                   ActionDispatcher dispatcher,
                                                   Parser parser) {
        return new HotPaxServerManager(zkClient,
                hotPaxosServerProps,
                registry,
                discovery,
                listener,
                dispatcher,
                parser, node);
    }

    @Bean
    public HotPaxNettyApplicationContext hotPaxNettyApplicationContext(InvokerManager invokerManager) {
        return new HotPaxNettyApplicationContext(invokerManager);
    }

    @Bean
    public InvokerManager invokerManager() {
        return new InvokerManager();
    }

}