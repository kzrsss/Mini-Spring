package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

import java.util.Map;

/**
 * 可列表的BeanFactory接口
 * 提供列出Bean的方法
 */
public interface ListableBeanFactory extends BeanFactory {
    
    /**
     * 获取指定类型的所有Bean
     * 
     * @param type Bean类型
     * @return Bean名称到Bean实例的映射
     * @throws BeansException 如果获取失败
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
    
    /**
     * 获取所有Bean定义的名称
     * 
     * @return Bean定义名称数组
     */
    String[] getBeanDefinitionNames();
    
    /**
     * 检查是否包含指定名称的Bean定义
     * 
     * @param beanName Bean名称
     * @return 如果包含返回true
     */
    boolean containsBeanDefinition(String beanName);
} 