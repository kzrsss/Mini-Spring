package com.minispring.test;

import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.test.bean.LifecycleBean;
import com.minispring.test.bean.PrototypeBean;
import com.minispring.test.bean.TestBean;
import com.minispring.test.service.TestService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean工厂测试类
 * 测试从XML加载Bean定义并实例化Bean
 */
public class XmlBeanFactoryTest {
    
    @Test
    void testXmlBeanFactory() {
        // 创建Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 创建Bean定义读取器
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        
        // 加载Bean定义
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        
        // 获取并验证TestBean
        TestBean testBean = beanFactory.getBean("testBean", TestBean.class);
        assertNotNull(testBean);
        assertEquals("测试Bean", testBean.getName());
        
        // 获取并验证TestService
        TestService testService = beanFactory.getBean("testService", TestService.class);
        assertNotNull(testService);
        assertEquals("Hello, MiniSpring!", testService.getMessage());
        assertSame(testBean, testService.getTestBean());
        
        // 获取并验证LifecycleBean
        LifecycleBean lifecycleBean = beanFactory.getBean("lifecycleBean", LifecycleBean.class);
        assertNotNull(lifecycleBean);
        assertEquals("Lifecycle", lifecycleBean.getName());
        assertTrue(lifecycleBean.isInitialized());
        assertFalse(lifecycleBean.isDestroyed());
        
        // 测试原型Bean
        PrototypeBean prototypeBean1 = beanFactory.getBean("prototypeBean", PrototypeBean.class);
        PrototypeBean prototypeBean2 = beanFactory.getBean("prototypeBean", PrototypeBean.class);
        assertNotNull(prototypeBean1);
        assertNotNull(prototypeBean2);
        assertEquals("Prototype", prototypeBean1.getName());
        assertEquals("Prototype", prototypeBean2.getName());
        assertNotSame(prototypeBean1, prototypeBean2);
    }
} 