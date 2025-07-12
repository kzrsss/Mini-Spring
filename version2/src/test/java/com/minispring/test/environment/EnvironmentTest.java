package com.minispring.test.environment;

import com.minispring.core.env.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Environment测试类
 */
public class EnvironmentTest {
    
    /**
     * 测试StandardEnvironment的基本功能
     */
    @Test
    public void testStandardEnvironment() {
        StandardEnvironment environment = new StandardEnvironment();
        
        // 测试系统属性
        System.setProperty("test.property", "test-value");
        assertEquals("test-value", environment.getProperty("test.property"));
        
        // 测试不同类型的属性获取
        System.setProperty("test.int", "123");
        System.setProperty("test.boolean", "true");
        
        assertEquals(123, environment.getProperty("test.int", Integer.class).intValue());
        assertTrue(environment.getProperty("test.boolean", Boolean.class));
        
        // 测试默认值
        assertEquals("default-value", environment.getProperty("non.existent.property", "default-value"));
        assertEquals(456, environment.getProperty("non.existent.int", Integer.class, 456).intValue());
        
        // 清理系统属性
        System.clearProperty("test.property");
        System.clearProperty("test.int");
        System.clearProperty("test.boolean");
    }
    
    /**
     * 测试Profiles功能
     */
    @Test
    public void testProfiles() {
        AbstractEnvironment environment = new StandardEnvironment();
        
        // 默认情况下只有default profile激活
        assertTrue(environment.acceptsProfiles("default"));
        assertFalse(environment.acceptsProfiles("test"));
        
        // 设置激活的profiles
        environment.setActiveProfiles("test", "dev");
        
        assertTrue(environment.acceptsProfiles("test"));
        assertTrue(environment.acceptsProfiles("dev"));
        assertFalse(environment.acceptsProfiles("prod"));
        
        // 测试多个profiles
        assertTrue(environment.acceptsProfiles("prod", "test"));  // 只要有一个匹配就返回true
        
        // 添加profile
        environment.addActiveProfile("prod");
        assertTrue(environment.acceptsProfiles("prod"));
    }
    
    /**
     * 测试PropertySource优先级
     */
    @Test
    public void testPropertySourcePrecedence() {
        MutablePropertySources propertySources = new MutablePropertySources();
        
        // 添加两个属性源，第一个优先级高于第二个
        MapPropertySource firstSource = new MapPropertySource("first", java.util.Collections.singletonMap("test.key", "first-value"));
        MapPropertySource secondSource = new MapPropertySource("second", java.util.Collections.singletonMap("test.key", "second-value"));
        
        propertySources.addFirst(secondSource);
        propertySources.addFirst(firstSource);
        
        // 创建自定义环境实现测试PropertySource优先级
        AbstractEnvironment environment = new AbstractEnvironment() {
            @Override
            protected void customizePropertySources(MutablePropertySources propertySources) {
                propertySources.addFirst(firstSource);
                propertySources.addLast(secondSource);
            }
        };
        
        // 应该返回第一个属性源的值
        assertEquals("first-value", environment.getProperty("test.key"));
    }
    
    /**
     * 测试SystemEnvironmentPropertySource
     */
    @Test
    public void testSystemEnvironmentPropertySource() {
        // 创建一个模拟的环境变量Map
        java.util.Map<String, Object> mockEnv = new java.util.HashMap<>();
        mockEnv.put("TEST_ENV_VAR", "test-value");
        mockEnv.put("NESTED_VALUE", "nested");
        
        SystemEnvironmentPropertySource source = new SystemEnvironmentPropertySource("test", mockEnv);
        
        // 测试原始格式
        assertEquals("test-value", source.getProperty("TEST_ENV_VAR"));
        
        // 测试小写格式
        assertEquals("test-value", source.getProperty("test.env.var"));
        
        // 测试不存在的属性
        assertNull(source.getProperty("non.existent"));
        
        // 测试containsProperty方法
        assertTrue(source.containsProperty("test.env.var"));
        assertTrue(source.containsProperty("TEST_ENV_VAR"));
        assertFalse(source.containsProperty("non.existent"));
    }
    
    /**
     * 测试Environment合并
     */
    @Test
    public void testEnvironmentMerge() {
        // 创建父环境
        StandardEnvironment parent = new StandardEnvironment();
        parent.setActiveProfiles("parent");
        
        // 添加自定义属性源到父环境
        MutablePropertySources parentSources = parent.getPropertySources();
        parentSources.addFirst(new MapPropertySource("parentProperties", 
                java.util.Collections.singletonMap("parent.property", "parent-value")));
        
        // 创建子环境
        StandardEnvironment child = new StandardEnvironment();
        child.setActiveProfiles("child");
        
        // 添加自定义属性源到子环境
        MutablePropertySources childSources = child.getPropertySources();
        childSources.addFirst(new MapPropertySource("childProperties", 
                java.util.Collections.singletonMap("child.property", "child-value")));
        
        // 合并父环境到子环境
        child.merge(parent);
        
        // 测试合并后的profiles
        assertTrue(child.acceptsProfiles("parent"));
        assertTrue(child.acceptsProfiles("child"));
        
        // 测试合并后的属性
        assertEquals("child-value", child.getProperty("child.property"));
        assertEquals("parent-value", child.getProperty("parent.property"));
    }
} 