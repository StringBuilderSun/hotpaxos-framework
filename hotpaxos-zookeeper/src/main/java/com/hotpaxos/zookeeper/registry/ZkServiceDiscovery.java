package com.hotpaxos.zookeeper.registry;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.MoreExecutors;
import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.ServiceDiscovery;
import com.hotpaxos.framework.common.registry.listener.ServiceNodeListener;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.utils.ZKPaths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ZkServiceDiscovery implements ServiceDiscovery {

    private ZkClient zkClient;
    //将监听器存入缓存中
    private Map<ServiceNodeListener, ZkCacheListener> listenerMap = new ConcurrentHashMap<>();

    public ZkServiceDiscovery(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    @Override
    public List<CommonServiceNode> lookup(String path) {
        List<String> childrenKeys = zkClient.getChildrenKeys(path);
        if (childrenKeys == null || childrenKeys.isEmpty()) {
            return Collections.emptyList();
        }
        return childrenKeys.stream()
                .map(key -> path + ZKPaths.PATH_SEPARATOR + key)
                .map(zkClient::getData)
                .filter(Objects::nonNull)
                .map(childDate -> JSON.parseObject(childDate, CommonServiceNode.class))
                .collect(Collectors.toList());
    }

    @Override
    public void subscribe(String path, ServiceNodeListener serviceNodeListener) {
        ZkCacheListener zkCacheListener = new ZkCacheListener(serviceNodeListener, path);
        listenerMap.putIfAbsent(serviceNodeListener, zkCacheListener);
        zkClient.getClient().getTreeCache().getListenable().addListener(zkCacheListener, MoreExecutors.newDirectExecutorService());
    }

    @Override
    public void unsubscribe(String path, ServiceNodeListener serviceNodeListener) {
        TreeCacheListener treeCacheListener = listenerMap.get(serviceNodeListener);
        if (treeCacheListener == null) {
            return;
        }
        listenerMap.remove(serviceNodeListener);
        zkClient.getClient().getTreeCache().getListenable().removeListener(treeCacheListener);
    }
}
