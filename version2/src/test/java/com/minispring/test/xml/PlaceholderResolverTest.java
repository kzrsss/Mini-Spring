package com.minispring.test.xml;

import com.minispring.beans.factory.config.PropertyPlaceholderResolver;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 占位符解析器测试类
 */
public class PlaceholderResolverTest {
    
    @Test
    void testResolvePlaceholders() {
        // 创建属性
        Properties properties = new Properties();
        properties.setProperty("name", "张三");
        properties.setProperty("age", "30");
        properties.setProperty("city", "北京市");
        
        // 创建占位符解析器
        PropertyPlaceholderResolver resolver = new PropertyPlaceholderResolver(properties);
        
        // 测试解析单个占位符
        String value1 = resolver.resolvePlaceholders("Hello, ${name}!");
        assertEquals("Hello, 张三!", value1);
        
        // 测试解析多个占位符
        String value2 = resolver.resolvePlaceholders("${name}今年${age}岁，住在${city}");
        assertEquals("张三今年30岁，住在北京市", value2);
        
        // 测试解析带默认值的占位符
        String value3 = resolver.resolvePlaceholders("${name}的工作是${job:程序员}");
        assertEquals("张三的工作是程序员", value3);
        
        // 测试解析不存在的占位符
        String value4 = resolver.resolvePlaceholders("${name}的爱好是${hobby}");
        assertEquals("张三的爱好是${hobby}", value4);
        
        // 测试检查是否包含占位符
        assertTrue(resolver.containsPlaceholder("Hello, ${name}!"));
    }
}