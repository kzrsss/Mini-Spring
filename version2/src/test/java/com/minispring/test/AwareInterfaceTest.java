package com.minispring.test;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.AwareBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Aware接口族测试类
 */
public class AwareInterfaceTest {
    
    @Test
    void testBeanNameAwareAndBeanFactoryAware() {
        // 创建Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册Bean定义
        BeanDefinition beanDefinition = new BeanDefinition(AwareBean.class);
        beanFactory.registerBeanDefinition("awareBean", beanDefinition);
        
        // 获取Bean
        AwareBean awareBean = beanFactory.getBean("awareBean", AwareBean.class);
        
        // 验证BeanNameAware接口
        assertEquals("awareBean", awareBean.getBeanName());
        
        // 验证BeanFactoryAware接口
        assertSame(beanFactory, awareBean.getBeanFactory());
        
        System.out.println("BeanNameAware接口测试通过：" + awareBean.getBeanName());
        System.out.println("BeanFactoryAware接口测试通过：" + awareBean.getBeanFactory());
    }
} 