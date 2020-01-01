package com.hotpaxos.test.config;

import com.hotpaxos.framework.common.registry.listener.DiscoveryServiceListener;
import com.hotpaxos.zookeeper.registry.ZkCacheListener;
import com.hotpaxos.zookeeper.registry.ZkClient;
import com.hotpaxos.zookeeper.registry.ZkServiceDiscovery;
import com.hotpaxos.zookeeper.registry.ZkServiceNodeRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/24.
 */
//@Configuration
//@EnableAutoConfiguration
@ConditionalOnProperty(prefix = "application", name = "env", havingValue = "test")
public class AutoTesConfig {
    @Bean
    public ZkServiceNodeRegistry zkRegistry(@Autowired ZkClient zkClient) {
        return new ZkServiceNodeRegistry(zkClient);
    }

    @Bean
    public DiscoveryServiceListener serviceNodeListener() {
        return DiscoveryServiceListener.defaultDiscoveryServiceListener();
    }

    @Bean
    public ZkCacheListener zkCacheListener(@Autowired DiscoveryServiceListener serviceNodeLister) {
        return new ZkCacheListener(serviceNodeLister, "/");
    }

    @Bean
    public ZkServiceDiscovery zkServiceDiscovery(@Autowired ZkClient zkClient)
    {
         return new ZkServiceDiscovery(zkClient);
    }


}
