package com.minispring.web.context.request;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP请求作用域实现
 * Bean的生命周期与HTTP请求相同，一个请求内共享一个Bean实例
 */
public class RequestScope implements Scope {
    
    private final ThreadLocal<Map<String, Object>> requestScope = ThreadLocal.withInitial(HashMap::new);
    private final ThreadLocal<Map<String, Runnable>> destructionCallbacks = ThreadLocal.withInitial(HashMap::new);
    
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Map<String, Object> scope = this.requestScope.get();
        Object bean = scope.get(name);
        
        if (bean == null) {
            try {
                bean = objectFactory.getObject();
                scope.put(name, bean);
            } catch (BeansException ex) {
                throw ex;
            }
        }
        
        return bean;
    }
    
    @Override
    public Object remove(String name) {
        Map<String, Object> scope = this.requestScope.get();
        return scope.remove(name);
    }
    
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        Map<String, Runnable> callbacks = this.destructionCallbacks.get();
        callbacks.put(name, callback);
    }
    
    @Override
    public String getConversationId() {
        return "request-" + Thread.currentThread().getName();
    }
    
    /**
     * 执行请求结束回调
     * 通常在HTTP请求结束时被调用
     */
    public void endRequest() {
        Map<String, Runnable> callbacks = this.destructionCallbacks.get();
        
        for (Map.Entry<String, Runnable> entry : callbacks.entrySet()) {
            try {
                entry.getValue().run();
            }
            catch (Throwable ex) {
                System.err.println("Exception thrown while executing destruction callback for request bean [" + entry.getKey() + "]: " + ex);
            }
        }
        
        // 清理ThreadLocal资源
        this.requestScope.remove();
        this.destructionCallbacks.remove();
    }
} 