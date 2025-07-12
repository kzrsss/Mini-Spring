package com.minispring.aop.framework;

/**
 * AOP代理的核心接口
 * 定义获取代理对象的方法
 */
public interface AopProxy {
    
    /**
     * 创建一个新的代理对象
     * @return 代理对象
     */
    Object getProxy();
    
    /**
     * 使用给定的类加载器创建一个新的代理对象
     * @param classLoader 用于创建代理的类加载器
     * @return 代理对象
     */
    Object getProxy(ClassLoader classLoader);
} 