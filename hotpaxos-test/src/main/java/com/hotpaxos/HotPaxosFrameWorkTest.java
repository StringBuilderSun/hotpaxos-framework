package com.hotpaxos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/23.
 */
@SpringBootApplication
public class HotPaxosFrameWorkTest {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(HotPaxosFrameWorkTest.class);
    }


}
