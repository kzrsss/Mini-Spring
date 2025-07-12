package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;
import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationContextAware;

/**
 * ApplicationContextAware处理器
 * 处理实现了ApplicationContextAware接口的Bean
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {
    
    private final ApplicationContext applicationContext;
    
    /**
     * 构造函数
     * 
     * @param applicationContext 应用上下文
     */
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    /**
     * 在Bean初始化前处理
     * 
     * @param bean Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean
     * @throws BeansException 如果处理过程中发生错误
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }
    
    /**
     * 在Bean初始化后处理
     * 
     * @param bean Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean
     * @throws BeansException 如果处理过程中发生错误
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
} 