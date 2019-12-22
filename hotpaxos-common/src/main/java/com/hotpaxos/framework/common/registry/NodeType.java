package com.hotpaxos.framework.common.registry;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@AllArgsConstructor
@Getter
public enum NodeType {
    AVALIABLE_SERVER("server"),
    UNAVALIABLE_SERVER("unavaliableServer"),
    CLIENT("client");
    private String value;
}
