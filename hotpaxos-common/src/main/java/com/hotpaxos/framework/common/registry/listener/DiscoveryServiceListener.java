package com.hotpaxos.framework.common.registry.listener;

import com.hotpaxos.framework.common.registry.ServiceNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: lijinpeng
 * Created by Shanghai on 2019/12/28.
 * 服务节点监听默认处理流程控制
 * 1、节点变化处理逻辑都放入到节点监听器集合中
 * 2、确保一个节点变化 能够通知到订阅该节点的所有动态处理
 * 3、针对每个服务监听器操作加锁 确保不会出现线程安全问题
 */
@Slf4j
public class DiscoveryServiceListener implements ServiceNodeListener {

    private static DiscoveryServiceListener discoveryServiceListener = new DiscoveryServiceListener();
    private final List<NotifyListener> serviceAddListener;
    private final List<NotifyListener> serviceUpdateListener;
    private final List<NotifyListener> serviceRemoveListener;
    private ReentrantLock addLock = new ReentrantLock();
    private ReentrantLock removeLock = new ReentrantLock();
    private ReentrantLock updateLock = new ReentrantLock();

    public static DiscoveryServiceListener defaultDiscoveryServiceListener() {
        return discoveryServiceListener;
    }

    private DiscoveryServiceListener() {
        this.serviceAddListener = new ArrayList<>();
        this.serviceRemoveListener = new ArrayList<>();
        this.serviceUpdateListener = new ArrayList<>();
    }


    @Override
    public void onServiceNodeCreated(String path, ServiceNode serviceNode) {
        log.debug("listener onServiceNodeCreated....path:{} serviceNode:{}",path,serviceNode);
        try {
            addLock.lock();
            notifys(serviceAddListener, path, serviceNode);
        } catch (Exception e) {
            log.error("[{}] serviceNode added notify event excute fail...", serviceNode, e);
        } finally {
            addLock.unlock();
        }
    }

    @Override
    public void onServiceNodeUpdated(String path, ServiceNode serviceNode) {
        log.debug("listener onServiceNodeUpdated....path:{} serviceNode:{}",path,serviceNode);
        try {
            updateLock.lock();
            notifys(serviceUpdateListener, path, serviceNode);
        } catch (Exception e) {
            log.error("[{}] serviceNode update notify event excute fail...", serviceNode, e);
        } finally {
            updateLock.unlock();
        }
    }

    @Override
    public void onServiceNodeRemoved(String path, ServiceNode serviceNode) {
        log.debug("listener onServiceNodeRemoved....path:{} serviceNode:{}",path,serviceNode);
        try {
            removeLock.lock();
            notifys(serviceRemoveListener, path, serviceNode);
        } catch (Exception e) {
            log.error("[{}] serviceNode removed notify event excute fail...", serviceNode, e);
        } finally {
            removeLock.unlock();
        }
    }

    @Override
    public void addServiceNodeCreatedListener(NotifyListener notifyListener) {
        try {
            addLock.lock();
            if (!serviceAddListener.contains(notifyListener)) {
                serviceAddListener.add(notifyListener);
            }
        } finally {
            addLock.unlock();
        }
    }

    @Override
    public void addServiceNodeUpdatedListener(NotifyListener notifyListener) {
        try {
            updateLock.lock();
            if (!serviceUpdateListener.contains(notifyListener)) {
                serviceUpdateListener.add(notifyListener);
            }
        } finally {
            updateLock.unlock();
        }
    }

    @Override
    public void addServiceNodeRemovedListener(NotifyListener notifyListener) {
        try {
            removeLock.lock();
            if (!serviceRemoveListener.contains(notifyListener)) {
                serviceRemoveListener.add(notifyListener);
            }
        } finally {
            removeLock.unlock();
        }
    }

    private void notifys(List<NotifyListener> notifyListeners, String path, ServiceNode serviceNode) {
        if (notifyListeners == null) {
            return;
        }
        for (NotifyListener notifyListener : notifyListeners) {
            try {
                notifyListener.notify(path, serviceNode);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }
}
