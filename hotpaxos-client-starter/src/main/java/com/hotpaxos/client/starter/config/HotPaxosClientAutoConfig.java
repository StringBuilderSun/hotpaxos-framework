package com.hotpaxos.client.starter.config;

import com.hotpaxos.statistical.StaticNodeImpl;
import com.hotpaxos.statistical.StatisticalNode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/2
 */
@Configuration
public class HotPaxosClientAutoConfig {

    @Bean
    public StatisticalNode statisticalNode() {

        System.out.println("创建对象");
        return new StaticNodeImpl();
    }
}
