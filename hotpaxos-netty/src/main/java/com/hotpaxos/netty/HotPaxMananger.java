package com.hotpaxos.netty;

import com.hotpaxos.framework.common.core.Admin;
import com.hotpaxos.framework.common.registry.DiscoveryClient;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * netty服务管理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public abstract class HotPaxMananger implements Admin, ApplicationListener<ContextRefreshedEvent> {

    //zk节点发现服务
    private DiscoveryClient client;

    protected final AtomicBoolean started = new AtomicBoolean(false);

    public HotPaxMananger(DiscoveryClient client) {
        this.client = client;
    }


    public DiscoveryClient getClient() {
        return client;
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }

    @Override
    public boolean isClosed() {
        return !isStarted();
    }


    @Override
    public void start() {
        //开启zk服务
        if (client.isClosed()) {
            try {
                client.start();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void shutdown() {

    }


}
