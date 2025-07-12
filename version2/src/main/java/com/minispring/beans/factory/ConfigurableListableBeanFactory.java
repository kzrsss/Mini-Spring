package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;

import java.util.Set;

/**
 * 可配置的ListableBeanFactory接口
 * 提供配置BeanFactory的方法
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {
    
    /**
     * 根据名称获取BeanDefinition
     * 
     * @param beanName Bean名称
     * @return Bean定义
     * @throws BeansException 如果找不到Bean定义
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    
    /**
     * 预初始化所有单例Bean
     * 
     * @throws BeansException 如果初始化过程中发生错误
     */
    void preInstantiateSingletons() throws BeansException;
    
    /**
     * 获取所有Bean定义的名称
     * @return Bean定义名称数组
     */
    @Override
    String[] getBeanDefinitionNames();
    
    /**
     * 添加BeanPostProcessor
     * 
     * @param beanPostProcessor 要添加的处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
} 