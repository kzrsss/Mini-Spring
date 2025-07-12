package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.core.io.FileSystemResource;
import com.minispring.core.io.Resource;

/**
 * 文件系统XML应用上下文
 * 从文件系统加载XML配置文件
 */
public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {
    
    /**
     * 配置文件位置
     */
    private String[] configLocations;
    
    /**
     * 构造函数，使用单个配置文件
     * 
     * @param configLocation 配置文件位置
     * @throws BeansException 如果创建上下文失败
     */
    public FileSystemXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }
    
    /**
     * 构造函数，使用多个配置文件
     * 
     * @param configLocations 配置文件位置数组
     * @throws BeansException 如果创建上下文失败
     */
    public FileSystemXmlApplicationContext(String[] configLocations) throws BeansException {
        this.configLocations = configLocations;
        refresh();
    }
    
    /**
     * 获取配置文件位置
     * 
     * @return 配置文件位置数组
     */
    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }
    
    /**
     * 获取资源
     * 从文件系统加载资源
     * 
     * @param location 资源位置
     * @return 资源对象
     */
    @Override
    public Resource getResource(String location) {
        return new FileSystemResource(location);
    }
    
    /**
     * 检查是否包含指定名称的Bean定义
     * 
     * @param beanName Bean名称
     * @return 如果包含返回true
     */
    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }
} 