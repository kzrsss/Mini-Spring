package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.core.io.Resource;
import com.minispring.core.io.ResourceLoader;

/**
 * Bean定义读取器接口
 * 定义从不同来源读取Bean定义的方法
 */
public interface BeanDefinitionReader {

    /**
     * 获取Bean定义注册表
     * @return Bean定义注册表
     */
    BeanDefinitionRegistry getRegistry();

    /**
     * 获取资源加载器
     * @return 资源加载器
     */
    ResourceLoader getResourceLoader();

    /**
     * 从资源中加载Bean定义
     * @param resource 资源
     * @throws BeansException 加载异常
     */
    void loadBeanDefinitions(Resource resource) throws BeansException;

    /**
     * 从多个资源中加载Bean定义
     * @param resources 资源数组
     * @throws BeansException 加载异常
     */
    void loadBeanDefinitions(Resource... resources) throws BeansException;

    /**
     * 从指定位置加载Bean定义
     * @param location 资源位置
     * @throws BeansException 加载异常
     */
    void loadBeanDefinitions(String location) throws BeansException;

    /**
     * 从多个位置加载Bean定义
     * @param locations 资源位置数组
     * @throws BeansException 加载异常
     */
    void loadBeanDefinitions(String... locations) throws BeansException;
} 