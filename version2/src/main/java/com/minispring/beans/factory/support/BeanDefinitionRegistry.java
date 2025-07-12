package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition注册表接口
 * 定义注册BeanDefinition的方法
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册BeanDefinition
     * @param beanName Bean名称
     * @param beanDefinition Bean定义
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 获取BeanDefinition
     * @param beanName Bean名称
     * @return Bean定义
     * @throws BeansException 如果找不到BeanDefinition
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 判断是否包含指定名称的BeanDefinition
     * @param beanName Bean名称
     * @return 是否包含
     */
    boolean containsBeanDefinition(String beanName);

    /**
     * 获取所有已注册的Bean名称
     * @return Bean名称数组
     */
    String[] getBeanDefinitionNames();
} 