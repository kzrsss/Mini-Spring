package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * Bean工厂接口
 * IoC容器的核心接口，定义获取Bean的方法
 */
public interface BeanFactory {

    /**
     * 根据Bean名称获取Bean实例
     * @param name Bean名称
     * @return Bean实例
     * @throws BeansException 如果获取Bean失败
     */
    Object getBean(String name) throws BeansException;

    /**
     * 根据Bean名称和类型获取Bean实例
     * @param name Bean名称
     * @param requiredType Bean类型
     * @param <T> Bean类型
     * @return Bean实例
     * @throws BeansException 如果获取Bean失败
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException;

    /**
     * 根据Bean类型获取Bean实例
     * @param requiredType Bean类型
     * @param <T> Bean类型
     * @return Bean实例
     * @throws BeansException 如果获取Bean失败
     */
    <T> T getBean(Class<T> requiredType) throws BeansException;
    
    /**
     * 根据Bean名称和构造参数获取Bean实例
     * @param name Bean名称
     * @param args 构造参数
     * @return Bean实例
     * @throws BeansException 如果获取Bean失败
     */
    Object getBean(String name, Object... args) throws BeansException;

    /**
     * 判断是否包含指定名称的Bean
     * @param name Bean名称
     * @return 是否包含
     */
    boolean containsBean(String name);
} 