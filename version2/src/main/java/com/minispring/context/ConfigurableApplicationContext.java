package com.minispring.context;

import com.minispring.beans.BeansException;
import com.minispring.core.env.ConfigurableEnvironment;

/**
 * 可配置的ApplicationContext接口
 * 提供配置应用上下文的方法
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
    
    /**
     * 刷新应用上下文
     * 
     * @throws BeansException 如果刷新过程中发生错误
     */
    void refresh() throws BeansException;
    
    /**
     * 关闭应用上下文
     */
    void close();
    
    /**
     * 发布应用事件
     * 
     * @param event 要发布的事件
     */
    void publishEvent(ApplicationEvent event);
    
    /**
     * 获取Environment
     * 
     * @return 可配置的Environment
     */
    ConfigurableEnvironment getEnvironment();
    
    /**
     * 设置Environment
     * 
     * @param environment 可配置的Environment
     */
    void setEnvironment(ConfigurableEnvironment environment);
} 