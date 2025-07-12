package com.minispring.beans.factory.config;

import com.minispring.aop.framework.AopProxy;
import com.minispring.aop.framework.ProxyFactory;
import com.minispring.aop.framework.TargetSource;
import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.ConfigurableBeanFactory;

/**
 * 作用域代理工厂
 * 用于创建作用域Bean的代理
 */
public class ScopedProxyFactory {
    
    /**
     * 创建作用域代理
     * @param targetBean 目标Bean
     * @param targetBeanName Bean名称
     * @param scopeName 作用域名称
     * @param beanFactory Bean工厂
     * @return 作用域Bean的代理
     */
    public static Object createScopedProxy(Object targetBean, String targetBeanName, String scopeName, ConfigurableBeanFactory beanFactory) {
        if (targetBean == null) {
            throw new IllegalArgumentException("Target bean must not be null");
        }
        
        // 创建代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();
        
        // 设置目标源为延迟目标源，使得每次调用都会从目标作用域获取最新的Bean
        proxyFactory.setTargetSource(new ScopedTargetSource(targetBeanName, scopeName, beanFactory));
        
        // 设置代理目标类，确保能够正确代理
        if (targetBean.getClass().getInterfaces().length > 0) {
            // 如果目标Bean实现了接口，使用JDK动态代理
            proxyFactory.setTargetClass(targetBean.getClass());
        } else {
            // 否则使用CGLIB代理
            proxyFactory.setTargetClass(targetBean.getClass());
        }
        
        // 获取代理
        return proxyFactory.getProxy();
    }
    
    /**
     * 延迟加载的作用域目标源
     */
    private static class ScopedTargetSource implements TargetSource {
        
        private final String targetBeanName;
        private final String scopeName;
        private final ConfigurableBeanFactory beanFactory;
        
        public ScopedTargetSource(String targetBeanName, String scopeName, ConfigurableBeanFactory beanFactory) {
            this.targetBeanName = targetBeanName;
            this.scopeName = scopeName;
            this.beanFactory = beanFactory;
        }
        
        @Override
        public Class<?> getTargetClass() {
            try {
                return beanFactory.getType(this.targetBeanName);
            } catch (BeansException ex) {
                // 如果无法获取类型，返回Object.class
                return Object.class;
            }
        }
        
        @Override
        public boolean isStatic() {
            // 返回false，表示目标对象不是静态的，每次调用都需要解析
            return false;
        }
        
        @Override
        public Object getTarget() throws Exception {
            // 从作用域中获取目标Bean
            Scope scope = beanFactory.getRegisteredScope(this.scopeName);
            if (scope == null) {
                throw new IllegalStateException("No Scope registered for scope name '" + this.scopeName + "'");
            }
            
            // 创建对象工厂
            ObjectFactory<Object> objectFactory = new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    return beanFactory.getBean(targetBeanName);
                }
            };
            
            // 使用ObjectFactory从作用域中获取或创建Bean
            return scope.get(this.targetBeanName, objectFactory);
        }
        
        @Override
        public void releaseTarget(Object target) throws Exception {
            // 不需要特别处理，作用域会自行管理对象的生命周期
        }
    }
} 