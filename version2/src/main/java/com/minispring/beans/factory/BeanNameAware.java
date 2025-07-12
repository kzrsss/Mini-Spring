package com.minispring.beans.factory;

/**
 * BeanNameAware接口
 * 实现此接口的Bean会在创建过程中被注入Bean名称
 */
public interface BeanNameAware {
    
    /**
     * 设置Bean名称
     * 在Bean属性填充后、初始化前调用
     * 
     * @param name Bean名称
     */
    void setBeanName(String name);
} 