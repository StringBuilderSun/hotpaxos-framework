package com.hotpaxos.netty.proxy;

import com.hotpaxos.framework.common.core.IHandler;

import java.lang.reflect.Method;

/**
 * IHandler 代理工具
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public class HandlerEnhance {

    /**
     * 创建IHandler代理对象
     * @param bean 被代理的对象
     * @param method 要增强的方法
     * @param scope
     * @param index
     * @return
     */
    public static IHandler invokeEnhance(Object bean, Method method, String scope, int index) {
        return null;
    }
}
