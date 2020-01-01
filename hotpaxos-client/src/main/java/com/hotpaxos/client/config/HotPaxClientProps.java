package com.hotpaxos.client.config;

import com.hotpaxos.client.event.MetaDataChangeEvent;
import com.hotpaxos.client.retry.RetryType;
import com.hotpaxos.framework.common.registry.NodeType;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * netty客户端配置
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Data
@ConfigurationProperties(prefix = "hotpaxos.client")
public class HotPaxClientProps implements InitializingBean, ApplicationContextAware {
    private ApplicationContext application;
    /**
     * 客户端默认的服务发现路径(zk服务节点路径)
     */
    private final static String ZK_CLIENT_CONNECT_PATH = HotpaxosConstans.PATH_SEPARATOR + NodeType.AVALIABLE_SERVER.getValue();
    /**
     * 与服务端连接数
     */
    private int channelSize = 1;
    /**
     * 接收数据的最大长度
     */
    private int receiveSize = 65535;
    /**
     * 工作线程数量
     */
    private int eventLoopThreads = 4;
    /**
     * 连接超时
     */
    private int connectTimeOut;
    /**
     * 重连间隔基数
     */
    private int reconnectSleepTimeMs = 2000;
    /**
     * 重连最大次数
     */
    private int maxReconnectRetryCount = Integer.MAX_VALUE;
    /**
     * ping 间隔
     */
    private int pingInterval = 10;
    /**
     * 服务端appid
     */
    private int serverAppId;
    /**
     * 读空闲操作时间
     */
    private int readerIdleTimeSeconds = 60;
    /**
     * 写空闲操作时间
     */
    private int writerIdleTimeSeconds = 30;
    /**
     * 读写空闲操作时间
     */
    private int allIdleTimeSeconds = 60;
    /**
     * 重试策略
     */
    private String retryType = RetryType.RETRY_MAX_COUNT.name();
    /**
     * metadata-map
     */
    private Map<String, String> metadataMap = new HashMap<>();
    /**
     * TSession的作用域
     */
    private List<String> scopes = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        //此时Bean属性填充已经完成  将时间放入到spring容器中等待监听器发现
        application.publishEvent(new MetaDataChangeEvent(metadataMap));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.application = applicationContext;
    }
}
