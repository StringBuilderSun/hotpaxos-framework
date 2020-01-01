package com.hotpaxos.client.retry;

/**
 * 重试策略枚举
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public enum RetryType {
    /**
     * 一直重试
     */
    RETRY_FOREVER,
    /**
     * 重试最大次数
     */
    RETRY_MAX_COUNT
}
