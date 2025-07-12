package com.minispring.aop;

/**
 * 类过滤器接口，用于确定哪些类应该被代理
 */
public interface ClassFilter {
    
    /**
     * 判断给定的类是否匹配
     * @param clazz 要检查的类
     * @return 如果类匹配则返回true
     */
    boolean matches(Class<?> clazz);
    
    /**
     * 默认的类过滤器，匹配所有类
     */
    ClassFilter TRUE = clazz -> true;
} 