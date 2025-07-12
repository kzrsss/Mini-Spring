package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.ObjectFactory;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象Bean工厂
 * 实现BeanFactory接口，继承DefaultSingletonBeanRegistry
 * 提供获取Bean的模板方法
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /** BeanPostProcessor列表 */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) doGetBean(name, requiredType, null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBean(requiredType.getName(), requiredType);
    }
    
    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, null, args);
    }

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name) || containsBeanDefinition(name);
    }
    
    /**
     * 添加BeanPostProcessor
     * @param beanPostProcessor BeanPostProcessor
     */
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // 避免重复添加
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }
    
    /**
     * 获取BeanPostProcessor列表
     * @return BeanPostProcessor列表
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
    
    /**
     * 执行BeanPostProcessor的前置处理
     * @param existingBean 现有的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean实例
     */
    public abstract Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    
    /**
     * 执行BeanPostProcessor的后置处理
     * @param existingBean 现有的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean实例
     */
    public abstract Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 获取Bean的实际实现
     * @param name Bean名称
     * @param requiredType Bean类型
     * @param args 构造参数
     * @param <T> Bean类型
     * @return Bean实例
     * @throws BeansException 如果获取Bean失败
     */
    protected <T> T doGetBean(String name, Class<T> requiredType, Object[] args) throws BeansException {
        // 先从单例Bean缓存中获取
        Object bean = getSingleton(name);
        if (bean != null) {
            System.out.println("从缓存中获取到Bean: " + name);
            return (T) bean;
        }

        // 如果没有，则创建Bean实例
        BeanDefinition beanDefinition = getBeanDefinition(name);
        
        if (beanDefinition.isSingleton()) {
            // 对于单例Bean，使用getSingleton方法创建并缓存
            bean = getSingleton(name, new ObjectFactory<Object>() {
                @Override
                public Object getObject() throws BeansException {
                    try {
                        return createBean(name, beanDefinition, args);
                    } catch (Exception e) {
                        throw new BeansException("创建Bean[" + name + "]失败", e);
                    }
                }
            });
            System.out.println("创建并缓存单例Bean: " + name);
        } else {
            // 对于原型Bean，直接创建新实例
            bean = createBean(name, beanDefinition, args);
            System.out.println("创建原型Bean: " + name);
        }
        
        return (T) bean;
    }

    /**
     * 判断是否包含指定名称的BeanDefinition
     * @param beanName Bean名称
     * @return 是否包含
     */
    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * 获取BeanDefinition
     * @param beanName Bean名称
     * @return Bean定义
     * @throws BeansException 如果找不到BeanDefinition
     */
    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    /**
     * 创建Bean实例
     * @param beanName Bean名称
     * @param beanDefinition Bean定义
     * @param args 构造参数
     * @return Bean实例
     * @throws BeansException 如果创建Bean失败
     */
    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
} 