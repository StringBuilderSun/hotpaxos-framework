package com.hotpaxos.netty.invoke;

import com.hotpaxos.framework.common.core.invoke.HotPaxInvoke;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 注册拦截执行器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
public class InvokerRegister {

    private final static InvokerRegister instance = new InvokerRegister();

    public static InvokerRegister instance() {
        return instance;
    }

    private InvokerRegister() {
    }

    //执行器集合
    private Map<String, List<HotPaxInvoke>> invokes = new ConcurrentHashMap<>();

    //添加执行器
    public void put(String invokeType, HotPaxInvoke invoker) {
        List<HotPaxInvoke> invokers = invokes.computeIfAbsent(invokeType, s -> new ArrayList<>());
        if (!invokers.contains(invoker)) {
            invokers.add(invoker);
            invokes.put(invokeType, invokers);
        }
    }

    public void remove(String invokeType) {
        this.invokes.remove(invokeType);
    }

    //移除所有invoketype中的该执行器
    public void remove(HotPaxInvoke hotPaxInvoke) {
        for (List<HotPaxInvoke> invokesByType : this.invokes.values()) {
            invokesByType.remove(invokesByType);
        }
    }

    //获取指定类型的invoke集合
    public List<HotPaxInvoke> getInvokersByType(String invokerType) {
        return invokes.getOrDefault(invokerType, new ArrayList<>());
    }

    //为执行器排序
    public void sort() {
        for (String key : invokes.keySet()) {
            List<HotPaxInvoke> invokesByType = this.invokes.get(key);
            Collections.sort(invokesByType);
        }
    }
}
