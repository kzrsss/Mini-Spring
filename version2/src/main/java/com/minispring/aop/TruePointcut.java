package com.minispring.aop;

/**
 * 匹配所有类和方法的切点实现
 */
final class TruePointcut implements Pointcut {
    
    /**
     * 单例实例
     */
    public static final TruePointcut INSTANCE = new TruePointcut();
    
    /**
     * 私有构造函数，防止外部实例化
     */
    private TruePointcut() {
    }
    
    @Override
    public ClassFilter getClassFilter() {
        return ClassFilter.TRUE;
    }
    
    @Override
    public MethodMatcher getMethodMatcher() {
        return MethodMatcher.TRUE;
    }
    
    /**
     * 返回单例实例
     */
    private Object readResolve() {
        return INSTANCE;
    }
} 