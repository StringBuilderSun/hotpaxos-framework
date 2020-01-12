package com.hotpaxos.netty.filter;

import com.hotpaxos.framework.common.core.IFilter;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IFilter 注册器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/12
 */
public class FilterRegister {

    private final static FilterRegister filterRegister = new FilterRegister();

    private final Map<String, IFilter> filters = new ConcurrentHashMap<>();

    private FilterRegister() {
    }

    public static FilterRegister instance() {
        return filterRegister;
    }

    public void put(String key, IFilter filter) {
        this.filters.putIfAbsent(key, filter);
    }

    public void remove(String key) {
        this.filters.remove(key);
    }

    public IFilter getFilter(String key) {
        return this.filters.get(key);
    }

    public int size() {
        return this.filters.size();
    }

    public Collection<IFilter> getAllFilter() {
        return filters.values();
    }
}
