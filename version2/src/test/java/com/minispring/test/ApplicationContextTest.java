package com.minispring.test;

import com.minispring.context.ApplicationContext;
import com.minispring.context.support.ClassPathXmlApplicationContext;
import com.minispring.test.bean.TestBean;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApplicationContext测试类
 * 测试ApplicationContext的基本功能
 */
public class ApplicationContextTest {
    
    /**
     * 测试从类路径XML加载Bean
     */
    @Test
    public void testClassPathXmlApplicationContext() {
        // 创建ApplicationContext
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        
        // 获取Bean
        TestBean testBean = applicationContext.getBean("testBean", TestBean.class);
        
        // 验证Bean
        assertNotNull(testBean, "Bean不应该为null");
        assertEquals("测试Bean", testBean.getName(), "属性值应该正确");
        
        // 测试单例
        TestBean testBean2 = applicationContext.getBean("testBean", TestBean.class);
        assertSame(testBean, testBean2, "应该是同一个Bean实例");
        
        // 测试BeanFactory方法
        assertTrue(applicationContext.containsBean("testBean"), "应该包含testBean");
        assertFalse(applicationContext.containsBean("nonExistBean"), "不应该包含nonExistBean");
        
        // 测试Bean列表
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        assertNotNull(beanNames, "Bean名称数组不应为null");
        assertTrue(beanNames.length > 0, "Bean名称数组应包含元素");
        
        // 输出所有Bean名称
        System.out.println("所有Bean名称:");
        for (String name : beanNames) {
            System.out.println("- " + name);
        }
    }
}