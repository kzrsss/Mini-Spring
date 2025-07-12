package com.minispring.test.bean;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.BeanFactory;
import com.minispring.beans.factory.BeanFactoryAware;
import com.minispring.beans.factory.BeanNameAware;
import com.minispring.beans.factory.DisposableBean;
import com.minispring.beans.factory.InitializingBean;
import com.minispring.context.ApplicationContext;
import com.minispring.context.ApplicationContextAware;

/**
 * 测试Bean类
 * 实现各种Aware接口和生命周期接口
 */
public class TestBean implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {
    
    private String name;
    private String beanName;
    private BeanFactory beanFactory;
    private ApplicationContext applicationContext;
    
    /**
     * 默认构造函数
     */
    public TestBean() {
        System.out.println("TestBean构造方法执行");
    }
    
    /**
     * 构造函数
     * 
     * @param name 名称
     */
    public TestBean(String name) {
        this.name = name;
        System.out.println("TestBean构造方法执行，参数name: " + name);
    }
    
    /**
     * 获取名称
     * 
     * @return 名称
     */
    public String getName() {
        return name;
    }
    
    /**
     * 设置名称
     * 
     * @param name 名称
     */
    public void setName(String name) {
        System.out.println("TestBean设置属性name: " + name);
        this.name = name;
    }
    
    /**
     * 获取BeanName
     * 
     * @return BeanName
     */
    public String getBeanName() {
        return beanName;
    }
    
    /**
     * 设置BeanName（BeanNameAware接口）
     * 
     * @param name BeanName
     */
    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware接口调用，beanName: " + name);
        this.beanName = name;
    }
    
    /**
     * 获取BeanFactory
     * 
     * @return BeanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }
    
    /**
     * 设置BeanFactory（BeanFactoryAware接口）
     * 
     * @param beanFactory BeanFactory
     * @throws BeansException 如果设置过程中发生错误
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware接口调用，设置beanFactory");
        this.beanFactory = beanFactory;
    }
    
    /**
     * 获取ApplicationContext
     * 
     * @return ApplicationContext
     */
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     * 设置ApplicationContext（ApplicationContextAware接口）
     * 
     * @param applicationContext ApplicationContext
     * @throws BeansException 如果设置过程中发生错误
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware接口调用，设置applicationContext");
        this.applicationContext = applicationContext;
    }
    
    /**
     * 初始化方法（InitializingBean接口）
     * 
     * @throws Exception 如果初始化过程中发生错误
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean接口调用，执行afterPropertiesSet方法");
    }
    
    /**
     * 自定义初始化方法
     */
    public void init() {
        System.out.println("自定义初始化方法init执行");
    }
    
    /**
     * 销毁方法（DisposableBean接口）
     * 
     * @throws Exception 如果销毁过程中发生错误
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean接口调用，执行destroy方法");
    }
    
    /**
     * 自定义销毁方法
     */
    public void customDestroy() {
        System.out.println("自定义销毁方法customDestroy执行");
    }
    
    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", beanName='" + beanName + '\'' +
                '}';
    }
} 