package com.hotpaxos.netty.proxy;

import com.hotpaxos.framework.common.core.IParser;

import java.lang.reflect.Method;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public class ParserEnhance {

    /**
     * @param bean
     * @param cmdMethod
     * @param cmdClass
     * @param scope
     * @param contentType
     * @param messageId
     * @param index
     * @return
     */
    public static IParser invokeEnhance(Object bean, Method cmdMethod, Class cmdClass, String scope, int contentType, int messageId, int index) {
        return null;
    }
}
