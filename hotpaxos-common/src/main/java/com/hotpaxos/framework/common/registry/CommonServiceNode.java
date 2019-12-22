package com.hotpaxos.framework.common.registry;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;
import java.util.UUID;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@Builder
@AllArgsConstructor
public class CommonServiceNode implements ServiceNode {
    private int appId;
    private String host;
    private int port;
    private String nodeId;
    private transient boolean persistent;
    private Map<String, Object> attrs;

    @Override
    public int getAppId() {
        return this.appId;
    }

    @Override
    public String getNodeId() {
        return this.nodeId;
    }

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public <T> T getAttr(String name) {
        if (attrs == null || attrs.isEmpty()) {
            return null;
        }
        return (T) attrs.get(name);
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }


    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public Map<String, Object> getAttrs() {
        return attrs;
    }

    /**
     * 创建node节点名
     *
     * @return
     */
    public static String createNodeId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
