package com.minispring.aop.aspectj;

import com.minispring.aop.ClassFilter;
import com.minispring.aop.MethodMatcher;
import com.minispring.aop.Pointcut;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJ表达式切点
 * 使用AspectJ的表达式语言定义切点
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {
    
    // AspectJ支持的切点原语
    private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<>();
    
    static {
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }
    
    // 切点表达式
    private String expression;
    
    // AspectJ切点表达式对象
    private PointcutExpression pointcutExpression;
    
    // 类加载器，用于解析表达式
    private final ClassLoader pointcutClassLoader;
    
    /**
     * 创建一个新的AspectJExpressionPointcut
     */
    public AspectJExpressionPointcut() {
        this(null, null);
    }
    
    /**
     * 创建一个新的AspectJExpressionPointcut
     * @param expression 切点表达式
     */
    public AspectJExpressionPointcut(String expression) {
        this(expression, null);
    }
    
    /**
     * 创建一个新的AspectJExpressionPointcut
     * @param expression 切点表达式
     * @param pointcutClassLoader 类加载器
     */
    public AspectJExpressionPointcut(String expression, ClassLoader pointcutClassLoader) {
        this.expression = expression;
        this.pointcutClassLoader = (pointcutClassLoader != null ? pointcutClassLoader :
                AspectJExpressionPointcut.class.getClassLoader());
        if (expression != null) {
            buildPointcutExpression();
        }
    }
    
    /**
     * 设置切点表达式
     * @param expression 切点表达式
     */
    public void setExpression(String expression) {
        this.expression = expression;
        buildPointcutExpression();
    }
    
    /**
     * 获取切点表达式
     * @return 切点表达式
     */
    public String getExpression() {
        return this.expression;
    }
    
    /**
     * 构建AspectJ切点表达式
     */
    private void buildPointcutExpression() {
        if (this.expression == null) {
            throw new IllegalStateException("Expression must not be null");
        }
        
        PointcutParser parser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
                SUPPORTED_PRIMITIVES, this.pointcutClassLoader);
        this.pointcutExpression = parser.parsePointcutExpression(this.expression);
    }
    
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }
    
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }
    
    @Override
    public boolean matches(Class<?> clazz) {
        checkReadyToMatch();
        return this.pointcutExpression.couldMatchJoinPointsInType(clazz);
    }
    
    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        return shadowMatch.alwaysMatches();
    }
    
    @Override
    public boolean isRuntime() {
        checkReadyToMatch();
        return this.pointcutExpression.mayNeedDynamicTest();
    }
    
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = this.pointcutExpression.matchesMethodExecution(method);
        return shadowMatch.alwaysMatches();
    }
    
    /**
     * 检查是否准备好进行匹配
     */
    private void checkReadyToMatch() {
        if (this.pointcutExpression == null) {
            throw new IllegalStateException("Must set expression before attempting to match");
        }
    }
} 