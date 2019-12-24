package com.hotpaxos.zookeeper.registry;

import com.alibaba.fastjson.JSON;
import com.hotpaxos.framework.common.registry.CommonServiceNode;
import com.hotpaxos.framework.common.registry.ServiceNodeListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
//@Slf4j
public class ZkCacheListener implements TreeCacheListener {

    private ServiceNodeListener serviceNodeListener;
    private String watchUrl;

    public ZkCacheListener(ServiceNodeListener listener, String watchUrl) {
        this.serviceNodeListener = listener;
        this.watchUrl = watchUrl;
    }


    @Override
    public void childEvent(CuratorFramework curatorFramework, TreeCacheEvent treeCacheEvent) throws Exception {
        ChildData childData = treeCacheEvent.getData();
        if (childData == null) return;
        String activePath = childData.getPath();
        if (activePath.startsWith(watchUrl)) {
            switch (treeCacheEvent.getType()) {
                case NODE_ADDED:
                    serviceNodeListener.onServiceNodeCreated(activePath, JSON.parseObject(childData.getData(), CommonServiceNode.class));
                    break;
                case NODE_REMOVED:
                    serviceNodeListener.onServiceNodeRemoved(activePath, JSON.parseObject(childData.getData(), CommonServiceNode.class));
                    break;
                case NODE_UPDATED:
                    serviceNodeListener.onServiceNodeUpdated(activePath, JSON.parseObject(childData.getData(), CommonServiceNode.class));
                    break;
            }
//            log.info("zk serviceNode channge:{} nodePath:{} watchUrl:{}", treeCacheEvent.getType(), activePath, watchUrl);
        }
    }
}
