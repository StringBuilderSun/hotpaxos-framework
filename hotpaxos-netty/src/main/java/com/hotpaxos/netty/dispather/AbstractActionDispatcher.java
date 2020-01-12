package com.hotpaxos.netty.dispather;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.netty.invoke.Invoker;
import com.hotpaxos.netty.invoke.InvokerManager;
import com.hotpaxos.netty.invoke.InvokerType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 路由转发器 找到发送目的地和执行器
 * 转发之前  可以有过滤器 和 拦截器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class AbstractActionDispatcher implements ActionDispatcher {


    private InvokerManager invokerManager;

    public AbstractActionDispatcher(InvokerManager invokerManager) {
        this.invokerManager = invokerManager;
    }

    @Override
    public void open(TSession tSession) {
        //这里会执行 打开连接的操作
        List<Invoker> invokers = invokerManager.getInvokers(InvokerType.SHANKE_INVOKER);
        for (Invoker invoker : invokers) {
            invoker.invoke(tSession);
        }
    }

    @Override
    public void handleAction(TSession tSession, HotpaxMessage hotpaxMessage) {
        log.info("收到消息:TSessio:{} message:{}", tSession.ipAndPort(), hotpaxMessage.getContext());
    }

    @Override
    public void disconnect(TSession tSession) {

    }
}
