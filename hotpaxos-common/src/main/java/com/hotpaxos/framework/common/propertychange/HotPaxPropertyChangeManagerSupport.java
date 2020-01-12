package com.hotpaxos.framework.common.propertychange;

import org.springframework.util.CollectionUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * 属性变更通知处理器
 * User: lijinpeng
 * Created by Shanghai on 2020/1/2
 */
public class HotPaxPropertyChangeManagerSupport {

    private static HotPaxPropertyChangeManagerSupport instance = new HotPaxPropertyChangeManagerSupport();

    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private HotPaxPropertyChangeManagerSupport() {

    }

    public static HotPaxPropertyChangeManagerSupport getInstance() {
        return instance;
    }


    //调用此方法 会执行通知处理器执行处理业务
    public void pushPropsEvent(String propertyName, Object oldValue, Object newValue) {
        listeners.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void pushPropsEvent(String propertyName, Object newValue) {
        pushPropsEvent(propertyName, null, newValue);
    }

    //添加处理器
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void addPropertyChanngeListener(String propertyName, PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChanngeListener(List<PropertyChangeListener> listeners) {
        if (CollectionUtils.isEmpty(listeners)) {
            return;
        }
        for (PropertyChangeListener listener : listeners) {
            this.listeners.addPropertyChangeListener(listener);
        }
    }

    //移除处理器
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String propertyName) {
        PropertyChangeListener[] listeners = this.listeners.getPropertyChangeListeners(propertyName);
        removePropertyChangeListener(propertyName, listeners);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener[] propertyChangeListeners) {
        if (null != propertyChangeListeners && propertyChangeListeners.length > 0) {
            for (PropertyChangeListener listener : propertyChangeListeners) {
                this.listeners.removePropertyChangeListener(propertyName, listener);
            }
        }
    }

}
