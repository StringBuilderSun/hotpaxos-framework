package com.hotpaxos.client.retry;

import com.hotpaxos.framework.common.core.retry.Retry;
import com.hotpaxos.framework.common.core.retry.RetryPolicy;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取重试策略的工厂
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
@Slf4j
public class RetryFactory {
    public static Retry apply(RetryPolicy retryPolicy) {
        try {
            RetryType retryType = Enum.valueOf(RetryType.class, retryPolicy.getRetryType());
            switch (retryType) {
                case RETRY_FOREVER:
                    return new RetryForever(retryPolicy.getReConnectSleepTimeMs());
                case RETRY_MAX_COUNT:
                    return new RetryMaxCount(retryPolicy.getReConnectRetryCount(), retryPolicy.getReConnectSleepTimeMs());
                default:
                    return null;
            }
        } catch (Exception ex) {
            log.warn("RetryFactory apply retryPolicy Error", ex);
            return null;
        }

    }
}
