package com.hotpaxos.client.retry;

import com.hotpaxos.framework.common.core.retry.Retry;
import com.hotpaxos.framework.common.core.retry.RetrySleeper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
@Data
@Slf4j
public class RetryMaxCount implements Retry {
    //当前重试次数
    private int retryIndex;
    //每次重试间隔
    private int retryIntervalMs;

    public RetryMaxCount(int retryIndex, int retryIntervalMs) {
        this.retryIndex = retryIndex;
        this.retryIntervalMs = retryIntervalMs;
    }

    @Override
    public boolean allowRetry(int retryCount, long elspsedTimeMs, RetrySleeper retrySleeper) {
        if (retryIndex < retryCount) {
            try {
                retrySleeper.sleep(retryIntervalMs, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                //关闭当前执行线程
                Thread.currentThread().interrupt();
                log.error("Error occurred while retry sleeping ", e);
                return false;
            }
        }
        return true;
    }
}
