package com.hotpaxos.client.retry;

import com.hotpaxos.framework.common.core.retry.Retry;
import com.hotpaxos.framework.common.core.retry.RetrySleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 一直重试
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
@Slf4j
public class RetryForever implements Retry {
    /**
     * 重试时间间隔
     */
    private final int retryIntevalMs;

    @Override
    public boolean allowRetry(int retryCount, long elspsedTimeMs, RetrySleeper retrySlepper) {
        try {
            retrySlepper.sleep(retryIntevalMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //关闭当前执行线程
            Thread.currentThread().interrupt();
            log.error("Error occurred while retry sleeping ", e);
            return false;
        }
        return true;
    }

    public RetryForever(int retryIntevalMs) {
        this.retryIntevalMs = retryIntevalMs;
    }
}
