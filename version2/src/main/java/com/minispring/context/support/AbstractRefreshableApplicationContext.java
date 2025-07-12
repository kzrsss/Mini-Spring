package com.minispring.context.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;

/**
 * 可刷新的ApplicationContext抽象实现
 * 支持重复刷新，每次刷新都会创建新的内部BeanFactory
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
    
    /**
     * 内部的Bean工厂
     */
    private DefaultListableBeanFactory beanFactory;
    
    /**
     * 获取新的BeanFactory
     * 每次刷新都会创建新的BeanFactory
     * 
     * @return 新的BeanFactory
     */
    @Override
    protected final ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }
    
    /**
     * 刷新Bean工厂
     * 创建新的Bean工厂并加载Bean定义
     * 
     * @throws BeansException 如果创建或加载过程中发生错误
     */
    protected void refreshBeanFactory() throws BeansException {
        // 如果已存在BeanFactory，则销毁所有单例Bean并关闭工厂
        if (this.beanFactory != null) {
            this.beanFactory.destroySingletons();
            this.beanFactory = null;
        }
        
        // 创建新的BeanFactory
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        
        // 加载Bean定义
        loadBeanDefinitions(beanFactory);
        
        this.beanFactory = beanFactory;
    }
    
    /**
     * 创建Bean工厂
     * 
     * @return 新的DefaultListableBeanFactory
     */
    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }
    
    /**
     * 加载Bean定义
     * 由子类实现，可以从不同来源加载Bean定义
     * 
     * @param beanFactory Bean工厂
     * @throws BeansException 如果加载过程中发生错误
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;
    
    /**
     * 获取Bean工厂
     * 
     * @return Bean工厂
     * @throws IllegalStateException 如果工厂尚未创建
     */
    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        if (this.beanFactory == null) {
            throw new IllegalStateException("BeanFactory尚未初始化或已关闭");
        }
        return this.beanFactory;
    }
}