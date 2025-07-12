package com.minispring.aop.framework;

import com.minispring.aop.AfterReturningAdvice;
import com.minispring.aop.MethodBeforeAdvice;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 基于CGLIB的AOP代理实现
 * 适用于代理未实现接口的类
 */
public class CglibAopProxy implements AopProxy {
    
    // 代理配置
    private final AdvisedSupport advised;
    
    /**
     * 创建一个新的CglibAopProxy
     * @param advised 代理配置
     */
    public CglibAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
    
    @Override
    public Object getProxy() {
        return getProxy(null);
    }
    
    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.getTargetSource() == null) {
            throw new IllegalStateException("TargetSource cannot be null when creating a proxy");
        }
        
        Class<?> targetClass = this.advised.getTargetSource().getTargetClass();
        if (targetClass == null) {
            throw new IllegalStateException("Target class must be available for creating a CGLIB proxy");
        }
        
        // 创建CGLIB增强器
        Enhancer enhancer = new Enhancer();
        if (classLoader != null) {
            enhancer.setClassLoader(classLoader);
        }
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new CglibMethodInterceptor());
        
        // 创建代理实例
        return enhancer.create();
    }
    
    /**
     * CGLIB方法拦截器
     * 处理代理方法的调用
     */
    private class CglibMethodInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Object target = null;
            
            try {
                target = advised.getTargetSource().getTarget();
                if (target == null) {
                    throw new IllegalStateException("Target is null");
                }
                
                // 获取方法对应的拦截器链
                List<Object> chain = advised.getInterceptorsAndDynamicInterceptionAdvice(method, target.getClass());
                
                // 如果没有拦截器，直接调用目标方法
                if (chain.isEmpty()) {
                    return methodProxy.invoke(target, args);
                }
                
                // 创建方法调用
                CglibMethodInvocation invocation = new CglibMethodInvocation(target, method, args, methodProxy);
                
                // 处理拦截器链
                return processInterceptors(chain, invocation);
            } finally {
                if (target != null) {
                    advised.getTargetSource().releaseTarget(target);
                }
            }
        }
    }
    
    /**
     * CGLIB方法调用
     * 扩展ReflectiveMethodInvocation，使用CGLIB的MethodProxy调用目标方法
     */
    private static class CglibMethodInvocation extends ReflectiveMethodInvocation {
        
        private final MethodProxy methodProxy;
        
        public CglibMethodInvocation(Object target, Method method, Object[] arguments, MethodProxy methodProxy) {
            super(target, method, arguments);
            this.methodProxy = methodProxy;
        }
        
        @Override
        protected Object invokeJoinPoint() throws Throwable {
            return this.methodProxy.invoke(getThis(), getArguments());
        }
    }
    
    /**
     * 处理拦截器链
     * @param chain 拦截器链
     * @param invocation 方法调用
     * @return 调用结果
     * @throws Throwable 如果处理过程中发生异常
     */
    private Object processInterceptors(List<Object> chain, ReflectiveMethodInvocation invocation) throws Throwable {
        Object returnValue = null;
        
        // 前置通知处理
        for (Object advice : chain) {
            if (advice instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advice).before(
                        invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
        }
        
        // 调用目标方法
        returnValue = invocation.proceed();
        
        // 后置通知处理
        for (Object advice : chain) {
            if (advice instanceof AfterReturningAdvice) {
                ((AfterReturningAdvice) advice).afterReturning(
                        returnValue, invocation.getMethod(), invocation.getArguments(), invocation.getThis());
            }
        }
        
        return returnValue;
    }
} 