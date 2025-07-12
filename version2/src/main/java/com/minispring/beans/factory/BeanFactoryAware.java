package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * BeanFactoryAware接口
 * 实现此接口的Bean会在创建过程中被注入BeanFactory
 */
public interface BeanFactoryAware {
    
    /**
     * 设置BeanFactory
     * 在Bean属性填充后、初始化前调用
     * 
     * @param beanFactory 所属的BeanFactory
     * @throws BeansException 如果设置过程中发生错误
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
} 