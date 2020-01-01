package com.hotpaxos.netty.dispather;

import com.hotpaxos.framework.common.core.ActionDispatcher;
import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.TSession;
import lombok.extern.slf4j.Slf4j;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/1
 */
@Slf4j
public class AbstractActionDispatcher implements ActionDispatcher {
    @Override
    public void open(TSession tSession) {

    }

    @Override
    public void handleAction(TSession tSession, HotpaxMessage hotpaxMessage) {
      log.info("收到消息:TSession： message：{}",tSession.ipAndPort(),hotpaxMessage.getContext());
    }

    @Override
    public void disconnect(TSession tSession) {

    }
}
