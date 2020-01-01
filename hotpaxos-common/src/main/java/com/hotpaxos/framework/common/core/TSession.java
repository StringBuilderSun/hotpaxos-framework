package com.hotpaxos.framework.common.core;

import com.hotpaxos.framework.common.exception.HotPaxosException;
import com.hotpaxos.framework.common.utils.HotpaxosConstans;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@ToString(exclude = {"parser", "channel"})
public abstract class TSession {
    private int appId = -1;
    private final String channelId;
    private final long createTime;
    private final String ip;
    private final int port;
    private Channel channel;
    protected final Parser parser;
    private final List<String> scopes;
    private final ConcurrentHashMap<String, Object> attrs = new ConcurrentHashMap<>();

    public TSession(Channel channel, Parser parser) {
        this.createTime = System.currentTimeMillis();
        this.channel = channel;
        this.parser = parser;
        this.channelId = channel.id().asLongText();
        String[] params = channel.remoteAddress().toString().split(":");
        this.ip = parserIp(params[0]);
        this.port = parserPort(params[1]);
        this.scopes = Arrays.asList(HotpaxosConstans.DEFAULT_SCOPE, HotpaxosConstans.HAND_SHAKE_SCOPE, HotpaxosConstans.AGENT_SCOPE);
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public void close() {
        channel.close();
    }

    public <T> T setAttr(String key, T value) {
        return (T) attrs.put(key, value);
    }

    public Object setAttrIfAbsent(String key, Object value) {
        return attrs.putIfAbsent(key, value);
    }

    public boolean hasAttr(String key) {
        return attrs.containsKey(key);
    }

    public <T> T getAttr(String key) {
        return (T) attrs.get(key);
    }

    public <T> T getAttr(String key, T defaultValue) {
        T attr = (T) attrs.get(key);
        return attr == null ? defaultValue : attr;
    }

    public <T> T removeAttr(String key) {
        return (T) attrs.remove(key);
    }

    public String ipAndPort() {
        return ip + HotpaxosConstans.COLON_SEPARATOR + port;
    }

    public void setAppId(int appId) {
        if (-1 != this.appId) {
            throw new HotPaxosException("the appid has already been set,please do not set again.");
        }
        this.appId = appId;
    }

    public void setScope(List<String> scopes) {
        if (null != scopes) {
            for (String scope : scopes) {
                if (!this.scopes.contains(scope)) {
                    this.scopes.add(scope);
                }
            }
        }
    }

    //获取服务pid
    public Integer getTid() {
        return this.channel.attr(HotpaxosConstans.TID_KEY).get();
    }

    //发送数据
    public abstract void send(HotpaxMessage hotpaxMessage);

    //发送数据
    public abstract void write(HotpaxMessage hotpaxMessage);

    //发送数据
    public abstract void writeAndFlush(ByteBuf byteBuf);

    //发送数据 带监听器
    public abstract void writeAndFlush(ByteBuf byteBuf, GenericFutureListener<? extends Future<? super Void>> listener);

    //将模板信息转换为byteBuf
    public abstract ByteBuf parserByteBuf(HotpaxMessage hotpaxMessage);

    private int parserPort(String param) {
        return Integer.valueOf(param);
    }


    private String parserIp(String param) {
        return param;
    }


}
