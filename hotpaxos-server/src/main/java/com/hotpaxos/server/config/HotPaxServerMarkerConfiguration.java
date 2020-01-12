package com.hotpaxos.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 启用了EnableHotPaxServer注解 会导入该类实例 用于加载server相关的bean实例到容器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/11
 */
@Configuration
public class HotPaxServerMarkerConfiguration {

    @Bean
    public ServerMarker serverMarker() {
        return new ServerMarker();
    }

    public class ServerMarker {

    }
}
