package com.hotpaxos.framework.common.core;

/**
 * 消息处理器 消息会被封装成该接口的代理类
 * 用于调用注解方法
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public interface IHandler {
    /**
     * 使用了HotPaxMsgAno注解方法 所在的类
     *
     * @return
     */
    Object getBean();

    /**
     * 获取注解标识的作用域
     *
     * @return
     */
    String getScope();

    /**
     * 该方法会增强使用了HotPaxMsgAno注解的方法
     * @param target
     * @param cmd
     * @param header
     */
    void handle(Object target, Object cmd, Object header);
}
