package com.minispring.core.env;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 可变属性源集合
 * 提供对属性源集合的增删改操作
 */
public class MutablePropertySources implements PropertySources {
    
    /**
     * 属性源列表，使用线程安全的CopyOnWriteArrayList
     */
    private final List<PropertySource<?>> propertySources;
    
    /**
     * 默认构造函数
     */
    public MutablePropertySources() {
        this.propertySources = new CopyOnWriteArrayList<>();
    }
    
    /**
     * 使用现有属性源列表构造
     * 
     * @param propertySources 现有属性源列表
     */
    public MutablePropertySources(List<PropertySource<?>> propertySources) {
        this.propertySources = new CopyOnWriteArrayList<>(propertySources);
    }
    
    /**
     * 获取属性源列表，返回副本以避免修改
     * 
     * @return 属性源列表的副本
     */
    public List<PropertySource<?>> getPropertySources() {
        return new ArrayList<>(this.propertySources);
    }
    
    /**
     * 添加属性源到顶部
     * 
     * @param propertySource 要添加的属性源
     */
    public void addFirst(PropertySource<?> propertySource) {
        removeIfPresent(propertySource);
        this.propertySources.add(0, propertySource);
    }
    
    /**
     * 添加属性源到底部
     * 
     * @param propertySource 要添加的属性源
     */
    public void addLast(PropertySource<?> propertySource) {
        removeIfPresent(propertySource);
        this.propertySources.add(propertySource);
    }
    
    /**
     * 在指定名称的属性源之前添加属性源
     * 
     * @param relativePropertySourceName 相对属性源名称
     * @param propertySource 要添加的属性源
     * @throws IllegalArgumentException 如果相对属性源不存在
     */
    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = indexOf(relativePropertySourceName);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "属性源 '" + relativePropertySourceName + "' 不存在");
        }
        this.propertySources.add(index, propertySource);
    }
    
    /**
     * 在指定名称的属性源之后添加属性源
     * 
     * @param relativePropertySourceName 相对属性源名称
     * @param propertySource 要添加的属性源
     * @throws IllegalArgumentException 如果相对属性源不存在
     */
    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = indexOf(relativePropertySourceName);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "属性源 '" + relativePropertySourceName + "' 不存在");
        }
        this.propertySources.add(index + 1, propertySource);
    }
    
    /**
     * 替换指定名称的属性源
     * 
     * @param name 要替换的属性源名称
     * @param propertySource 新的属性源
     * @throws IllegalArgumentException 如果指定名称的属性源不存在
     */
    public void replace(String name, PropertySource<?> propertySource) {
        int index = indexOf(name);
        if (index == -1) {
            throw new IllegalArgumentException(
                    "属性源 '" + name + "' 不存在");
        }
        this.propertySources.set(index, propertySource);
    }
    
    /**
     * 移除指定名称的属性源
     * 
     * @param name 要移除的属性源名称
     * @return 被移除的属性源，如果不存在返回null
     */
    public PropertySource<?> remove(String name) {
        int index = indexOf(name);
        if (index != -1) {
            return this.propertySources.remove(index);
        }
        return null;
    }
    
    /**
     * 获取指定名称的属性源
     * 
     * @param name 属性源名称
     * @return 属性源，如果不存在返回null
     */
    @Override
    public PropertySource<?> get(String name) {
        for (PropertySource<?> propertySource : this.propertySources) {
            if (propertySource.getName().equals(name)) {
                return propertySource;
            }
        }
        return null;
    }
    
    /**
     * 判断是否包含指定名称的属性源
     * 
     * @param name 属性源名称
     * @return 如果包含返回true，否则返回false
     */
    @Override
    public boolean contains(String name) {
        return this.propertySources.stream()
                .anyMatch(propertySource -> propertySource.getName().equals(name));
    }
    
    /**
     * 返回属性源的迭代器
     * 
     * @return 属性源迭代器
     */
    @Override
    public Iterator<PropertySource<?>> iterator() {
        return this.propertySources.iterator();
    }
    
    /**
     * 如果属性源已存在则移除
     * 
     * @param propertySource 要移除的属性源
     */
    private void removeIfPresent(PropertySource<?> propertySource) {
        this.propertySources.removeIf(source -> source.getName().equals(propertySource.getName()));
    }
    
    /**
     * 检查相对添加是否合法
     * 
     * @param relativePropertySourceName 相对属性源名称
     * @param propertySource 要添加的属性源
     * @throws IllegalArgumentException 如果相对添加不合法
     */
    private void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (relativePropertySourceName == null) {
            throw new IllegalArgumentException("相对属性源名称不能为null");
        }
        if (propertySource == null) {
            throw new IllegalArgumentException("属性源不能为null");
        }
        if (relativePropertySourceName.equals(propertySource.getName())) {
            throw new IllegalArgumentException("相对属性源与要添加的属性源不能同名");
        }
    }
    
    /**
     * 获取指定名称的属性源索引
     * 
     * @param name 属性源名称
     * @return 索引，如果不存在返回-1
     */
    private int indexOf(String name) {
        for (int i = 0; i < this.propertySources.size(); i++) {
            if (this.propertySources.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * 重写toString方法
     */
    @Override
    public String toString() {
        return this.propertySources.toString();
    }
} 