package com.minispring.context.event;

import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationEvent;

/**
 * 上下文关闭事件
 * 当ApplicationContext被关闭时发布
 */
public class ContextClosedEvent extends ApplicationEvent {
    
    /**
     * 构造函数
     * 
     * @param source 事件源（应用上下文）
     */
    public ContextClosedEvent(ApplicationContext source) {
        super(source);
    }
    
    /**
     * 获取应用上下文
     * 
     * @return 应用上下文
     */
    public ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
} 