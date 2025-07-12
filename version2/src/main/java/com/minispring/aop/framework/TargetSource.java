package com.minispring.aop.framework;

/**
 * TargetSource接口用于封装目标对象
 * AOP代理的最终目标
 */
public interface TargetSource {
    
    /**
     * 返回目标对象的类型
     * @return 目标类型
     */
    Class<?> getTargetClass();
    
    /**
     * 返回是否返回相同的目标对象
     * @return 如果每次调用返回相同的目标对象则返回true
     */
    boolean isStatic();
    
    /**
     * 获取目标对象
     * 每次方法调用都可能被调用
     * @return 目标对象
     */
    Object getTarget() throws Exception;
    
    /**
     * 释放目标对象
     * 在方法调用完成后可能被调用
     * @param target 要释放的目标对象
     */
    void releaseTarget(Object target) throws Exception;
} 