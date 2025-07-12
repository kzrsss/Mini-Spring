package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单例作用域实现
 * Bean默认的作用域，整个应用只有一个Bean实例
 */
public class SingletonScope implements Scope {
    
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, Runnable> destructionCallbacks = new HashMap<>(16);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        // 先从缓存中获取
        Object bean = this.singletonObjects.get(name);
        if (bean == null) {
            try {
                // 如果不存在，则创建并缓存
                bean = objectFactory.getObject();
                this.singletonObjects.put(name, bean);
            } catch (BeansException ex) {
                throw ex;
            }
        }
        return bean;
    }
    
    @Override
    public Object remove(String name) {
        // 移除单例对象并返回
        Object bean = this.singletonObjects.remove(name);
        // 移除对应的销毁回调
        this.destructionCallbacks.remove(name);
        return bean;
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        this.destructionCallbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "singleton";
    }
    
    /**
     * 执行所有注册的销毁回调
     * 在应用关闭时调用
     */
    public void destroySingletons() {
        String[] singletonNames = this.destructionCallbacks.keySet().toArray(new String[0]);
        for (String name : singletonNames) {
            Runnable callback = this.destructionCallbacks.remove(name);
            if (callback != null) {
                try {
                    callback.run();
                }
                catch (Throwable ex) {
                    System.err.println("Exception thrown while executing destruction callback for singleton [" + name + "]: " + ex);
                }
            }
        }
        // 清除所有单例对象
        this.singletonObjects.clear();
    }
} 