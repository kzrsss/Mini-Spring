package com.minispring.core.env;

/**
 * 环境接口
 * 表示当前应用程序运行的环境
 * 提供属性访问和Profile功能
 */
public interface Environment {
    
    /**
     * 获取指定名称的属性值
     * 
     * @param key 属性名称
     * @return 属性值，如果不存在返回null
     */
    String getProperty(String key);
    
    /**
     * 获取指定名称的属性值，如果不存在则使用默认值
     * 
     * @param key 属性名称
     * @param defaultValue 默认值
     * @return 属性值，如果不存在返回默认值
     */
    String getProperty(String key, String defaultValue);
    
    /**
     * 获取指定名称的属性值，并转换为指定类型
     * 
     * @param key 属性名称
     * @param targetType 目标类型
     * @param <T> 目标类型泛型
     * @return 属性值，如果不存在返回null
     */
    <T> T getProperty(String key, Class<T> targetType);
    
    /**
     * 获取指定名称的属性值，并转换为指定类型，如果不存在则使用默认值
     * 
     * @param key 属性名称
     * @param targetType 目标类型
     * @param defaultValue 默认值
     * @param <T> 目标类型泛型
     * @return 属性值，如果不存在返回默认值
     */
    <T> T getProperty(String key, Class<T> targetType, T defaultValue);
    
    /**
     * 判断指定名称的属性是否存在
     * 
     * @param key 属性名称
     * @return 如果存在返回true，否则返回false
     */
    boolean containsProperty(String key);
    
    /**
     * 获取当前激活的profiles
     * 
     * @return 激活的profiles数组
     */
    String[] getActiveProfiles();
    
    /**
     * 获取默认的profiles
     * 
     * @return 默认的profiles数组
     */
    String[] getDefaultProfiles();
    
    /**
     * 判断指定的profile是否激活
     * 
     * @param profile profile名称
     * @return 如果激活返回true，否则返回false
     */
    boolean acceptsProfiles(String profile);
    
    /**
     * 判断指定的profiles中是否有任一激活
     * 
     * @param profiles profile名称数组
     * @return 如果有任一激活返回true，否则返回false
     */
    boolean acceptsProfiles(String... profiles);
} 