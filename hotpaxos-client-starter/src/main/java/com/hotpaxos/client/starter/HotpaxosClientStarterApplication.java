package com.hotpaxos.client.starter;

import com.hotpaxos.client.marker.SocketClientMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/21.
 */
@SpringBootApplication
public class HotpaxosClientStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotpaxosClientStarterApplication.class, args);
    }


    @Bean
    public SocketClientMarker socketClientMarker() {
        return new SocketClientMarker();
    }

}
