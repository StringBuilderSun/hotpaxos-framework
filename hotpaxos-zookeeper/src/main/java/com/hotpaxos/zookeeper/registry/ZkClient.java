package com.hotpaxos.zookeeper.registry;


import com.hotpaxos.framework.common.registry.DiscoveryClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Map;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
@Slf4j
public class ZkClient implements DiscoveryClient<ZkClient.Cache> {
    private ZkClient.Cache zkCache;
    private CuratorFramework curatorClient;
    private TreeCache treeCache;
    private ZkConfig zkConfig;
    //本地临时缓存节点
    private Map<String, String> ephemeralNodes = new ConcurrentHashMap<>(4);
    //本地临时缓存顺序节点
    private Map<String, String> ephemeralSeqNodes = new ConcurrentHashMap<>(4);
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ZkClient(ZkConfig zkConfig) {
        this.zkConfig = zkConfig;
    }

    //初始化配置连接信息
    private void init() {
        //创建zk客户端
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory
                .builder()
                .connectString(zkConfig.getHosts())
                .retryPolicy(new ExponentialBackoffRetry(zkConfig.getBaseSleepTimeMs(), zkConfig.getMaxRetries(), zkConfig.getMaxSleepMs()))
                .namespace(zkConfig.getNameSpace());
        if (zkConfig.getConnectTimeOut() > 0) {
            builder.connectionTimeoutMs(zkConfig.getConnectTimeOut());
        }
        if (zkConfig.getSessionTimeOut() > 0) {
            builder.sessionTimeoutMs(zkConfig.getSessionTimeOut());
        }
        //权限设置
        if (zkConfig.getDigest() != null) {
            builder.authorization("digest", zkConfig.getDigest().getBytes(Charset.forName("UTF-8")));
            builder.aclProvider(new ACLProvider() {
                @Override
                public List<ACL> getDefaultAcl() {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }

                @Override
                public List<ACL> getAclForPath(String path) {
                    return ZooDefs.Ids.CREATOR_ALL_ACL;
                }
            });
        }
        curatorClient = builder.build();
        //zk客户端连接成功
        log.info("zk client init successful.....");
    }

    //初始化本地缓存
    private void initLocalCache(String listernPath) throws Exception {
        //treeCache会监听路径下所有节点的变化 并更新到缓存中
        treeCache = new TreeCache(curatorClient, listernPath);
        treeCache.start();
    }

    @Override
    public boolean isStarted() {
        return started.get();
    }

    @Override
    public boolean isClosed() {
        return !started.get();
    }

    @Override
    public void start() throws Exception {
        //启动zk客户端
        if (started.compareAndSet(false, true)) {
            this.init();
            curatorClient.start();
            log.info("zk client is staring ,waiting to connection...");
            //阻塞等待一分钟 直到服务端完成 established
            if (!curatorClient.blockUntilConnected(1, TimeUnit.MINUTES)) {
                throw new ZkException("init zk error,zkConfig=" + zkConfig);
            }
            initLocalCache(zkConfig.getWatchPath());
            this.zkCache = new Cache(curatorClient, treeCache);
            addConnectionStateListener();
            running.compareAndSet(false, true);
            log.info("zk client start success,server lists:{}", zkConfig.getHosts());
        }
    }

    @Override
    public void shutdown() {
        if (treeCache != null) {
            treeCache.close();
        }
        try {
            //先关闭 缓存  预留时间方式  正在写数据
            TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {

        }
        if (isStarted()) {
            curatorClient.close();
        }
        log.info("zk client closed...");
    }

    @Override
    public String getData(String key) {
        //先从缓存获取  缓存获取不到再从zk获取
        if (treeCache == null) {
            return null;
        }
        ChildData childData = treeCache.getCurrentData(key);
        if (null != childData) {
            return null == childData.getData() ? null : new String(childData.getData(), Charset.forName("UTF-8"));
        }
        return getFromRemote(key);
    }

    private String getFromRemote(String key) {
        if (isExisted(key)) {
            try {
                return new String(curatorClient.getData().forPath(key), Charset.forName("UTF-8"));
            } catch (Exception e) {
                log.error("zk getFromRemote error!key:{}", key, e);
            }
        }
        return null;
    }


    @Override
    public List<String> getChildrenKeys(String key) {
        List<String> childNodes = new ArrayList<>();
        try {
            if (!isExisted(key)) return childNodes;
            childNodes = curatorClient.getChildren().forPath(key);
            childNodes.sort(Comparator.reverseOrder());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childNodes;
    }

    @Override
    public boolean isExisted(String key) {
        try {
            return null != curatorClient.checkExists().forPath(key);
        } catch (Exception e) {
            log.error("zk check key isExisted error key:{}", key, e);
        }
        return false;
    }

    @Override
    public void updateNodeData(String key, String value) {
        try {
            curatorClient.inTransaction().check().forPath(key)
                    .and()
                    .setData().forPath(key, value.getBytes(Charset.forName("UTF-8")))
                    .and()
                    .commit();
        } catch (Exception e) {
            log.error("update zk node error！key:{} value:{}", key, value, e);
            throw new ZkException(e);
        }
    }

    @Override
    public void removeNode(String key) {
        try {
            curatorClient.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (Exception e) {
            log.error("removeNode error!key:{}", key);
            throw new ZkException(e);
        }
    }

    @Override
    public void registerPersistNode(String key, String value) {
        if (isExisted(key)) {
            updateNodeData(key, value);
        } else {
            try {
                curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(key, value.getBytes(Charset.forName("UTF-8")));
            } catch (Exception e) {
                log.error("registerPersistNode error,key:{} value:{}", key, value, e);
                throw new ZkException(e);
            }
        }
    }

    @Override
    public void registerEphemeralNode(String key, String value, boolean cache) {

        try {
            if (isExisted(key)) {
                //删除临时节点
                removeNode(key);
            }
            curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
            if (cache) ephemeralNodes.put(key, value);
        } catch (Exception e) {
            log.error("registerEphemeralNode error,key:{} value:{}", key, value);
            throw new ZkException(e);
        }
    }

    public void reRegisterEphemeralNode(String key, String value) {
        registerEphemeralNode(key, value, false);
    }

    @Override
    public void registerEphemeralNodeSequential(String key, String value, boolean cache) {

        try {
            curatorClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(key, value.getBytes(Charset.forName("UTF-8")));
            if (cache) ephemeralSeqNodes.put(key, value);
        } catch (Exception e) {
            log.error("registerEphemeralNodeSequential error,key:{} value:{}", key, value);
            throw new ZkException(e);
        }
    }

    public void reRegisterEphemeralNodeSequential(String key, String value) {
        registerEphemeralNode(key, value, false);
    }


    //注册连接状态监听器
    private void addConnectionStateListener() {
        curatorClient.getConnectionStateListenable().addListener(
                (client, newState) -> {
                    if (ConnectionState.RECONNECTED == newState) {
                        //重连重新注册临时节点数据
                        ephemeralNodes.forEach(this::reRegisterEphemeralNode);
                        ephemeralSeqNodes.forEach(this::reRegisterEphemeralNodeSequential);
                    }
                }
        );
    }
    @Override
    public Cache getClient() {
        return zkCache;
    }

    @AllArgsConstructor
    @Data
    public class Cache {
        private CuratorFramework curatorClient;
        private TreeCache treeCache;
    }
}
