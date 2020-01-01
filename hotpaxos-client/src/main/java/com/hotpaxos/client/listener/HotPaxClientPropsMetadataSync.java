package com.hotpaxos.client.listener;

import com.hotpaxos.client.event.MetaDataChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * metadata发生变化时  同步给注册中心和sever端
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class HotPaxClientPropsMetadataSync implements ApplicationListener<MetaDataChangeEvent>, Ordered {
    @Override
    public void onApplicationEvent(MetaDataChangeEvent metaDataChangeEvent) {
        log.info("metadata发生变化时  同步给注册中心和sever端  等待后续开发......");
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
