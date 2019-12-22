package com.hotpaxos.zookeeper.registry;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@AllArgsConstructor
@NoArgsConstructor
public class ZkException extends RuntimeException {
    private String message;

    public ZkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZkException(Throwable cause) {
        super(cause);
    }
}
