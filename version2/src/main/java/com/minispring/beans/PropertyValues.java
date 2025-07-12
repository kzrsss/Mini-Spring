package com.minispring.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Bean属性值集合
 * 用于存储和管理多个PropertyValue对象
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList = new ArrayList<>();

    /**
     * 添加属性值
     * 如果已存在同名属性，则替换
     * @param propertyValue 属性值对象，不能为null
     * @throws IllegalArgumentException 如果propertyValue为null
     */
    public void addPropertyValue(PropertyValue propertyValue) {
        if (propertyValue == null) {
            throw new IllegalArgumentException("PropertyValue不能为null");
        }
        
        // 移除已存在的同名属性
        this.propertyValueList.removeIf(existing ->
                existing.getName().equals(propertyValue.getName()));
        this.propertyValueList.add(propertyValue);
    }

    /**
     * 获取所有属性值
     * @return 属性值数组，不会为null，可能为空数组
     */
    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[0]);
    }

    /**
     * 根据属性名获取属性值
     * @param propertyName 属性名
     * @return 包含属性值的Optional对象，如果不存在则为空Optional
     */
    public Optional<PropertyValue> getPropertyValue(String propertyName) {
        if (propertyName == null) {
            return Optional.empty();
        }
        
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(propertyName)) {
                return Optional.of(propertyValue);
            }
        }
        return Optional.empty();
    }
    
    /**
     * 判断是否包含指定名称的属性
     * @param propertyName 属性名
     * @return 是否包含
     */
    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName).isPresent();
    }
    
    /**
     * 获取属性值数量
     * @return 属性值数量
     */
    public int size() {
        return propertyValueList.size();
    }
    
    /**
     * 判断是否为空
     * @return 是否为空
     */
    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }
    
    /**
     * 获取不可修改的属性值列表
     * @return 不可修改的属性值列表
     */
    public List<PropertyValue> getPropertyValueList() {
        return Collections.unmodifiableList(propertyValueList);
    }
} 