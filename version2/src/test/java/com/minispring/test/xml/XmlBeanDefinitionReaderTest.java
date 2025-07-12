package com.minispring.test.xml;

import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.core.io.ClassPathResource;
import com.minispring.core.io.Resource;
import com.minispring.test.bean.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * XML Bean定义读取器测试类
 */
public class XmlBeanDefinitionReaderTest {
    
    @Test
    void testLoadBeanDefinitions() {
        // 创建Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 创建XML Bean定义读取器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        
        // 加载XML配置文件
        Resource resource = new ClassPathResource("bean-definitions.xml");
        reader.loadBeanDefinitions(resource);
        
        // 验证Bean定义是否正确加载
        assertTrue(beanFactory.containsBeanDefinition("person"));
        
        // 获取Bean定义
        BeanDefinition personDefinition = beanFactory.getBeanDefinition("person");
        assertEquals(Person.class, personDefinition.getBeanClass());
        
        // 获取Bean实例
        Person person = beanFactory.getBean("person", Person.class);
        assertNotNull(person);
        assertEquals("张三", person.getName());
        assertEquals(30, person.getAge());
        
        // 验证引用注入
        assertTrue(beanFactory.containsBeanDefinition("address"));
        assertNotNull(person.getAddress());
        assertEquals("北京市", person.getAddress().getCity());
        assertEquals("朝阳区", person.getAddress().getDistrict());
    }
} 