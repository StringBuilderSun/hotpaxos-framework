package com.hotpaxos.server.starter.config;

import com.hotpaxos.server.config.HotPaxServerMarkerConfiguration;
import com.hotpaxos.server.starter.handler.HandlerShakeAno;
import com.hotpaxos.statistical.StaticNodeImpl;
import com.hotpaxos.statistical.StatisticalNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */

@Configuration
@ConditionalOnBean(HotPaxServerMarkerConfiguration.ServerMarker.class)
public class HotPaxosServerAutoConfig {

    @Bean
    public StatisticalNode statisticalNode() {
        return new StaticNodeImpl();
    }

    @Bean
    public HandlerShakeAno handlerShakeAno() {
        return new HandlerShakeAno();
    }
}
