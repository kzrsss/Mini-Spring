package com.minispring.test;

import com.minispring.beans.PropertyValue;
import com.minispring.beans.PropertyValues;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanReference;
import com.minispring.beans.factory.support.DefaultListableBeanFactory;
import com.minispring.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 循环依赖测试类
 * 测试三级缓存解决循环依赖的能力
 */
public class CircularDependencyTest {

    /**
     * 测试互相依赖的情况
     * A依赖B，B依赖A
     */
    @Test
    public void testCircularDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册A的Bean定义
        BeanDefinition beanDefinitionA = new BeanDefinition(TestServiceA.class);
        PropertyValues propertyValuesA = new PropertyValues();
        propertyValuesA.addPropertyValue(new PropertyValue("serviceB", new BeanReference("serviceB")));
        beanDefinitionA.setPropertyValues(propertyValuesA);
        beanFactory.registerBeanDefinition("serviceA", beanDefinitionA);
        
        // 注册B的Bean定义
        BeanDefinition beanDefinitionB = new BeanDefinition(TestServiceB.class);
        PropertyValues propertyValuesB = new PropertyValues();
        propertyValuesB.addPropertyValue(new PropertyValue("serviceA", new BeanReference("serviceA")));
        beanDefinitionB.setPropertyValues(propertyValuesB);
        beanFactory.registerBeanDefinition("serviceB", beanDefinitionB);
        
        // 获取A，触发循环依赖解析
        TestServiceA serviceA = (TestServiceA) beanFactory.getBean("serviceA");
        TestServiceB serviceB = (TestServiceB) beanFactory.getBean("serviceB");
        
        // 验证循环依赖是否被成功解决
        assertNotNull(serviceA);
        assertNotNull(serviceB);
        assertNotNull(serviceA.getServiceB());
        assertNotNull(serviceB.getServiceA());
        assertSame(serviceA, serviceB.getServiceA());
        assertSame(serviceB, serviceA.getServiceB());
        
        System.out.println("循环依赖测试通过：");
        System.out.println("ServiceA: " + serviceA + ", ServiceB: " + serviceB);
        System.out.println("ServiceA.serviceB: " + serviceA.getServiceB());
        System.out.println("ServiceB.serviceA: " + serviceB.getServiceA());
    }

    /**
     * 测试服务A
     */
    public static class TestServiceA {
        private TestServiceB serviceB;
        
        public TestServiceA() {
            System.out.println("创建TestServiceA实例");
        }
        
        public void setServiceB(TestServiceB serviceB) {
            System.out.println("设置TestServiceA.serviceB = " + serviceB);
            this.serviceB = serviceB;
        }
        
        public TestServiceB getServiceB() {
            return serviceB;
        }
        
        @Override
        public String toString() {
            return "TestServiceA@" + Integer.toHexString(hashCode());
        }
    }
    
    /**
     * 测试服务B
     */
    public static class TestServiceB {
        private TestServiceA serviceA;
        
        public TestServiceB() {
            System.out.println("创建TestServiceB实例");
        }
        
        public void setServiceA(TestServiceA serviceA) {
            System.out.println("设置TestServiceB.serviceA = " + serviceA);
            this.serviceA = serviceA;
        }
        
        public TestServiceA getServiceA() {
            return serviceA;
        }
        
        @Override
        public String toString() {
            return "TestServiceB@" + Integer.toHexString(hashCode());
        }
    }
    
    /**
     * 测试三层循环依赖
     * A依赖B，B依赖C，C依赖A
     */
    @Test
    public void testThreeLevelCircularDependency() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        
        // 注册A的Bean定义
        BeanDefinition beanDefinitionA = new BeanDefinition(ServiceA.class);
        PropertyValues propertyValuesA = new PropertyValues();
        propertyValuesA.addPropertyValue(new PropertyValue("serviceB", new BeanReference("serviceB")));
        beanDefinitionA.setPropertyValues(propertyValuesA);
        beanFactory.registerBeanDefinition("serviceA", beanDefinitionA);
        
        // 注册B的Bean定义
        BeanDefinition beanDefinitionB = new BeanDefinition(ServiceB.class);
        PropertyValues propertyValuesB = new PropertyValues();
        propertyValuesB.addPropertyValue(new PropertyValue("serviceC", new BeanReference("serviceC")));
        beanDefinitionB.setPropertyValues(propertyValuesB);
        beanFactory.registerBeanDefinition("serviceB", beanDefinitionB);
        
        // 注册C的Bean定义
        BeanDefinition beanDefinitionC = new BeanDefinition(ServiceC.class);
        PropertyValues propertyValuesC = new PropertyValues();
        propertyValuesC.addPropertyValue(new PropertyValue("serviceA", new BeanReference("serviceA")));
        beanDefinitionC.setPropertyValues(propertyValuesC);
        beanFactory.registerBeanDefinition("serviceC", beanDefinitionC);
        
        // 获取A，触发循环依赖解析
        ServiceA serviceA = (ServiceA) beanFactory.getBean("serviceA");
        ServiceB serviceB = (ServiceB) beanFactory.getBean("serviceB");
        ServiceC serviceC = (ServiceC) beanFactory.getBean("serviceC");
        
        // 验证循环依赖是否被成功解决
        assertNotNull(serviceA);
        assertNotNull(serviceB);
        assertNotNull(serviceC);
        assertNotNull(serviceA.getServiceB());
        assertNotNull(serviceB.getServiceC());
        assertNotNull(serviceC.getServiceA());
        
        // 验证引用关系
        assertSame(serviceA, serviceC.getServiceA());
        assertSame(serviceB, serviceA.getServiceB());
        assertSame(serviceC, serviceB.getServiceC());
        
        System.out.println("三级循环依赖测试通过");
    }
    
    /**
     * 服务A
     */
    public static class ServiceA {
        private ServiceB serviceB;
        
        public ServiceB getServiceB() {
            return serviceB;
        }
        
        public void setServiceB(ServiceB serviceB) {
            this.serviceB = serviceB;
        }
    }
    
    /**
     * 服务B
     */
    public static class ServiceB {
        private ServiceC serviceC;
        
        public ServiceC getServiceC() {
            return serviceC;
        }
        
        public void setServiceC(ServiceC serviceC) {
            this.serviceC = serviceC;
        }
    }
    
    /**
     * 服务C
     */
    public static class ServiceC {
        private ServiceA serviceA;
        
        public ServiceA getServiceA() {
            return serviceA;
        }
        
        public void setServiceA(ServiceA serviceA) {
            this.serviceA = serviceA;
        }
    }
} 