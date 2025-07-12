package com.minispring.context.support;

import com.minispring.beans.BeansException;

/**
 * 类路径XML应用上下文
 * 从类路径加载XML配置文件
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    
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
    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }
    
    /**
     * 构造函数，使用多个配置文件
     * 
     * @param configLocations 配置文件位置数组
     * @throws BeansException 如果创建上下文失败
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException {
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