package com.minispring.beans.factory;

/**
 * 层次结构的BeanFactory接口
 * 定义具有父子关系的BeanFactory
 */
public interface HierarchicalBeanFactory extends BeanFactory {
    
    /**
     * 获取父BeanFactory
     * 
     * @return 父BeanFactory，如果没有则返回null
     */
    BeanFactory getParentBeanFactory();
    
    /**
     * 检查本地BeanFactory是否包含指定名称的Bean
     * 
     * @param name Bean名称
     * @return 如果本地包含返回true
     */
    boolean containsLocalBean(String name);
} 