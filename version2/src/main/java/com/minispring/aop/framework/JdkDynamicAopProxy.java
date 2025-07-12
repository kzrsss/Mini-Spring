package com.minispring.aop.framework;

import com.minispring.aop.Advice;
import com.minispring.aop.MethodBeforeAdvice;
import com.minispring.aop.AfterReturningAdvice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 基于JDK动态代理的AOP代理实现
 * 适用于代理实现了接口的类
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    
    // 代理配置
    private final AdvisedSupport advised;
    
    /**
     * 创建一个新的JdkDynamicAopProxy
     * @param advised 代理配置
     */
    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }
    
    @Override
    public Object getProxy() {
        return getProxy(Thread.currentThread().getContextClassLoader());
    }
    
    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (this.advised.getTargetSource() == null) {
            throw new IllegalStateException("TargetSource cannot be null when creating a proxy");
        }
        
        Class<?> targetClass = this.advised.getTargetSource().getTargetClass();
        // 获取目标类实现的所有接口
        Class<?>[] interfaces = targetClass.getInterfaces();
        if (interfaces.length == 0) {
            throw new IllegalStateException("Target class '" + targetClass.getName() + 
                    "' does not implement any interfaces, cannot create JDK proxy");
        }
        
        // 创建代理
        return Proxy.newProxyInstance(classLoader, interfaces, this);
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = null;
        
        try {
            target = this.advised.getTargetSource().getTarget();
            if (target == null) {
                throw new IllegalStateException("Target is null");
            }
            
            System.out.println("JdkDynamicAopProxy.invoke: method=" + method.getName() + ", target=" + target.getClass().getName());
            
            // 获取目标类中的方法（而不是接口的方法）
            Method targetMethod = null;
            try {
                targetMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                // 如果找不到方法，则使用接口方法
                targetMethod = method;
            }
            
            // 获取方法对应的拦截器链
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, this.advised.getTargetClass());
            System.out.println("JdkDynamicAopProxy.invoke: interceptors=" + chain.size());
            
            // 如果没有拦截器，直接调用目标方法
            if (chain.isEmpty()) {
                System.out.println("JdkDynamicAopProxy.invoke: No interceptors, direct invoke");
                return method.invoke(target, args);
            }
            
            // 创建方法调用
            ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(target, targetMethod, args);
            
            // 处理拦截器链
            return processInterceptors(chain, invocation);
        } finally {
            if (target != null) {
                this.advised.getTargetSource().releaseTarget(target);
            }
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
        System.out.println("Starting to process interceptor chain with " + chain.size() + " interceptors");
        
        // 创建一个包含拦截器链的方法调用对象
        AopMethodInvocation methodInvocation = new AopMethodInvocation(invocation, chain);
        
        // 执行方法调用，这将依次执行所有拦截器和目标方法
        return methodInvocation.proceed();
    }
    
    /**
     * AOP方法调用实现类，包含了拦截器链的处理逻辑
     */
    private static class AopMethodInvocation extends ReflectiveMethodInvocation {
        private final List<Object> interceptorsAndAdvices;
        private int currentInterceptorIndex = -1;
        
        public AopMethodInvocation(ReflectiveMethodInvocation invocation, List<Object> interceptorsAndAdvices) {
            super(invocation.getThis(), invocation.getMethod(), invocation.getArguments());
            this.interceptorsAndAdvices = interceptorsAndAdvices;
        }
        
        @Override
        public Object proceed() throws Throwable {
            // 所有拦截器已执行完毕，调用目标方法
            if (this.currentInterceptorIndex == this.interceptorsAndAdvices.size() - 1) {
                System.out.println("Invoking target method directly");
                return super.proceed();
            }
            
            // 获取下一个拦截器
            Object interceptorOrAdvice = this.interceptorsAndAdvices.get(++this.currentInterceptorIndex);
            System.out.println("Processing interceptor: " + interceptorOrAdvice.getClass().getName());
            
            // 处理不同类型的通知
            if (interceptorOrAdvice instanceof MethodBeforeAdvice) {
                System.out.println("Executing before advice");
                MethodBeforeAdvice beforeAdvice = (MethodBeforeAdvice) interceptorOrAdvice;
                beforeAdvice.before(getMethod(), getArguments(), getThis());
                return proceed();
            } else if (interceptorOrAdvice instanceof AfterReturningAdvice) {
                System.out.println("Executing after returning advice");
                Object returnValue = proceed();
                AfterReturningAdvice afterAdvice = (AfterReturningAdvice) interceptorOrAdvice;
                afterAdvice.afterReturning(returnValue, getMethod(), getArguments(), getThis());
                return returnValue;
            } else {
                throw new IllegalStateException("Unknown advice type: " + interceptorOrAdvice.getClass());
            }
        }
    }
} 