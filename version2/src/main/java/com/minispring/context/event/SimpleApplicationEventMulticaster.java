package com.minispring.context.event;

import com.minispring.beans.factory.BeanFactory;
import com.minispring.context.ApplicationEvent;
import com.minispring.context.ApplicationListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 简单的应用事件多播器实现
 * 维护一个监听器列表，在事件发生时通知所有匹配的监听器
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {
    
    /**
     * 监听器列表
     */
    private final List<ApplicationListener<?>> listeners = new ArrayList<>();
    
    /**
     * Bean工厂，用于获取监听器
     */
    private BeanFactory beanFactory;
    
    /**
     * 默认构造函数
     */
    public SimpleApplicationEventMulticaster() {
    }
    
    /**
     * 构造函数
     * 
     * @param beanFactory Bean工厂
     */
    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    /**
     * 设置Bean工厂
     * 
     * @param beanFactory Bean工厂
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    /**
     * 添加事件监听器
     * 
     * @param listener 要添加的监听器
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        listeners.add(listener);
    }
    
    /**
     * 移除事件监听器
     * 
     * @param listener 要移除的监听器
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        listeners.remove(listener);
    }
    
    /**
     * 删除所有监听器
     */
    @Override
    public void removeAllListeners() {
        listeners.clear();
    }
    
    /**
     * 将事件多播给所有匹配的监听器
     * 
     * @param event 要多播的事件
     */
    @Override
    public void multicastEvent(ApplicationEvent event) {
        for (ApplicationListener listener : getApplicationListeners(event)) {
            invokeListener(listener, event);
        }
    }
    
    /**
     * 获取匹配事件的所有监听器
     * 
     * @param event 事件
     * @return 匹配的监听器列表
     */
    private Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        List<ApplicationListener> allListeners = new ArrayList<>();
        for (ApplicationListener<?> listener : listeners) {
            if (supportsEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }
    
    /**
     * 检查监听器是否支持给定的事件
     * 
     * @param listener 监听器
     * @param event 事件
     * @return 如果支持返回true
     */
    private boolean supportsEvent(ApplicationListener<?> listener, ApplicationEvent event) {
        // 简化实现，默认所有监听器都支持所有事件
        // 实际Spring中会使用反射检查泛型类型
        return true;
    }
    
    /**
     * 调用监听器处理事件
     * 
     * @param listener 监听器
     * @param event 事件
     */
    @SuppressWarnings("unchecked")
    private void invokeListener(ApplicationListener listener, ApplicationEvent event) {
        try {
            listener.onApplicationEvent(event);
        } catch (Exception e) {
            // 日志记录或重新抛出异常
            System.err.println("处理事件时发生错误：" + e.getMessage());
        }
    }
} 