package com.hotpaxos.framework.common.utils;

import com.hotpaxos.framework.common.core.HotpaxMessage;
import com.hotpaxos.framework.common.core.TSession;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * 通道发送管理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
@Slf4j
public class ChannelUtil {

    /**
     * 发送心跳包
     *
     * @param channel    当前通道
     * @param pingPacket 是ping还是pong
     */
    public static void sendHeartPing(Channel channel, boolean pingPacket) {
        //当客户端被激活时，会将Tsession channel 属性中
        TSession tSession = channel.attr(HotpaxosConstans.SESSION_ID).get();
        sendHeartBeatMsg(tSession, pingPacket);
    }

    //发送心跳包
    private static void sendHeartBeatMsg(TSession tSession, boolean pingPacket) {
        HotpaxMessage heartMsg = HotPaxMessageInit(pingPacket ? HotpaxosConstans.PING_MESSAGE_ID : HotpaxosConstans.PONG_MESSAGE_ID);
        log.debug("send hearbeat pack to server:{} msg:{}", tSession.ipAndPort(), heartMsg);
        tSession.send(heartMsg);
    }

    public static HotpaxMessage HotPaxMessageInit(int messageId) {
        HotpaxMessage message = new HotpaxMessage();
        message.putMessageId(messageId);
        message.putAppId(AppIdUtils.loadAppId());
        message.putParserVersion((byte) '0');
        message.putSendType((byte) '0');
        message.setContext("ping message");
        return message;
    }
}
