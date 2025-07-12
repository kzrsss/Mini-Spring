package com.minispring.aop.support;

import com.minispring.aop.Advice;
import com.minispring.aop.Pointcut;
import com.minispring.aop.PointcutAdvisor;

/**
 * PointcutAdvisor的默认实现
 * 可以与任何Pointcut和Advice一起使用
 */
public class DefaultPointcutAdvisor implements PointcutAdvisor {
    
    private Advice advice;
    private Pointcut pointcut = Pointcut.TRUE;
    
    /**
     * 创建一个空的DefaultPointcutAdvisor
     * 通知必须通过setAdvice方法设置
     */
    public DefaultPointcutAdvisor() {
    }
    
    /**
     * 使用给定的通知创建一个DefaultPointcutAdvisor
     * 将使用默认的Pointcut.TRUE切点
     * @param advice 要使用的通知
     */
    public DefaultPointcutAdvisor(Advice advice) {
        this.advice = advice;
    }
    
    /**
     * 使用给定的切点和通知创建一个DefaultPointcutAdvisor
     * @param pointcut 要使用的切点
     * @param advice 要使用的通知
     */
    public DefaultPointcutAdvisor(Pointcut pointcut, Advice advice) {
        this.pointcut = pointcut;
        this.advice = advice;
    }
    
    /**
     * 设置此切面的通知
     * @param advice 要设置的通知
     */
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
    
    /**
     * 设置此切面的切点
     * @param pointcut 要设置的切点
     */
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = (pointcut != null ? pointcut : Pointcut.TRUE);
    }
    
    @Override
    public Advice getAdvice() {
        return this.advice;
    }
    
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
    
    @Override
    public boolean isPerInstance() {
        return true;
    }
} 