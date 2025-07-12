package com.minispring.beans;

/**
 * Bean属性值
 * 存储Bean属性的名称、原始值和转换后的值
 */
public class PropertyValue {

    private final String name;
    private final Object value;
    private Object convertedValue;

    /**
     * 创建一个新的PropertyValue实例
     * @param name 属性名称
     * @param value 属性原始值
     */
    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 获取属性名称
     * @return 属性名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取属性原始值
     * @return 属性原始值
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * 获取转换后的属性值
     * @return 转换后的属性值，如果未转换则返回null
     */
    public Object getConvertedValue() {
        return convertedValue;
    }
    
    /**
     * 设置转换后的属性值
     * @param convertedValue 转换后的属性值
     */
    public void setConvertedValue(Object convertedValue) {
        this.convertedValue = convertedValue;
    }
} 