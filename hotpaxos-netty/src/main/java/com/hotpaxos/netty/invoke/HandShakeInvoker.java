package com.hotpaxos.netty.invoke;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.netty.invoke.Invoker;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端握手
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
@Slf4j
public class HandShakeInvoker implements Invoker {

    @Override
    public void invoke(TSession tSession) {
        log.info("收到客户端连接,给客户端发ack.....");
        HotpaxMessage hotpaxMessage = new HotpaxMessage();
        hotpaxMessage.setContext("server ack message");
        tSession.write(hotpaxMessage);
    }
}
