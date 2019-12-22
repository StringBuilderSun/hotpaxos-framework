package com.hotpaxos.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/21.
 */
@ConfigurationProperties(prefix = "hotpaxos.instance")
@Data
public class ClientConfiguration {
    private String hostName;
    private String port;
    private Map<String,String> attr;
}
