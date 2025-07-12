package com.minispring.beans.factory.config;

import com.minispring.beans.factory.ConfigurableBeanFactory;
import com.minispring.web.context.request.RequestScope;
import com.minispring.web.context.request.SessionScope;

/**
 * 作用域注册器
 * 用于向Bean工厂注册各种作用域
 */
public class ScopeRegistrar {
    
    /**
     * 注册标准作用域（单例和原型）
     * @param beanFactory 目标Bean工厂
     */
    public static void registerStandardScopes(ConfigurableBeanFactory beanFactory) {
        // 注册单例作用域
        beanFactory.registerScope(ConfigurableBeanFactory.SCOPE_SINGLETON, new SingletonScope());
        
        // 注册原型作用域
        beanFactory.registerScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE, new PrototypeScope());
    }
    
    /**
     * 注册Web相关作用域
     * @param beanFactory 目标Bean工厂
     */
    public static void registerWebScopes(ConfigurableBeanFactory beanFactory) {
        // 注册请求作用域
        beanFactory.registerScope("request", new RequestScope());
        
        // 注册会话作用域
        beanFactory.registerScope("session", new SessionScope());
    }
    
    /**
     * 注册所有作用域
     * @param beanFactory 目标Bean工厂
     */
    public static void registerAllScopes(ConfigurableBeanFactory beanFactory) {
        registerStandardScopes(beanFactory);
        
        // 这里可以根据环境类型判断是否注册Web作用域
        try {
            // 检查是否存在Web相关类
            Class.forName("javax.servlet.http.HttpServletRequest");
            // 如果存在，注册Web作用域
            registerWebScopes(beanFactory);
        } catch (ClassNotFoundException e) {
            // 不是Web环境，忽略
        }
    }
} 