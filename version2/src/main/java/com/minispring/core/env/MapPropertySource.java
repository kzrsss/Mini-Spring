package com.minispring.core.env;

import java.util.Map;

/**
 * 基于Map的属性源实现
 * 从Map中获取属性值
 */
public class MapPropertySource extends PropertySource<Map<String, Object>> {
    
    /**
     * 构造函数
     * 
     * @param name 属性源名称
     * @param source 属性源底层Map
     */
    public MapPropertySource(String name, Map<String, Object> source) {
        super(name, source);
    }
    
    /**
     * 获取指定名称的属性值
     * 
     * @param name 属性名称
     * @return 属性值，如果不存在返回null
     */
    @Override
    public Object getProperty(String name) {
        return this.source.get(name);
    }
    
    /**
     * 判断属性源是否包含指定名称的属性
     * 
     * @param name 属性名称
     * @return 如果包含返回true，否则返回false
     */
    @Override
    public boolean containsProperty(String name) {
        return this.source.containsKey(name);
    }
} 