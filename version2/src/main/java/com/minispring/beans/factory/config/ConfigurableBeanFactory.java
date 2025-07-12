package com.minispring.beans.factory.config;

import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.BeansException;

/**
 * 可配置的Bean工厂接口
 * 扩展了BeanFactory接口，增加了配置Bean工厂的功能
 */
public interface ConfigurableBeanFactory extends BeanFactory {
    
    /**
     * 单例作用域的常量标识
     */
    String SCOPE_SINGLETON = "singleton";
    
    /**
     * 原型作用域的常量标识
     */
    String SCOPE_PROTOTYPE = "prototype";
    
    /**
     * 注册作用域
     * @param scopeName 作用域名称
     * @param scope 作用域对象
     */
    void registerScope(String scopeName, Scope scope);
    
    /**
     * 获取注册的作用域
     * @param scopeName 作用域名称
     * @return 作用域对象，如果未找到则返回null
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
     * 设置父级Bean工厂
     * @param parentBeanFactory 父级Bean工厂
     */
    void setParentBeanFactory(BeanFactory parentBeanFactory);
    
    /**
     * 添加BeanPostProcessor
     * @param beanPostProcessor Bean后处理器
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    
    /**
     * 销毁所有单例Bean
     * 在容器关闭时调用
     */
    void destroySingletons();
} 