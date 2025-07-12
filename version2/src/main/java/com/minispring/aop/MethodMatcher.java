package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * 方法匹配器接口，用于确定哪些方法应该被代理
 */
public interface MethodMatcher {
    
    /**
     * 判断给定的方法是否匹配
     * @param method 要检查的方法
     * @param targetClass 目标类
     * @return 如果方法匹配则返回true
     */
    boolean matches(Method method, Class<?> targetClass);
    
    /**
     * 判断是否是动态方法匹配器
     * 静态匹配：在代理创建时就能确定方法是否匹配
     * 动态匹配：在每次方法调用时都需要检查方法是否匹配（参数值可能影响匹配结果）
     * @return 如果是动态匹配器返回true
     */
    boolean isRuntime();
    
    /**
     * 检查给定方法和参数是否匹配（仅用于运行时动态匹配）
     * @param method 要检查的方法
     * @param targetClass 目标类
     * @param args 方法参数
     * @return 如果方法匹配则返回true
     */
    boolean matches(Method method, Class<?> targetClass, Object... args);
    
    /**
     * 默认的方法匹配器，匹配所有方法
     */
    MethodMatcher TRUE = new MethodMatcher() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return true;
        }
        
        @Override
        public boolean isRuntime() {
            return false;
        }
        
        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return true;
        }
    };
} 