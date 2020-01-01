package com.hotpaxos.test.config;

import com.google.common.util.concurrent.MoreExecutors;
import com.hotpaxos.zookeeper.registry.ZkCacheListener;
import com.hotpaxos.zookeeper.registry.ZkClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/24.
 */
@Slf4j
//@Component
public class ApplicationTestRunner implements ApplicationRunner {

    @Autowired
    private ZkClient zkClient;
    @Autowired
    private ZkCacheListener zkCacheListener;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (zkClient.isClosed()) {
            try {
                zkClient.start();
                log.info("zk 服务开启成功...");
            } catch (Exception e) {
                log.error("zk 服务器开启失败！");
            }
        }
    }
}
