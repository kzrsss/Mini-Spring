package com.minispring.aop;

/**
 * 具有切点的切面接口
 * 扩展了Advisor接口，添加了获取切点的方法
 */
public interface PointcutAdvisor extends Advisor {
    
    /**
     * 返回此切面的切点
     * @return 切点对象
     */
    Pointcut getPointcut();
} 