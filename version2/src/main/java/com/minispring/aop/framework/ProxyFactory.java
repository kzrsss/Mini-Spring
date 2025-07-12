package com.minispring.aop.framework;

import com.minispring.aop.Advisor;
import com.minispring.aop.PointcutAdvisor;

/**
 * AOP代理工厂
 * 创建代理对象，自动选择使用JDK动态代理或CGLIB代理
 */
public class ProxyFactory extends AdvisedSupport {
    
    /**
     * 创建一个新的ProxyFactory
     */
    public ProxyFactory() {
    }
    
    /**
     * 使用给定的目标对象创建一个新的ProxyFactory
     * @param target 目标对象
     */
    public ProxyFactory(Object target) {
        setTargetSource(new SingletonTargetSource(target));
    }
    
    /**
     * 获取代理对象
     * @return 代理对象
     */
    public Object getProxy() {
        return createAopProxy().getProxy();
    }
    
    /**
     * 使用给定的类加载器获取代理对象
     * @param classLoader 类加载器
     * @return 代理对象
     */
    public Object getProxy(ClassLoader classLoader) {
        return createAopProxy().getProxy(classLoader);
    }
    
    /**
     * 创建AOP代理
     * 根据目标类是否实现接口选择使用JDK动态代理或CGLIB代理
     * @return AOP代理
     */
    protected AopProxy createAopProxy() {
        // 如果目标类实现了接口，使用JDK动态代理
        if (getTargetClass().getInterfaces().length > 0) {
            return new JdkDynamicAopProxy(this);
        }
        // 否则使用CGLIB代理
        return new CglibAopProxy(this);
    }
    
    /**
     * 添加切点通知器
     * @param advisor 通知器
     */
    public void addAdvisor(Advisor advisor) {
        super.addAdvisor(advisor);
    }
    
    /**
     * 检查目标类是否可代理
     * @param targetClass 目标类
     * @return 如果目标类可代理返回true
     */
    private boolean isProxyTargetClass(Class<?> targetClass) {
        if (targetClass == null) {
            return false;
        }
        
        // 检查是否有匹配目标类的通知器
        for (Advisor advisor : getAdvisors()) {
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if (pointcutAdvisor.getPointcut().getClassFilter().matches(targetClass)) {
                    return true;
                }
            }
        }
        
        return false;
    }
} 