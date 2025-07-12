package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * 方法返回通知接口
 * 在目标方法成功执行后执行自定义的逻辑
 */
public interface AfterReturningAdvice extends AfterAdvice {
    
    /**
     * 在目标方法成功执行后被调用
     *
     * @param returnValue The value returned by the method, if any
     * @param method 被执行的方法
     * @param args 方法的参数
     * @param target 目标对象
     * @throws Throwable 可能抛出的异常
     */
    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;
} 