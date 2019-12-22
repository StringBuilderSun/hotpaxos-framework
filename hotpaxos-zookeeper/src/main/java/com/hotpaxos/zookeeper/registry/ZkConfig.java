package com.hotpaxos.zookeeper.registry;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zk连接配置信息
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@ConfigurationProperties(prefix = "hotpaxos.zk.instance")
@Data
public class ZkConfig {

    private String hosts;
    private String digest;
    /**
     * 最大重试次数
     */
    private int maxRetries = 3;
    /**
     * 连接等待时间
     */
    private int baseSleepTimeMs = 5000;
    /**
     * 最大连接等待时间
     */
    private int maxSleepMs = 5000;
    /**
     * 响应超时时间
     */
    private int sessionTimeOut = 5000;
    /**
     * 连接超时时间
     */
    private int connectTimeOut = 5000;
    /**
     * 监控路径
     */
    private String watchPath;
    /**
     * 进入的根目录
     */
    private String nameSpace = "hotpaxos";
}
