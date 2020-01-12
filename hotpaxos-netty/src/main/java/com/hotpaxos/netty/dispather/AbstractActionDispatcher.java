package com.hotpaxos.netty.dispather;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.IFilter;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.framework.common.core.invoke.HotPaxInvoke;
import com.hotpaxos.framework.common.core.invoke.InvokeConstants;
import com.hotpaxos.netty.filter.FilterRegister;
import com.hotpaxos.netty.invoke.InvokerRegister;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


/**
 * 路由转发器 找到发送目的地和执行器
 * 转发之前  可以有过滤器 和 拦截器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class AbstractActionDispatcher implements ActionDispatcher {

    //过滤器
    private FilterRegister filterRegister;

    //拦截执行器
    private InvokerRegister invokerRegister;

    public AbstractActionDispatcher() {
        //后续这部分内容 由扫描自动注入
        this.filterRegister = FilterRegister.instance();
        this.invokerRegister = InvokerRegister.instance();
    }

    @Override
    public void open(TSession tSession) {
        //1 过滤器
        Collection<IFilter> filters = filterRegister.getAllFilter();
        for (IFilter filter : filters) {
            if (!filter.onConnect(tSession)) {
                //返回true 直接终端后续操作
                return;
            }
        }
        //2 拦截执行器  此时为连接建立阶段
        Collection<HotPaxInvoke> invokes = invokerRegister.getInvokersByType(InvokeConstants.OPEN_TYPE);
        for (HotPaxInvoke invoke : invokes) {
            if (invoke.isStaicInvoke()) {
                invoke.invoke(tSession);
            }
        }
    }

    @Override
    public void handleAction(TSession tSession, HotpaxMessage hotpaxMessage) {
        log.info("收到消息:TSessio:{} message:{}", tSession.ipAndPort(), hotpaxMessage.getContext());
    }

    @Override
    public void disconnect(TSession tSession) {

    }

    public final FilterRegister getFilterRegister() {
        return this.filterRegister;
    }

    public final void setFilterRegister(FilterRegister filterRegister) {
        this.filterRegister = filterRegister;
    }
}
