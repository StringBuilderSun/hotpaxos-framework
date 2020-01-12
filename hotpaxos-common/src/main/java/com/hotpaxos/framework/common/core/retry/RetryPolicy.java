package com.hotpaxos.framework.common.core.retry;

import lombok.Builder;
import lombok.Data;

/**
 * 重试策略
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
@Data
@Builder
public class RetryPolicy {
    private String host;
    private int port;
    /**
     * 最大重试次数
     */
    private int maxRetryCount;
    /**
     * 重试策略
     */
    private String retryType;
    /**
     * 重连睡眠时间
     */
    private int reConnectSleepTimeMs;
    /**
     * 当前重连次数
     */
    private int reConnectRetryCount;

    public String hostAndPort() {
        return host + ":" + port;
    }

    public void incrementReConnectRetryCount() {
        reConnectRetryCount++;
    }

    public void resetReconnectRetryCount() {
        reConnectRetryCount = 0;
    }

}
