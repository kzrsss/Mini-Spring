package com.minispring.context;

import java.util.EventObject;

/**
 * 应用事件基类
 * 所有应用事件都应继承此类
 */
public abstract class ApplicationEvent extends EventObject {
    
    /**
     * 事件发生时间
     */
    private final long timestamp;
    
    /**
     * 构造函数
     * 
     * @param source 事件源对象
     */
    public ApplicationEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 获取事件发生时间
     * 
     * @return 时间戳（毫秒）
     */
    public long getTimestamp() {
        return timestamp;
    }
} 