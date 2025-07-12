package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.Resource;

/**
 * 基于XML配置的应用上下文抽象实现
 * 从XML文件加载Bean定义
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
    
    /**
     * 加载Bean定义
     * 从XML配置文件加载Bean定义
     * 
     * @param beanFactory Bean工厂
     * @throws BeansException 如果加载过程中发生错误
     */
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        // 创建XmlBeanDefinitionReader
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        
        // 获取配置文件资源
        Resource[] configResources = getConfigResources();
        if (configResources != null) {
            // 从资源加载Bean定义
            beanDefinitionReader.loadBeanDefinitions(configResources);
        }
        
        // 获取配置文件位置
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            // 从位置加载Bean定义
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }
    
    /**
     * 获取配置资源
     * 默认实现返回null，子类可以覆盖
     * 
     * @return 配置资源数组
     */
    protected Resource[] getConfigResources() {
        return null;
    }
    
    /**
     * 获取配置文件位置
     * 由子类实现，返回配置文件的位置
     * 
     * @return 配置文件位置数组
     */
    protected abstract String[] getConfigLocations();
} 