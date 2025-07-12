package com.minispring.core.env;

import java.util.Properties;

/**
 * 系统属性源实现
 * 从系统属性中获取属性值
 */
public class SystemPropertySource extends PropertySource<Properties> {
    
    /**
     * 默认系统属性源名称
     */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
    
    /**
     * 构造函数，使用默认名称和系统属性
     */
    public SystemPropertySource() {
        super(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, System.getProperties());
    }
    
    /**
     * 构造函数，使用指定名称和系统属性
     * 
     * @param name 属性源名称
     */
    public SystemPropertySource(String name) {
        super(name, System.getProperties());
    }
    
    /**
     * 获取指定名称的属性值
     * 
     * @param name 属性名称
     * @return 属性值，如果不存在返回null
     */
    @Override
    public Object getProperty(String name) {
        return this.source.getProperty(name);
    }
} 