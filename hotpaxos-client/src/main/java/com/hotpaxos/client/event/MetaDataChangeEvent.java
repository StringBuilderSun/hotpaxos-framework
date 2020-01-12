package com.hotpaxos.client.event;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
public class MetaDataChangeEvent extends ApplicationEvent {
    private Map<String, String> metadataMap;

    public MetaDataChangeEvent(Map<String, String> metadataMap) {
        this(metadataMap, metadataMap);
    }

    public MetaDataChangeEvent(Map<String, String> metadataMap, Object source) {
        super(source);
        this.metadataMap = metadataMap;
    }
}
