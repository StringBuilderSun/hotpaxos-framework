package com.hotpaxos.client.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Configuration
@EnableConfigurationProperties(HotPaxClientProps.class)
public class HotPaxosClientAutoConfig {
}
