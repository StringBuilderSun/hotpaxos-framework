package com.hotpaxos.client.config;

import com.hotpaxos.client.bean.DemoPerson;
import com.hotpaxos.client.marker.SocketClientMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/21.
 */
@Configuration
@EnableConfigurationProperties(ClientConfiguration.class)
public class SocketClientAutoConfiguration {

    @Autowired
    private ClientConfiguration clientConfiguration;
    @Bean
    @ConditionalOnBean({SocketClientMarker.class})
    public DemoPerson demoPerson() {
        System.out.println(clientConfiguration.getAttr());
        return new DemoPerson(clientConfiguration.getHostName(),clientConfiguration.getPort());
    }
}
