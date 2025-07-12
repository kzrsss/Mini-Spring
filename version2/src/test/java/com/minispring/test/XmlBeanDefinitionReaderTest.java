package com.minispring.test;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.DefaultResourceLoader;
import com.minispring.core.io.Resource;
import com.minispring.test.bean.LifecycleBean;
import com.minispring.test.bean.PrototypeBean;
import com.minispring.test.bean.TestBean;
import com.minispring.test.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean定义读取器测试类
 */
public class XmlBeanDefinitionReaderTest {
    
    private DefaultListableBeanFactory beanFactory;
    private XmlBeanDefinitionReader beanDefinitionReader;
    
    @BeforeEach
    void setUp() {
        // 创建Bean工厂
        beanFactory = new DefaultListableBeanFactory();
        // 创建Bean定义读取器
        beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
    }
    
    @Test
    void testLoadBeanDefinitions() {
        // 加载Bean定义
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:spring.xml");
        beanDefinitionReader.loadBeanDefinitions(resource);
        
        // 验证Bean定义数量
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
        
        // 验证Bean定义内容
        BeanDefinition testBeanDefinition = beanFactory.getBeanDefinition("testBean");
        assertEquals(TestBean.class, testBeanDefinition.getBeanClass());
        assertEquals(1, testBeanDefinition.getPropertyValues().getPropertyValues().length);
        
        BeanDefinition testServiceDefinition = beanFactory.getBeanDefinition("testService");
        assertEquals(TestService.class, testServiceDefinition.getBeanClass());
        assertEquals(2, testServiceDefinition.getPropertyValues().getPropertyValues().length);
        
        BeanDefinition lifecycleBeanDefinition = beanFactory.getBeanDefinition("lifecycleBean");
        assertEquals(LifecycleBean.class, lifecycleBeanDefinition.getBeanClass());
        assertEquals("init", lifecycleBeanDefinition.getInitMethodName());
        assertEquals("destroy", lifecycleBeanDefinition.getDestroyMethodName());
        
        BeanDefinition prototypeBeanDefinition = beanFactory.getBeanDefinition("prototypeBean");
        assertEquals(PrototypeBean.class, prototypeBeanDefinition.getBeanClass());
        assertTrue(prototypeBeanDefinition.isPrototype());
        assertFalse(prototypeBeanDefinition.isSingleton());
    }
    
    @Test
    void testLoadBeanDefinitionsFromLocation() {
        // 从位置加载Bean定义
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");
        
        // 验证Bean定义数量
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
    }
    
    @Test
    void testLoadBeanDefinitionsFromMultipleLocations() {
        // 从多个位置加载Bean定义
        beanDefinitionReader.loadBeanDefinitions(
                "classpath:spring.xml"
        );
        
        // 验证Bean定义数量
        assertEquals(6, beanFactory.getBeanDefinitionNames().length);
    }
} 