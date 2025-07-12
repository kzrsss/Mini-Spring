package com.minispring.core.env;

import java.util.Iterator;

/**
 * 属性源集合接口
 * 管理一组PropertySource
 */
public interface PropertySources extends Iterable<PropertySource<?>> {
    
    /**
     * 返回包含指定名称的属性源
     * 
     * @param name 属性源名称
     * @return 属性源，如果不存在返回null
     */
    PropertySource<?> get(String name);
    
    /**
     * 判断是否包含指定名称的属性源
     * 
     * @param name 属性源名称
     * @return 如果包含返回true，否则返回false
     */
    boolean contains(String name);
    
    /**
     * 返回属性源的迭代器
     * 
     * @return 属性源迭代器
     */
    @Override
    Iterator<PropertySource<?>> iterator();
} 