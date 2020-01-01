package com.hotpaxos.server.config;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.Parser;
import com.hotpaxos.framework.common.registry.DiscoveryClient;
import com.hotpaxos.framework.common.registry.ServiceDiscovery;
import com.hotpaxos.framework.common.registry.ServiceNodeRegistry;
import com.hotpaxos.framework.common.registry.listener.DiscoveryServiceListener;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import com.hotpaxos.netty.dispather.DefaultActionDispatcher;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;



/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
@Configuration
@EnableConfigurationProperties({HotPaxosServerProps.class})
public class HotPaxosServerAutoConfig {
    @Resource
    private HotPaxosServerProps hotPaxosServerProps;
    @Resource
    private ZkClient zkClient;

    @Resource
    private StatisticalNode node;

    @Bean
    public Parser parser() {
        //这个需要改造等待 通过注解制定转码器
        return new DefaultParser();
    }

    //节点注册服务
    @Bean
    public ServiceNodeRegistry registry() {
        return new ZkServiceNodeRegistry(zkClient);
    }

    //节点发现服务
    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ZkServiceDiscovery(zkClient);
    }

    //服务监听器
    @Bean
    public ServiceNodeListener serviceNodeListener() {
        return DiscoveryServiceListener.defaultDiscoveryServiceListener();
    }

    //消息处理器
    @Bean
    public ActionDispatcher actionDispatcher() {
        return new DefaultActionDispatcher();
    }

    //netty 服务端管理器 初始化配置连接等信息入口
    @Bean(initMethod = "start", destroyMethod = "shutdown")
    @ConditionalOnMissingBean(HotPaxServerManager.class)
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

}
