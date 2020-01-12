package com.hotpaxos.server.starter.handler;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.TSession;
import com.hotpaxos.framework.common.core.invoke.HotPaxInvoke;
import com.hotpaxos.framework.common.core.invoke.InvokeConstants;
import com.hotpaxos.pb.HotPaxMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;

/**
 * 握手执行器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
@Slf4j
public class HandleShakeInvoke extends HotPaxInvoke {
    @Override
    public String[] invokeType() {
        return new String[]{InvokeConstants.OPEN_TYPE};
    }

    @Override
    public int invokeOrder() {
        //优先级最高
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void invoke(TSession tSession) {
        //向服务器发送握手信息
        HotPaxMsg.Syn syn = HotPaxMsg.Syn.newBuilder()
                .setMessage("ack...")
                .build();
        HotpaxMessage message = new HotpaxMessage();
        message.setContentBytes(syn.toByteArray());
        tSession.send(message);
        log.info("handshake invoke send ack to host:{}", tSession.ipAndPort());
    }
}
