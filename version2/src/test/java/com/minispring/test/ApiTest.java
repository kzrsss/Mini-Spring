package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.UserDao;
import com.minispring.test.bean.UserDaoImpl;
import com.minispring.test.bean.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 * API测试类
 * 用于测试IoC容器的基本功能
 */
public class ApiTest {

    @Test
    public void testBeanFactory() {
        // 1.创建Bean工厂
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 2.注册UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(UserDaoImpl.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);

        // 3.注册UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(UserService.class);
        // 设置初始化方法
        userServiceBeanDefinition.setInitMethodName("init");
        
        // 设置属性
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "张三"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", beanFactory.getBean("userDao")));
        userServiceBeanDefinition.setPropertyValues(propertyValues);
        
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);

        // 4.获取Bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        
        // 5.使用Bean
        String result = userService.queryUserInfo();
        System.out.println("测试结果：" + result);
        
        // 6.验证结果
        Assertions.assertEquals("北京市", result);
    }
    
    @Test
    public void testPropertyValues() {
        // 测试PropertyValues的功能
        PropertyValues propertyValues = new PropertyValues();
        
        // 测试添加属性
        propertyValues.addPropertyValue(new PropertyValue("name", "张三"));
        propertyValues.addPropertyValue(new PropertyValue("age", 18));
        
        // 测试获取属性
        Assertions.assertTrue(propertyValues.contains("name"));
        Assertions.assertTrue(propertyValues.contains("age"));
        Assertions.assertFalse(propertyValues.contains("address"));
        
        // 测试属性数量
        Assertions.assertEquals(2, propertyValues.size());
        
        // 测试获取属性值
        Assertions.assertEquals("张三", propertyValues.getPropertyValue("name").orElseThrow().getValue());
        Assertions.assertEquals(18, propertyValues.getPropertyValue("age").orElseThrow().getValue());
        
        // 测试替换属性
        propertyValues.addPropertyValue(new PropertyValue("name", "李四"));
        Assertions.assertEquals("李四", propertyValues.getPropertyValue("name").orElseThrow().getValue());
        Assertions.assertEquals(2, propertyValues.size());
        
        // 测试转换值
        PropertyValue nameProperty = propertyValues.getPropertyValue("name").orElseThrow();
        nameProperty.setConvertedValue("王五");
        Assertions.assertEquals("王五", nameProperty.getConvertedValue());
        Assertions.assertEquals("李四", nameProperty.getValue());
    }
} 