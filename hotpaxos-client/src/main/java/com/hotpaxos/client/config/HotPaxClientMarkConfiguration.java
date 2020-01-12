package com.hotpaxos.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 启用了EnableHotPaxClient注解 会导入该类实例 用于加载client相关的bean实例到容器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/11
 */
@Configuration
public class HotPaxClientMarkConfiguration {

    @Bean
    public ClientMarker clientMarker() {

        return new ClientMarker();
    }

    //只起标识左右
    public class ClientMarker {

    }
}
