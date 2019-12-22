package com.hotpaxos.framework.common.registry;

/**
 * 当注册节点发生变化时 通知
 * User: lijinpeng
 * Created by Shanghai on 2019/12/22.
 */
public interface NotifyListener {

    void notify(String registerUrl,ServiceNode serviceNode);
}
