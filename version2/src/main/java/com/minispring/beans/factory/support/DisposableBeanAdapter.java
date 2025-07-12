package com.minispring.beans.factory.support;

import com.minispring.beans.BeansException;
import com.minispring.beans.factory.DisposableBean;

import java.lang.reflect.Method;

/**
 * DisposableBean适配器
 * 用于统一处理实现了DisposableBean接口的Bean和配置了destroy-method的Bean
 */
public class DisposableBeanAdapter implements DisposableBean {
    
    private final Object bean;
    private final String beanName;
    private final String destroyMethodName;
    
    /**
     * 构造函数
     * 
     * @param bean 目标Bean
     * @param beanName Bean名称
     * @param destroyMethodName 销毁方法名称
     */
    public DisposableBeanAdapter(Object bean, String beanName, String destroyMethodName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = destroyMethodName;
    }
    
    /**
     * 执行Bean的销毁方法
     * 1. 如果Bean实现了DisposableBean接口，则调用其destroy方法
     * 2. 如果Bean配置了destroy-method，则通过反射调用该方法
     */
    @Override
    public void destroy() throws Exception {
        // 1. 如果Bean实现了DisposableBean接口，则调用其destroy方法
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
            System.out.println("执行Bean[" + beanName + "]的DisposableBean接口的destroy方法");
        }
        
        // 2. 如果Bean配置了destroy-method且不是DisposableBean接口中的方法，则通过反射调用该方法
        if (destroyMethodName != null && !(bean instanceof DisposableBean && "destroy".equals(destroyMethodName))) {
            try {
                Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
                destroyMethod.invoke(bean);
                System.out.println("执行Bean[" + beanName + "]的自定义销毁方法：" + destroyMethodName);
            } catch (NoSuchMethodException e) {
                throw new BeansException("找不到Bean[" + beanName + "]的销毁方法：" + destroyMethodName, e);
            } catch (Exception e) {
                throw new BeansException("执行Bean[" + beanName + "]的销毁方法[" + destroyMethodName + "]失败", e);
            }
        }
    }
} 