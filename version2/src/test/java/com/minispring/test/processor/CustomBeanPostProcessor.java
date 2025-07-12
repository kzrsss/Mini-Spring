package com.minispring.test.processor;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;

/**
 * 自定义BeanPostProcessor
 * 用于测试BeanPostProcessor的功能
 */
public class CustomBeanPostProcessor implements BeanPostProcessor {
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor前置处理: " + beanName);
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanPostProcessor后置处理: " + beanName);
        return bean;
    }
} 