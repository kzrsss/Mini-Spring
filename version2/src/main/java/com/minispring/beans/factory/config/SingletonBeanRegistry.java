package com.minispring.beans.factory.config;

/**
 * 单例Bean注册表接口
 * 定义获取单例Bean的方法
 */
public interface SingletonBeanRegistry {

    /**
     * 获取单例Bean
     * @param beanName Bean名称
     * @return Bean实例
     */
    Object getSingleton(String beanName);

    /**
     * 注册单例Bean
     * @param beanName Bean名称
     * @param singletonObject Bean实例
     */
    void registerSingleton(String beanName, Object singletonObject);
} 