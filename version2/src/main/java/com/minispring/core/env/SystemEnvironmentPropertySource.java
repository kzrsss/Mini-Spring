package com.minispring.core.env;

import java.util.Map;

/**
 * 用于处理系统环境变量的PropertySource
 * 系统环境变量通常是大写且包含下划线
 * 此类提供了对环境变量不同格式的支持
 */
public class SystemEnvironmentPropertySource extends MapPropertySource {
    
    /**
     * 构造函数
     * 
     * @param name 属性源名称
     * @param source 系统环境变量Map
     */
    public SystemEnvironmentPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
    
    /**
     * 获取属性值
     * 支持不同格式的环境变量名
     * 
     * @param name 属性名
     * @return 属性值，如果不存在返回null
     */
    @Override
    public Object getProperty(String name) {
        String actualKey = resolveKey(name);
        if (actualKey == null) {
            return null;
        }
        return super.getProperty(actualKey);
    }
    
    /**
     * 判断属性是否存在
     * 支持不同格式的环境变量名
     * 
     * @param name 属性名
     * @return 如果存在返回true，否则返回false
     */
    @Override
    public boolean containsProperty(String name) {
        String actualKey = resolveKey(name);
        return actualKey != null && super.containsProperty(actualKey);
    }
    
    /**
     * 解析实际的环境变量键名
     * 尝试多种格式：原始、大写、大写下划线、大写点分隔等
     * 
     * @param name 属性名
     * @return 实际的环境变量键名，如果不存在返回null
     */
    private String resolveKey(String name) {
        // 尝试原始键名
        if (super.containsProperty(name)) {
            return name;
        }
        
        // 尝试全大写
        String upperCase = name.toUpperCase();
        if (super.containsProperty(upperCase)) {
            return upperCase;
        }
        
        // 尝试将点分隔符替换为下划线，并全大写
        String withUnderscores = upperCase.replace('.', '_');
        if (super.containsProperty(withUnderscores)) {
            return withUnderscores;
        }
        
        // 尝试将中划线替换为下划线，并全大写
        String withUnderscoresForDashes = withUnderscores.replace('-', '_');
        if (super.containsProperty(withUnderscoresForDashes)) {
            return withUnderscoresForDashes;
        }
        
        return null;
    }
} 