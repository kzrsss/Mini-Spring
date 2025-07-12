package com.minispring.core.env;

/**
 * Environment的标准实现
 * 包含系统属性和环境变量
 */
public class StandardEnvironment extends AbstractEnvironment {
    
    /**
     * 系统属性源名称
     */
    public static final String SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME = "systemProperties";
    
    /**
     * 系统环境变量属性源名称
     */
    public static final String SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME = "systemEnvironment";
    
    /**
     * 自定义属性源
     * 添加系统属性和环境变量
     * 
     * @param propertySources 属性源集合
     */
    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        propertySources.addLast(new MapPropertySource(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, getSystemEnvironment()));
    }
} 