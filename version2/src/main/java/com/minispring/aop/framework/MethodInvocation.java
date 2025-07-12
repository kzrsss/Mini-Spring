package com.minispring.aop.framework;

import java.lang.reflect.Method;

/**
 * 方法调用的抽象接口
 * 表示一次运行时方法调用
 */
public interface MethodInvocation {
    
    /**
     * 获取被调用的方法
     * @return 方法对象
     */
    Method getMethod();
    
    /**
     * 获取方法的参数
     * @return 参数数组
     */
    Object[] getArguments();
    
    /**
     * 获取目标对象
     * @return 目标对象
     */
    Object getThis();
    
    /**
     * 继续方法调用链
     * @return 方法调用的返回值
     * @throws Throwable 方法调用可能抛出的异常
     */
    Object proceed() throws Throwable;
} 