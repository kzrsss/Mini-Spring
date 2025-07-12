package com.minispring.beans.factory;

import com.minispring.beans.BeansException;

/**
 * 对象工厂接口
 * 用于创建对象的工厂接口，为三级缓存提供支持
 * 主要用于解决循环依赖问题
 * @param <T> 创建对象的类型
 */
public interface ObjectFactory<T> {
    
    /**
     * 获取对象实例
     * 
     * @return 对象实例
     * @throws BeansException 如果对象创建失败
     */
    T getObject() throws BeansException;
} 