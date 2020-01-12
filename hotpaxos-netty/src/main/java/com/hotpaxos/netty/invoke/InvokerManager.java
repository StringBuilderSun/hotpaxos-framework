package com.hotpaxos.netty.invoke;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: lijinpeng
 * Created by Shanghai on 2020/1/6
 */
public class InvokerManager {


    //执行器集合
    private Map<String, List<Invoker>> cacheInvokers = new ConcurrentHashMap<>();

    //添加执行器
    public void addInvoker(InvokerType invokerType, Invoker invoker) {
        List<Invoker> invokers = cacheInvokers.getOrDefault(invokerType, new ArrayList<>());
        if (!invokers.contains(invoker)) {
            invokers.add(invoker);
            cacheInvokers.put(invokerType.name(), invokers);
        }
    }


    public List<Invoker> getInvokers(InvokerType invokerType) {
      return   cacheInvokers.getOrDefault(invokerType.name(), new ArrayList<>());
    }
}
