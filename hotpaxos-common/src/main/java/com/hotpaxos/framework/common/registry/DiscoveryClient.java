package com.hotpaxos.framework.common.registry;

import com.hotpaxos.framework.common.core.Admin;

import java.util.List;

/**
 * zk服务发现
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface DiscoveryClient<T> extends Admin {
    /**
     * 获取节点数据
     * @param key
     * @return
     */
    String getData(String key);

    /**
     * 获取子节点数据
     * @param key
     * @return
     */
    List<String>  getChildrenKeys(String key);

    /**
     * 判断阶段是否存在
     * @param key
     * @return
     */
    boolean isExisted(String key);

    /**
     * 更新节点数据
     * @param key
     * @param value
     */
    void updateNodeData(String key,String value);

    /**
     * 删除节点数据
     * @param key
     */
    void removeNode(String key);

    /**
     * 注册持久化节点
     * @param key
     * @param value
     */
    void registerPersistNode(String key,String value);

    /**
     * 注册临时节点
     * @param key
     * @param value
     * @param cache 是否本地缓存
     */
    void registerEphemeralNode(String key,String value,boolean cache);

    /**
     * 注册临时顺序节点
     * @param key
     */
    void registerEphemeralNodeSequential(String key,String value,boolean cache);

    /**
     * 获取客户端
     * @return
     */
    T getClient();
}
