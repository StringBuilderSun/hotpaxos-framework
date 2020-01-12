package com.hotpaxos.framework.common.core.retry;

import java.util.concurrent.TimeUnit;

/**
 * 重试睡眠
 * User: lijinpeng
 * Created by Shanghai on 2020/1/5
 */
public interface RetrySleeper {

    /**
     * 睡眠指定的时间
     * @param time
     * @param timeUnit
     * @throws InterruptedException
     */
    void sleep(long time, TimeUnit timeUnit) throws InterruptedException;
    //默认实现
    RetrySleeper DEFAULT_RETRY_SLEEPER = (time, unit) -> unit.sleep(time);
}
