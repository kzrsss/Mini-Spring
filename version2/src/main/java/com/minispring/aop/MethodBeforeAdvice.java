package com.minispring.aop;

import java.lang.reflect.Method;

/**
 * 方法前置通知接口
 * 在目标方法执行前执行自定义的逻辑
 */
public interface MethodBeforeAdvice extends BeforeAdvice {
    
    /**
     * 在目标方法执行前被调用
     *
     * @param method 正在被调用的方法
     * @param args 方法的参数
     * @param target 目标对象
     * @throws Throwable 可能抛出的异常
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
} 