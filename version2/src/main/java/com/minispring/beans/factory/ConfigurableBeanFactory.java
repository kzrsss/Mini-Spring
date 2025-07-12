package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.beans.factory.config.Scope;

/**
 * 可配置的BeanFactory接口
 * 提供配置BeanFactory的方法
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {
    
    /**
     * 单例作用域
     */
    String SCOPE_SINGLETON = "singleton";
    
    /**
     * 原型作用域
     */
    String SCOPE_PROTOTYPE = "prototype";
    
    /**
     * 注册作用域
     * @param scopeName 作用域名称
     * @param scope 作用域实现
     */
    void registerScope(String scopeName, Scope scope);
    
    /**
     * 获取注册的作用域
     * @param scopeName 作用域名称
     * @return 作用域实现，如果未找到则返回null
     */
    Scope getRegisteredScope(String scopeName);
    
    /**
     * 获取Bean的类型
     * @param name Bean的名称
     * @return Bean的类型
     * @throws BeansException 如果无法获取类型
     */
    Class<?> getType(String name) throws BeansException;
    
    /**
     * 添加BeanPostProcessor
     * 
     * @param beanPostProcessor 要添加的处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    
    /**
     * 销毁单例Bean
     */
    void destroySingletons();
} 