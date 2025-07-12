package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;

/**
 * Bean后处理器接口
 * 允许自定义修改新创建的Bean实例，例如检查标记接口或用代理包装Bean
 * 这是Spring AOP、事务等功能的基础
 */
public interface BeanPostProcessor {
    
    /**
     * 在Bean初始化之前应用这个BeanPostProcessor
     * @param bean 原始的Bean实例
     * @param beanName Bean的名称
     * @return 要使用的Bean实例，原始Bean或包装后的Bean
     * @throws BeansException 处理过程中的异常
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    /**
     * 在Bean初始化之后应用这个BeanPostProcessor
     * @param bean 原始的Bean实例
     * @param beanName Bean的名称
     * @return 要使用的Bean实例，原始Bean或包装后的Bean
     * @throws BeansException 处理过程中的异常
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
} 