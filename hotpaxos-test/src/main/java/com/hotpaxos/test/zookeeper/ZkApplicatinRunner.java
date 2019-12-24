package com.hotpaxos.test.zookeeper;

import com.hotpaxos.zookeeper.registry.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/23.
 */
@Component
public class ZkApplicatinRunner implements ApplicationRunner {

    @Autowired
    private ZkClient zkClient;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (zkClient.isClosed()) {
            zkClient.start();
        }
    }
}
