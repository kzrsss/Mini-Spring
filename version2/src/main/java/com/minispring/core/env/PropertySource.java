package com.minispring.core.env;

/**
 * 属性源抽象类
 * 表示一个键值对形式的属性源
 * 
 * @param <T> 属性源的底层来源类型
 */
public abstract class PropertySource<T> {
    
    /**
     * 属性源名称
     */
    private final String name;
    
    /**
     * 属性源的底层来源
     */
    protected final T source;
    
    /**
     * 构造函数
     * 
     * @param name 属性源名称
     * @param source 属性源底层来源
     */
    public PropertySource(String name, T source) {
        this.name = name;
        this.source = source;
    }
    
    /**
     * 获取属性源名称
     * 
     * @return 属性源名称
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 获取属性源底层来源
     * 
     * @return 属性源底层来源
     */
    public T getSource() {
        return this.source;
    }
    
    /**
     * 判断属性源是否包含指定名称的属性
     * 
     * @param name 属性名称
     * @return 如果包含返回true，否则返回false
     */
    public boolean containsProperty(String name) {
        return getProperty(name) != null;
    }
    
    /**
     * 获取指定名称的属性值
     * 
     * @param name 属性名称
     * @return 属性值，如果不存在返回null
     */
    public abstract Object getProperty(String name);
    
    /**
     * 重写equals方法
     * 通过属性源名称判断是否相等
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PropertySource)) {
            return false;
        }
        PropertySource<?> otherSource = (PropertySource<?>) other;
        return this.name.equals(otherSource.name);
    }
    
    /**
     * 重写hashCode方法
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
    
    /**
     * 重写toString方法
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " {name='" + this.name + "'}";
    }
} 