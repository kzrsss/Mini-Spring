package com.minispring.aop;

/**
 * Pointcut接口，用于定义拦截规则。
 * 切点是一个匹配规则，用于确定哪些方法应该被拦截。
 * 它结合了类匹配器和方法匹配器来定义精确的拦截范围。
 */
public interface Pointcut {
    
    /**
     * 返回切点的类匹配器，用于确定哪些类应该被拦截
     * @return 类匹配器
     */
    ClassFilter getClassFilter();
    
    /**
     * 返回切点的方法匹配器，用于确定类中哪些方法应该被拦截
     * @return 方法匹配器
     */
    MethodMatcher getMethodMatcher();
    
    /**
     * 代表匹配所有的切点常量
     */
    Pointcut TRUE = TruePointcut.INSTANCE;
} 