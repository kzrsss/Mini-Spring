package com.minispring.context;

import com.minispring.beans.BeansException;

/**
 * ApplicationContextAware接口
 * 实现此接口的Bean会在创建过程中被注入ApplicationContext
 */
public interface ApplicationContextAware {
    
    /**
     * 设置ApplicationContext
     * 在Bean属性填充后、初始化前调用
     * 
     * @param applicationContext 所属的ApplicationContext
     * @throws BeansException 如果设置过程中发生错误
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
} 