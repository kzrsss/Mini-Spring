package com.minispring.core.env;

import java.util.Map;

/**
 * 可配置的环境接口
 * 扩展Environment，提供配置功能
 */
public interface ConfigurableEnvironment extends Environment {
    
    /**
     * 设置活跃的profiles
     * 
     * @param profiles 活跃的profiles数组
     */
    void setActiveProfiles(String... profiles);
    
    /**
     * 添加活跃的profiles
     * 
     * @param profile 活跃的profile
     */
    void addActiveProfile(String profile);
    
    /**
     * 设置默认的profiles
     * 
     * @param profiles 默认的profiles数组
     */
    void setDefaultProfiles(String... profiles);
    
    /**
     * 获取系统属性
     * 
     * @return 系统属性
     */
    Map<String, Object> getSystemProperties();
    
    /**
     * 获取系统环境变量
     * 
     * @return 系统环境变量
     */
    Map<String, Object> getSystemEnvironment();
    
    /**
     * 合并另一个Environment的配置
     * 
     * @param parent 父Environment
     */
    void merge(ConfigurableEnvironment parent);
    
    /**
     * 获取属性源集合
     * 
     * @return 属性源集合
     */
    MutablePropertySources getPropertySources();
} 