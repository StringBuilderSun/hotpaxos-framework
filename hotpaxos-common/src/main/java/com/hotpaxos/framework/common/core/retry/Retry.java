package com.hotpaxos.framework.common.core.retry;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
public interface Retry {

    /**
     * 是否允许重试
     *
     * @param retryCount
     * @param elspsedTimeMs
     * @param retrySleeper
     * @return
     */
    boolean allowRetry(int retryCount, long elspsedTimeMs, RetrySleeper retrySleeper);
}
