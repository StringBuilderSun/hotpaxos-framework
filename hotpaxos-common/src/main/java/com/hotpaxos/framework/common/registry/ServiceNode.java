package com.hotpaxos.framework.common.registry;
import java.util.Map;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface ServiceNode {
    /**
     * 获取服务节点的appId
     *
     * @return
     */
    int getAppId();

    /**
     * 获取节点nodeId
     *
     * @return
     */
    String getNodeId();

    /**
     * 获取主机IP
     *
     * @return
     */
    String getHost();

    /**
     * 获取端口号
     *
     * @return
     */
    int getPort();

    /**
     * 获取属性
     *
     * @return
     */
    Map<String, Object> getAttrs();

    default <T> T getAttr(String name) {
        return null;
    }

    /**
     * 是持久节点吗
     *
     * @return
     */
    default boolean isPersistent() {
        return false;
    }

    default String hostAndPort() {
        return getHost() + ":" + getPort();
    }

    /**
     * 获取节点路径
     * @return
     */
    default String nodePath() {
        return "/" + getAppId() + "/" + getNodeId();
    }
}