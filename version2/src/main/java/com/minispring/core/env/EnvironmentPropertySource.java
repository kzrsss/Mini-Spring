package com.minispring.core.env;

import java.util.Map;

/**
 * 环境变量属性源实现
 * 从系统环境变量中获取属性值
 */
public class EnvironmentPropertySource extends MapPropertySource {
    
    /**
     * 默认环境变量属性源名称
     */
    public static final String ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
    
    /**
     * 构造函数，使用默认名称和系统环境变量
     */
    public EnvironmentPropertySource() {
        super(ENVIRONMENT_PROPERTY_SOURCE_NAME, (Map) System.getenv());
    }
    
    /**
     * 构造函数，使用指定名称和系统环境变量
     * 
     * @param name 属性源名称
     */
    public EnvironmentPropertySource(String name) {
        super(name, (Map) System.getenv());
    }
    
    /**
     * 获取指定名称的属性值
     * 对环境变量名称进行特殊处理，支持不同格式的环境变量名称
     * 
     * @param name 属性名称
     * @return 属性值，如果不存在返回null
     */
    @Override
    public Object getProperty(String name) {
        // 首先尝试直接获取
        Object value = super.getProperty(name);
        if (value != null) {
            return value;
        }
        
        // 尝试转换格式后获取
        // 例如，将user.home尝试转换为USER_HOME
        String alternateName = name.replace('.', '_').toUpperCase();
        return super.getProperty(alternateName);
    }
    
    /**
     * 判断属性源是否包含指定名称的属性
     * 
     * @param name 属性名称
     * @return 如果包含返回true，否则返回false
     */
    @Override
    public boolean containsProperty(String name) {
        // 首先尝试直接判断
        if (super.containsProperty(name)) {
            return true;
        }
        
        // 尝试转换格式后判断
        String alternateName = name.replace('.', '_').toUpperCase();
        return super.containsProperty(alternateName);
    }
} 