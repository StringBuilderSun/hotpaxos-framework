package com.hotpaxos.zookeeper.config;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/23.
 */

import com.hotpaxos.zookeeper.registry.ZkClient;
import com.hotpaxos.zookeeper.registry.ZkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ZkConfig.class)
public class ZkAutoConfig {



}
