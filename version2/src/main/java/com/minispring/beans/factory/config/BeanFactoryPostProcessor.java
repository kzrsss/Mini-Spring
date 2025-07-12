package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

/**
 * Bean工厂后处理器接口
 * 允许自定义修改应用上下文的Bean定义，调整上下文的Bean属性值
 * 在所有Bean定义加载完成后，但在Bean实例化之前调用
 */
public interface BeanFactoryPostProcessor {
    
    /**
     * 在Bean实例化之前修改Bean工厂中的Bean定义
     * @param beanFactory 可配置的Bean工厂
     * @throws BeansException 处理过程中的异常
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
} 