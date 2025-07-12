package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 简单实例化策略
 * 基于JDK反射机制实现的Bean实例化策略
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor<?> ctor, Object[] args) throws BeansException {
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            if (ctor != null) {
                // 参数校验
                if (args == null) {
                    args = new Object[0];
                }
                
                // 检查参数个数是否匹配
                if (args.length != ctor.getParameterCount()) {
                    throw new BeansException("构造函数参数个数不匹配: " + beanName + 
                            "，期望 " + ctor.getParameterCount() + " 个参数，但提供了 " + args.length + " 个参数");
                }
                
                // 使用指定构造函数实例化
                return ctor.newInstance(args);
            } else {
                // 使用默认构造函数实例化
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BeansException("实例化Bean失败 [" + beanName + "]", e);
        }
    }
} 