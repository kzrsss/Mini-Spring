package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.test.bean.TestUserController;
import com.minispring.test.bean.TestUserDao;
import com.minispring.test.bean.TestUserService;
import com.minispring.test.bean.TestUserServiceWithConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 依赖注入测试类
 */
public class DependencyInjectionTest {

    /**
     * 测试属性注入
     */
    @Test
    public void testPropertyInjection() {
        // 创建BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // 创建UserService的属性值
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "张三"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        
        // 注册UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserService.class, propertyValues);
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);
        
        // 获取UserService
        TestUserService userService = (TestUserService) beanFactory.getBean("userService");
        
        // 验证属性注入
        assertEquals("张三", userService.getName());
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * 测试构造函数注入
     */
    @Test
    public void testConstructorInjection() {
        // 创建BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // 注册UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserServiceWithConstructor.class);
        beanFactory.registerBeanDefinition("userServiceWithConstructor", userServiceBeanDefinition);
        
        // 获取UserService，通过构造函数注入UserDao
        System.out.println("开始获取userServiceWithConstructor");
        TestUserServiceWithConstructor userService = (TestUserServiceWithConstructor) beanFactory.getBean("userServiceWithConstructor", new Object[]{beanFactory.getBean("userDao")});
        System.out.println("获取到userServiceWithConstructor: " + userService);
        System.out.println("userDao: " + userService.getUserDao());
        
        // 验证构造函数注入
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * 测试自动装配
     */
    @Test
    public void testAutowiring() {
        // 创建BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // 注册UserService
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserServiceWithConstructor.class);
        beanFactory.registerBeanDefinition("userServiceWithConstructor", userServiceBeanDefinition);
        
        // 获取UserService，通过自动装配注入UserDao
        System.out.println("开始获取userServiceWithConstructor（自动装配）");
        TestUserServiceWithConstructor userService = (TestUserServiceWithConstructor) beanFactory.getBean("userServiceWithConstructor");
        System.out.println("获取到userServiceWithConstructor: " + userService);
        System.out.println("userDao: " + userService.getUserDao());
        
        // 验证自动装配
        assertNotNull(userService.getUserDao());
        assertEquals("UserDao", userService.getUserDao().toString());
    }
    
    /**
     * 测试嵌套依赖
     */
    @Test
    public void testNestedDependency() {
        // 创建BeanFactory
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册UserDao
        BeanDefinition userDaoBeanDefinition = new BeanDefinition(TestUserDao.class);
        beanFactory.registerBeanDefinition("userDao", userDaoBeanDefinition);
        
        // 注册UserService
        PropertyValues userServicePropertyValues = new PropertyValues();
        userServicePropertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
        BeanDefinition userServiceBeanDefinition = new BeanDefinition(TestUserService.class, userServicePropertyValues);
        beanFactory.registerBeanDefinition("userService", userServiceBeanDefinition);
        
        // 注册UserController
        PropertyValues userControllerPropertyValues = new PropertyValues();
        userControllerPropertyValues.addPropertyValue(new PropertyValue("userService", new BeanReference("userService")));
        BeanDefinition userControllerBeanDefinition = new BeanDefinition(TestUserController.class, userControllerPropertyValues);
        beanFactory.registerBeanDefinition("userController", userControllerBeanDefinition);
        
        // 获取UserController
        TestUserController userController = (TestUserController) beanFactory.getBean("userController");
        
        // 验证嵌套依赖
        assertNotNull(userController.getUserService());
        assertNotNull(userController.getUserService().getUserDao());
        assertEquals("UserDao", userController.getUserService().getUserDao().toString());
    }
} 