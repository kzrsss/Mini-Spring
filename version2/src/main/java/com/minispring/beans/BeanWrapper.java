package com.minispring.beans;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Bean包装器，用于封装Bean实例
 * 提供对Bean属性的访问能力，支持嵌套属性
 */
public class BeanWrapper {

    private final Object wrappedInstance;
    private Class<?> wrappedClass;
    private TypeConverter typeConverter;

    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
        this.typeConverter = new SimpleTypeConverter();
    }

    /**
     * 获取包装的Bean实例
     * @return Bean实例
     */
    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    /**
     * 获取包装的Bean实例的类型
     * @return Bean实例的类型
     */
    public Class<?> getWrappedClass() {
        return this.wrappedClass;
    }
    
    /**
     * 获取属性值，支持嵌套属性（如"person.address.city"）
     * 
     * @param propertyName 属性名
     * @return 属性值
     * @throws BeansException 如果获取属性值失败
     */
    public Object getPropertyValue(String propertyName) throws BeansException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new BeansException("属性名不能为空");
        }
        
        // 处理嵌套属性
        if (propertyName.contains(".")) {
            String[] propertyPath = propertyName.split("\\.", 2);
            String currentProperty = propertyPath[0];
            String remainingPath = propertyPath[1];
            
            Object nestedValue = getPropertyValueInternal(wrappedInstance, currentProperty);
            if (nestedValue == null) {
                return null;
            }
            
            BeanWrapper nestedWrapper = new BeanWrapper(nestedValue);
            return nestedWrapper.getPropertyValue(remainingPath);
        }
        
        // 处理简单属性
        return getPropertyValueInternal(wrappedInstance, propertyName);
    }
    
    /**
     * 设置属性值，支持嵌套属性（如"person.address.city"）
     * 
     * @param propertyName 属性名
     * @param value 属性值
     * @throws BeansException 如果设置属性值失败
     */
    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        if (propertyName == null || propertyName.isEmpty()) {
            throw new BeansException("属性名不能为空");
        }
        
        // 处理嵌套属性
        if (propertyName.contains(".")) {
            String[] propertyPath = propertyName.split("\\.", 2);
            String currentProperty = propertyPath[0];
            String remainingPath = propertyPath[1];
            
            Object nestedValue = getPropertyValueInternal(wrappedInstance, currentProperty);
            if (nestedValue == null) {
                // 如果嵌套对象为空，尝试创建一个新实例
                try {
                    Class<?> propertyType = getPropertyType(wrappedClass, currentProperty);
                    if (propertyType == null) {
                        throw new BeansException("无法确定属性类型: " + currentProperty);
                    }
                    
                    nestedValue = propertyType.getDeclaredConstructor().newInstance();
                    setPropertyValueInternal(wrappedInstance, currentProperty, nestedValue);
                } catch (Exception e) {
                    throw new BeansException("无法创建嵌套对象: " + currentProperty, e);
                }
            }
            
            BeanWrapper nestedWrapper = new BeanWrapper(nestedValue);
            nestedWrapper.setPropertyValue(remainingPath, value);
            return;
        }
        
        // 处理简单属性
        setPropertyValueInternal(wrappedInstance, propertyName, value);
    }
    
    /**
     * 内部方法：获取属性值
     * 
     * @param object 目标对象
     * @param propertyName 属性名
     * @return 属性值
     * @throws BeansException 如果获取属性值失败
     */
    private Object getPropertyValueInternal(Object object, String propertyName) throws BeansException {
        try {
            // 首先尝试通过getter方法获取
            String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Method getterMethod = object.getClass().getMethod(getterMethodName);
                return getterMethod.invoke(object);
            } catch (NoSuchMethodException e) {
                // 如果没有getter方法，尝试boolean类型的isXxx方法
                if (propertyName.startsWith("is")) {
                    getterMethodName = propertyName;
                } else {
                    getterMethodName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                }
                
                try {
                    Method isMethod = object.getClass().getMethod(getterMethodName);
                    return isMethod.invoke(object);
                } catch (NoSuchMethodException e1) {
                    // 如果没有getter方法和isXxx方法，尝试直接访问字段
                    Field field = object.getClass().getDeclaredField(propertyName);
                    field.setAccessible(true);
                    return field.get(object);
                }
            }
        } catch (Exception e) {
            throw new BeansException("获取属性值失败: " + propertyName, e);
        }
    }
    
    /**
     * 内部方法：设置属性值
     * 
     * @param object 目标对象
     * @param propertyName 属性名
     * @param value 属性值
     * @throws BeansException 如果设置属性值失败
     */
    private void setPropertyValueInternal(Object object, String propertyName, Object value) throws BeansException {
        try {
            // 获取属性类型
            Class<?> propertyType = getPropertyType(object.getClass(), propertyName);
            if (propertyType == null) {
                throw new BeansException("无法确定属性类型: " + propertyName);
            }
            
            // 类型转换
            Object convertedValue = value;
            if (value != null && !propertyType.isInstance(value)) {
                convertedValue = typeConverter.convert(value, propertyType);
            }
            
            // 首先尝试通过setter方法设置
            String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                Method setterMethod = object.getClass().getMethod(setterMethodName, propertyType);
                setterMethod.invoke(object, convertedValue);
                return;
            } catch (NoSuchMethodException e) {
                // 如果没有setter方法，尝试直接设置字段
                Field field = object.getClass().getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(object, convertedValue);
            }
        } catch (TypeMismatchException e) {
            throw e;
        } catch (Exception e) {
            throw new BeansException("设置属性值失败: " + propertyName, e);
        }
    }
    
    /**
     * 获取属性类型
     * 
     * @param beanClass Bean类型
     * @param propertyName 属性名
     * @return 属性类型，如果找不到则返回null
     */
    private Class<?> getPropertyType(Class<?> beanClass, String propertyName) {
        // 首先尝试通过getter方法获取类型
        String getterMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        try {
            Method getterMethod = beanClass.getMethod(getterMethodName);
            return getterMethod.getReturnType();
        } catch (NoSuchMethodException e) {
            // 如果没有getter方法，尝试boolean类型的isXxx方法
            if (propertyName.startsWith("is")) {
                getterMethodName = propertyName;
            } else {
                getterMethodName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            }
            
            try {
                Method isMethod = beanClass.getMethod(getterMethodName);
                return isMethod.getReturnType();
            } catch (NoSuchMethodException e1) {
                // 如果没有getter方法和isXxx方法，尝试通过setter方法获取类型
                String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                Method[] methods = beanClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                        return method.getParameterTypes()[0];
                    }
                }
                
                // 如果没有setter方法，尝试直接获取字段类型
                try {
                    Field field = beanClass.getDeclaredField(propertyName);
                    return field.getType();
                } catch (NoSuchFieldException e2) {
                    return null;
                }
            }
        }
    }
} 