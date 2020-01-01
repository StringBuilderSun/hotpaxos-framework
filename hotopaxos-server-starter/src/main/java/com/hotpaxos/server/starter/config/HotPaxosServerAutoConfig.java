package com.hotpaxos.server.starter.config;

import com.hotpaxos.statistical.StaticNodeImpl;
import com.hotpaxos.statistical.StatisticalNode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@EnableAutoConfiguration
@Configuration
public class HotPaxosServerAutoConfig {

    @Bean
    public StatisticalNode statisticalNode() {
        return new StaticNodeImpl();
    }
}
