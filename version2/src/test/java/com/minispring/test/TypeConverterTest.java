package com.minispring.test;

import com.minispring.beans.BeanWrapper;
import com.minispring.beans.SimpleTypeConverter;
import com.minispring.beans.TypeConverter;
import com.minispring.beans.TypeMismatchException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 类型转换器测试类
 */
public class TypeConverterTest {

    /**
     * 测试基本类型转换
     */
    @Test
    public void testBasicTypeConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // 测试字符串转整数
        assertEquals(42, converter.convert("42", Integer.class));
        
        // 测试字符串转长整数
        assertEquals(123L, converter.convert("123", Long.class));
        
        // 测试字符串转双精度浮点数
        assertEquals(3.14, converter.convert("3.14", Double.class));
        
        // 测试字符串转单精度浮点数
        assertEquals(3.14f, converter.convert("3.14", Float.class));
        
        // 测试字符串转布尔值
        assertTrue(converter.convert("true", Boolean.class));
        assertTrue(converter.convert("yes", Boolean.class));
        assertTrue(converter.convert("1", Boolean.class));
        assertFalse(converter.convert("false", Boolean.class));
        
        // 测试字符串转字符
        assertEquals('A', converter.convert("A", Character.class));
        
        // 测试数字转字符串
        assertEquals("42", converter.convert(42, String.class));
    }

    /**
     * 测试日期时间类型转换
     */
    @Test
    public void testDateTimeConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // 测试字符串转LocalDate
        assertEquals(LocalDate.parse("2023-12-25"), 
                converter.convert("2023-12-25", LocalDate.class));
        
        // 测试字符串转LocalTime
        assertEquals(LocalTime.parse("12:34:56"), 
                converter.convert("12:34:56", LocalTime.class));
        
        // 测试字符串转LocalDateTime（ISO格式）
        assertEquals(LocalDateTime.parse("2023-12-25T12:34:56"), 
                converter.convert("2023-12-25T12:34:56", LocalDateTime.class));
        
        // 测试字符串转LocalDateTime（自定义格式）
        assertEquals(LocalDateTime.parse("2023-12-25T12:34:56"), 
                converter.convert("2023-12-25 12:34:56", LocalDateTime.class));
    }

    /**
     * 测试无效转换
     */
    @Test
    public void testInvalidConversion() {
        TypeConverter converter = new SimpleTypeConverter();
        
        // 测试无效的数字格式
        assertThrows(TypeMismatchException.class, () -> {
            converter.convert("not a number", Integer.class);
        });
        
        // 测试无效的日期格式
        assertThrows(TypeMismatchException.class, () -> {
            converter.convert("not a date", LocalDate.class);
        });
    }

    /**
     * 测试并发转换
     */
    @Test
    public void testConcurrentConversion() throws InterruptedException {
        int threadCount = 10;
        int iterationsPerThread = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    TypeConverter localConverter = new SimpleTypeConverter();
                    for (int j = 0; j < iterationsPerThread; j++) {
                        assertEquals(j, localConverter.convert(String.valueOf(j), Integer.class));
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executorService.shutdown();
    }

    /**
     * 测试嵌套属性
     */
    @Test
    public void testNestedProperty() {
        ParentBean parent = new ParentBean();
        BeanWrapper wrapper = new BeanWrapper(parent);
        
        // 使用BeanWrapper设置嵌套属性，会自动创建子对象
        wrapper.setPropertyValue("child.name", "Alice");
        
        assertNotNull(parent.getChild());
        assertEquals("Alice", parent.getChild().getName());
        
        // 测试获取嵌套属性
        Object value = wrapper.getPropertyValue("child.name");
        assertEquals("Alice", value);
    }
    
    /**
     * 测试BeanWrapper与类型转换结合使用
     */
    @Test
    public void testBeanWrapperWithTypeConversion() {
        TestBean testBean = new TestBean();
        BeanWrapper wrapper = new BeanWrapper(testBean);
        
        // 设置各种类型的属性，需要进行类型转换
        wrapper.setPropertyValue("intValue", "123");
        wrapper.setPropertyValue("longValue", "123456789");
        wrapper.setPropertyValue("doubleValue", "123.456");
        wrapper.setPropertyValue("booleanValue", "true");
        wrapper.setPropertyValue("stringValue", 123);
        wrapper.setPropertyValue("dateValue", "2023-01-01");
        
        // 验证转换结果
        assertEquals(123, testBean.getIntValue());
        assertEquals(123456789L, testBean.getLongValue());
        assertEquals(123.456, testBean.getDoubleValue(), 0.0001);
        assertTrue(testBean.isBooleanValue());
        assertEquals("123", testBean.getStringValue());
        assertEquals(LocalDate.of(2023, 1, 1), testBean.getDateValue());
    }

    /**
     * 父Bean类，用于测试嵌套属性
     */
    public static class ParentBean {
        private ChildBean child;

        public ChildBean getChild() {
            return child;
        }

        public void setChild(ChildBean child) {
            this.child = child;
        }
    }

    /**
     * 子Bean类，用于测试嵌套属性
     */
    public static class ChildBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    /**
     * 测试Bean类，用于测试类型转换
     */
    public static class TestBean {
        private int intValue;
        private long longValue;
        private double doubleValue;
        private boolean booleanValue;
        private String stringValue;
        private LocalDate dateValue;
        
        public int getIntValue() {
            return intValue;
        }
        
        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
        
        public long getLongValue() {
            return longValue;
        }
        
        public void setLongValue(long longValue) {
            this.longValue = longValue;
        }
        
        public double getDoubleValue() {
            return doubleValue;
        }
        
        public void setDoubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
        }
        
        public boolean isBooleanValue() {
            return booleanValue;
        }
        
        public void setBooleanValue(boolean booleanValue) {
            this.booleanValue = booleanValue;
        }
        
        public String getStringValue() {
            return stringValue;
        }
        
        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
        
        public LocalDate getDateValue() {
            return dateValue;
        }
        
        public void setDateValue(LocalDate dateValue) {
            this.dateValue = dateValue;
        }
    }
} 