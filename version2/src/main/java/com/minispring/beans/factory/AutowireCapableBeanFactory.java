package com.minispring.beans.factory;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.config.BeanPostProcessor;

/**
 * 自动装配的BeanFactory接口
 * 提供自动装配Bean的方法
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    
    /**
     * 自动装配：按类型
     */
    int AUTOWIRE_BY_TYPE = 1;
    
    /**
     * 自动装配：按名称
     */
    int AUTOWIRE_BY_NAME = 2;
    
    /**
     * 自动装配：构造函数
     */
    int AUTOWIRE_CONSTRUCTOR = 3;
    
    /**
     * 不自动装配
     */
    int AUTOWIRE_NO = 0;
    
    /**
     * 创建一个新的Bean实例
     * 
     * @param beanClass Bean类
     * @return Bean实例
     * @throws BeansException 如果创建过程中发生错误
     */
    Object createBean(Class<?> beanClass) throws BeansException;
    
    /**
     * 自动装配一个已存在的Bean实例
     * 
     * @param existingBean 已存在的Bean实例
     * @param beanName Bean名称
     * @throws BeansException 如果装配过程中发生错误
     */
    void autowireBean(Object existingBean, String beanName) throws BeansException;
    
    /**
     * 应用BeanPostProcessor的前置处理
     * 
     * @param existingBean 已存在的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean
     * @throws BeansException 如果处理过程中发生错误
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;
    
    /**
     * 应用BeanPostProcessor的后置处理
     * 
     * @param existingBean 已存在的Bean实例
     * @param beanName Bean名称
     * @return 处理后的Bean
     * @throws BeansException 如果处理过程中发生错误
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}