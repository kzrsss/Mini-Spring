package com.minispring.beans.factory.config;

import com.minispring.beans.PropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean定义
 * 用于定义Bean的基本信息，如类名、属性值、初始化方法等
 */
public class BeanDefinition {

    private Class<?> beanClass;
    private PropertyValues propertyValues;
    private String initMethodName;
    private String destroyMethodName;
    private boolean singleton = true;
    private boolean prototype = false;
    
    // 作用域
    private String scope = SCOPE_SINGLETON;
    
    // 是否需要作用域代理
    private boolean scopedProxy = false;
    
    // 自定义属性容器
    private final Map<String, Object> attributes = new HashMap<>();

    /**
     * 默认作用域
     */
    public static String SCOPE_SINGLETON = "singleton";
    public static String SCOPE_PROTOTYPE = "prototype";

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new PropertyValues();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValues propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = propertyValues != null ? propertyValues : new PropertyValues();
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public boolean isPrototype() {
        return prototype;
    }

    public void setPrototype(boolean prototype) {
        this.prototype = prototype;
    }

    /**
     * 设置Bean的作用域
     * @param scope 作用域，可选值：singleton、prototype
     */
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }
    
    /**
     * 获取Bean的作用域
     * @return 作用域
     */
    public String getScope() {
        return this.scope;
    }
    
    /**
     * 设置是否需要作用域代理
     * @param scopedProxy 是否需要作用域代理
     */
    public void setScopedProxy(boolean scopedProxy) {
        this.scopedProxy = scopedProxy;
    }
    
    /**
     * 判断是否需要作用域代理
     * @return 如果需要作用域代理返回true
     */
    public boolean isScopedProxy() {
        return this.scopedProxy;
    }
    
    /**
     * 设置自定义属性
     * @param name 属性名
     * @param value 属性值
     */
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }
    
    /**
     * 获取自定义属性
     * @param name 属性名
     * @return 属性值
     */
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }
    
    /**
     * 判断是否有指定的自定义属性
     * @param name 属性名
     * @return 如果存在属性返回true
     */
    public boolean hasAttribute(String name) {
        return this.attributes.containsKey(name);
    }
} 