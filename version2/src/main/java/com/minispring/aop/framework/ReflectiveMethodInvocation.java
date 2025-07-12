package com.minispring.aop.framework;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * 方法调用的反射实现类
 * 该类实现了MethodInvocation接口，用于通过反射调用目标方法
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    // 目标对象
    protected final Object target;
    // 目标方法
    protected final Method method;
    // 方法参数
    protected final Object[] arguments;

    /**
     * 构造函数
     * @param target 目标对象
     * @param method 目标方法
     * @param arguments 方法参数
     */
    public ReflectiveMethodInvocation(Object target, Method method, Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * 获取方法对象
     * @return 方法对象
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /**
     * 获取方法参数
     * @return 方法参数数组
     */
    @Override
    public Object[] getArguments() {
        return arguments;
    }

    /**
     * 获取目标对象
     * @return 目标对象
     */
    @Override
    public Object getThis() {
        return target;
    }

    /**
     * 执行方法调用
     * 通过反射机制调用目标方法
     * @return 方法执行结果
     * @throws Throwable 如果方法调用过程中发生异常
     */
    @Override
    public Object proceed() throws Throwable {
        return invokeJoinPoint();
    }

    /**
     * 调用连接点
     * 这个方法实际执行反射调用
     * @return 方法执行结果
     * @throws Throwable 如果方法调用过程中发生异常
     */
    protected Object invokeJoinPoint() throws Throwable {
        // 确保方法可访问
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        // 通过反射调用目标方法
        return method.invoke(target, arguments);
    }
} 