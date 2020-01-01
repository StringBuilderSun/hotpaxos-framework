package com.hotpaxos.server.config;

import com.hotpaxos.framework.common.registry.NodeType;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * netty服务端配置信息
 * User: lijinpeng
 * Created by Shanghai on 2019/12/31
 */
@Data
@ConfigurationProperties(prefix = "hotpaxos.server")
public class HotPaxosServerProps {
    /**
     * 允许接收的最大数据长度
     */
    private int receiveSize = 65535;
    /**
     * 服务端绑定端口号
     */
    private List<Integer> binds = Collections.singletonList(1810);
    /**
     * worker工作线程数量
     */
    private int workThreads = 4;
    /**
     * boss工作线程数量
     */
    private int bossThreads = 1;
    /**
     * 读操作空闲时间
     */
    private int readerIdleTimeSeconds = 60;
    /**
     * 写操作空闲时间
     */
    private int writerIdleTimeSeconds = 30;
    /**
     * 读写操作空闲时间
     */
    private int allIdleTimeSeconds = 60;
    /**
     * TSession的作用域
     */
    private List<String> scopes = new ArrayList<>();
    /**
     * 服务端监控的客户端节点  /hotpaxos/client/****
     */
    private String watchUrl = HotpaxosConstans.PATH_SEPARATOR + NodeType.CLIENT.getValue();
}
