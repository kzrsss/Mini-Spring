package com.minispring.aop;

/**
 * Advisor接口，表示切面的访问者
 * 它是切点(Pointcut)和通知(Advice)的组合，用于定义在何处以何种方式应用通知
 */
public interface Advisor {
    
    /**
     * 返回此切面使用的通知
     * @return 通知对象
     */
    Advice getAdvice();
    
    /**
     * 返回此切面是否已经实例化
     * @return 如果切面已经实例化返回true
     */
    boolean isPerInstance();
} 