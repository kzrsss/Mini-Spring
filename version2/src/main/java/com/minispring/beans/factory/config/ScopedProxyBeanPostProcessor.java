package com.minispring.beans.factory.config;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableBeanFactory;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;

/**
 * 作用域代理Bean后处理器
 * 用于为需要作用域代理的Bean创建代理
 */
public class ScopedProxyBeanPostProcessor implements BeanPostProcessor {
    
    // 所属的Bean工厂
    private final ConfigurableBeanFactory beanFactory;
    
    public ScopedProxyBeanPostProcessor(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 检查Bean定义是否存在
        BeanDefinition beanDefinition = null;
        try {
            // 如果是ConfigurableListableBeanFactory，获取Bean定义
            if (beanFactory instanceof ConfigurableListableBeanFactory) {
                ConfigurableListableBeanFactory listableBeanFactory = (ConfigurableListableBeanFactory) beanFactory;
                beanDefinition = listableBeanFactory.getBeanDefinition(beanName);
            }
        } catch (BeansException e) {
            // 找不到Bean定义，返回原始Bean
            return bean;
        }
        
        // 如果没有找到Bean定义或不需要作用域代理，返回原始Bean
        if (beanDefinition == null || !beanDefinition.isScopedProxy()) {
            return bean;
        }
        
        // 获取作用域名称
        String scopeName = beanDefinition.getScope();
        if (scopeName == null || scopeName.equals(ConfigurableBeanFactory.SCOPE_SINGLETON) 
                || scopeName.equals(ConfigurableBeanFactory.SCOPE_PROTOTYPE)) {
            // 单例和原型作用域不需要代理
            return bean;
        }
        
        // 创建作用域代理
        return ScopedProxyFactory.createScopedProxy(bean, beanName, scopeName, (ConfigurableBeanFactory)beanFactory);
    }
} 