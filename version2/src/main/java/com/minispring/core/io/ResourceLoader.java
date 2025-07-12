package com.minispring.core.io;

/**
 * 资源加载器接口
 * 定义了获取资源的统一方法
 */
public interface ResourceLoader {
    
    /** Classpath URL前缀 */
    String CLASSPATH_URL_PREFIX = "classpath:";
    
    /** 文件系统URL前缀 */
    String FILE_URL_PREFIX = "file:";
    
    /** HTTP URL前缀 */
    String HTTP_URL_PREFIX = "http:";
    
    /** HTTPS URL前缀 */
    String HTTPS_URL_PREFIX = "https:";
    
    /**
     * 获取资源
     * @param location 资源位置
     * @return 资源对象
     */
    Resource getResource(String location);
    
    /**
     * 获取类加载器
     * @return 类加载器
     */
    ClassLoader getClassLoader();
} 