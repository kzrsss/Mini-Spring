package com.minispring.aop.framework;

import com.minispring.aop.Advisor;
import com.minispring.aop.PointcutAdvisor;
import com.minispring.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AOP代理配置支持类
 * 持有目标对象、通知和Advisor的配置
 */
public class AdvisedSupport {
    
    // 配置冻结标记，如果为true，则不允许修改配置
    private boolean frozen = false;
    
    // 目标对象源
    private TargetSource targetSource;
    
    // 目标类，当直接设置时使用
    private Class<?> targetClass;
    
    // 通知器列表
    private List<Advisor> advisors = new ArrayList<>();
    
    // 方法缓存，避免重复计算方法拦截器
    private transient Map<Method, List<Object>> methodCache = new ConcurrentHashMap<>(32);
    
    /**
     * 设置目标源
     * @param targetSource 目标源
     */
    public void setTargetSource(TargetSource targetSource) {
        this.targetSource = targetSource;
    }
    
    /**
     * 获取目标源
     * @return 目标源
     */
    public TargetSource getTargetSource() {
        return this.targetSource;
    }
    
    /**
     * 设置目标类
     * 当不使用TargetSource时，可以直接设置目标类
     * @param targetClass 目标类
     */
    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
    
    /**
     * 设置冻结标记
     * @param frozen 如果为true，则不允许修改配置
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
    
    /**
     * 检查配置是否已被冻结
     * @return 如果配置已被冻结返回true
     */
    public boolean isFrozen() {
        return this.frozen;
    }
    
    /**
     * 添加通知器
     * @param advisor 通知器
     */
    public void addAdvisor(Advisor advisor) {
        if (isFrozen()) {
            throw new RuntimeException("Cannot add advisor: Configuration is frozen");
        }
        this.advisors.add(advisor);
        // 添加新的通知器后，清除方法缓存
        this.methodCache.clear();
    }
    
    /**
     * 获取通知器列表
     * @return 通知器列表
     */
    public List<Advisor> getAdvisors() {
        return this.advisors;
    }
    
    /**
     * 获取目标类
     * @return 目标类
     */
    public Class<?> getTargetClass() {
        if (this.targetClass != null) {
            return this.targetClass;
        }
        return this.targetSource != null ? this.targetSource.getTargetClass() : null;
    }
    
    /**
     * 判断指定方法是否有通知
     * @param method 要检查的方法
     * @param targetClass 目标类
     * @return 如果方法有通知则返回true
     */
    public boolean hasAdvice(Method method, Class<?> targetClass) {
        return !getInterceptorsAndDynamicInterceptionAdvice(method, targetClass).isEmpty();
    }
    
    /**
     * 获取方法的拦截器和动态拦截通知
     * @param method 要处理的方法
     * @param targetClass 目标类
     * @return 拦截器和通知列表
     */
    public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
        // 检查缓存中是否已有计算过的结果
        List<Object> cached = this.methodCache.get(method);
        if (cached != null) {
            return cached;
        }
        
        System.out.println("AdvisedSupport: 计算方法 " + method.getName() + " 的拦截器链");
        System.out.println("AdvisedSupport: 目标类 " + targetClass.getName());
        System.out.println("AdvisedSupport: 接口: " + (targetClass.getInterfaces().length > 0 ? targetClass.getInterfaces()[0].getName() : "无"));
        System.out.println("AdvisedSupport: 通知器数量 " + this.advisors.size());
        
        // 计算适用于此方法的通知
        List<Object> interceptors = new ArrayList<>();
        for (Advisor advisor : this.advisors) {
            System.out.println("AdvisedSupport: 检查通知器 " + advisor.getClass().getName());
            
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                System.out.println("AdvisedSupport: 这是一个切点通知器: " + pointcutAdvisor.getClass().getName());
                
                // 检查切点是否匹配类
                boolean classMatches = pointcutAdvisor.getPointcut().getClassFilter().matches(targetClass);
                System.out.println("AdvisedSupport: 类匹配结果: " + classMatches);
                
                // 对于实现了接口的类，也检查是否匹配接口
                if (!classMatches && targetClass.getInterfaces().length > 0) {
                    for (Class<?> iface : targetClass.getInterfaces()) {
                        if (pointcutAdvisor.getPointcut().getClassFilter().matches(iface)) {
                            classMatches = true;
                            System.out.println("AdvisedSupport: 接口匹配结果: " + classMatches + " for interface " + iface.getName());
                            break;
                        }
                    }
                }
                
                // 检查切点是否匹配方法
                boolean methodMatches = false;
                if (classMatches) {
                    methodMatches = pointcutAdvisor.getPointcut().getMethodMatcher().matches(method, targetClass);
                    System.out.println("AdvisedSupport: 方法匹配结果: " + methodMatches);
                    
                    // 如果方法不匹配，尝试在接口上查找对应的方法并匹配
                    if (!methodMatches && targetClass.getInterfaces().length > 0) {
                        for (Class<?> iface : targetClass.getInterfaces()) {
                            try {
                                Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                                methodMatches = pointcutAdvisor.getPointcut().getMethodMatcher().matches(ifaceMethod, iface);
                                System.out.println("AdvisedSupport: 接口方法匹配结果: " + methodMatches + " for interface " + iface.getName());
                                if (methodMatches) {
                                    break;
                                }
                            } catch (NoSuchMethodException ex) {
                                // 接口上没有这个方法，继续下一个接口
                                System.out.println("AdvisedSupport: 接口 " + iface.getName() + " 上没有方法 " + method.getName());
                            }
                        }
                    }
                }
                
                // 如果匹配，添加通知
                if ((classMatches && methodMatches)) {
                    System.out.println("AdvisedSupport: 匹配成功，添加通知: " + advisor.getAdvice().getClass().getName());
                    interceptors.add(advisor.getAdvice());
                }
            } else {
                // 如果不是PointcutAdvisor，直接添加通知
                System.out.println("AdvisedSupport: 非切点通知器，直接添加通知");
                interceptors.add(advisor.getAdvice());
            }
        }
        
        System.out.println("AdvisedSupport: 最终得到的拦截器数量: " + interceptors.size());
        
        // 缓存结果
        this.methodCache.put(method, interceptors);
        return interceptors;
    }
} 