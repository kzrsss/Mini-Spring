package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 原型作用域实现
 * 每次获取Bean时都会创建一个新的实例
 */
public class PrototypeScope implements Scope {
    
    // 用于保存销毁回调
    private final Map<String, Runnable> destructionCallbacks = new HashMap<>(16);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        try {
            // 原型模式下，每次都创建新对象
            return objectFactory.getObject();
        } catch (BeansException ex) {
            throw ex;
        }
    }
    
    @Override
    public Object remove(String name) {
        // 从回调集合中移除
        this.destructionCallbacks.remove(name);
        return null; // 原型模式下不缓存对象，所以返回null
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        // 注册销毁回调
        this.destructionCallbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "prototype";
    }
    
    /**
     * 执行并清除所有销毁回调
     * 注意：在原型模式下，通常由客户端负责管理生命周期
     * 此方法通常不会被调用，因为原型Bean的生命周期不由容器管理
     */
    public void destroyPrototypes() {
        String[] prototypeNames = this.destructionCallbacks.keySet().toArray(new String[0]);
        for (String name : prototypeNames) {
            Runnable callback = this.destructionCallbacks.remove(name);
            if (callback != null) {
                try {
                    callback.run();
                }
                catch (Throwable ex) {
                    System.err.println("Exception thrown while executing destruction callback for prototype [" + name + "]: " + ex);
                }
            }
        }
    }
} 