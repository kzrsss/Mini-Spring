package com.minispring.context;

import java.util.EventListener;

/**
 * 应用事件监听器接口
 * 实现观察者模式，监听应用事件
 * 
 * @param <E> 事件类型
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {
    
    /**
     * 处理应用事件
     * 
     * @param event 要处理的事件
     */
    void onApplicationEvent(E event);
} 