package com.minispring.beans.factory.config;

import com.minispring.beans.factory.ObjectFactory;

/**
 * Bean作用域接口
 * 定义Bean的作用域行为和生命周期
 */
public interface Scope {
    
    /**
     * 从作用域中获取对象
     * @param name Bean名称
     * @param objectFactory 创建Bean对象的工厂，如果不存在则使用工厂创建
     * @return 作用域中的Bean实例
     */
    Object get(String name, ObjectFactory<?> objectFactory);
    
    /**
     * 从作用域中移除对象
     * @param name Bean名称
     * @return 被移除的对象，如果没有找到则返回null
     */
    Object remove(String name);
    
    /**
     * 注册销毁回调函数
     * 当作用域结束时，会调用此回调
     * @param name Bean名称
     * @param callback 销毁回调
     */
    void registerDestructionCallback(String name, Runnable callback);
    
    /**
     * 获取此作用域的会话标识
     * 可用于区分不同作用域实例
     * @return 会话标识，如果没有则返回null
     */
    String getConversationId();
} 