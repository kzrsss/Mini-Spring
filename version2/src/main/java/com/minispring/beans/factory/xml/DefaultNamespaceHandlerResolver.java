package com.minispring.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 默认的命名空间处理器解析器实现
 * 从配置文件中加载命名空间URI到处理器类的映射
 */
public class DefaultNamespaceHandlerResolver implements NamespaceHandlerResolver {
    
    /**
     * 默认的处理器映射文件路径
     */
    public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
    
    /**
     * 命名空间URI到处理器类名的映射
     */
    private final Map<String, String> handlerMappings = new HashMap<>();
    
    /**
     * 已解析的处理器缓存
     */
    private final Map<String, NamespaceHandler> handlerCache = new HashMap<>();
    
    /**
     * 使用默认的处理器映射文件路径创建解析器
     */
    public DefaultNamespaceHandlerResolver() {
        this(DEFAULT_HANDLER_MAPPINGS_LOCATION);
    }
    
    /**
     * 使用指定的处理器映射文件路径创建解析器
     * 
     * @param handlerMappingsLocation 处理器映射文件路径
     */
    public DefaultNamespaceHandlerResolver(String handlerMappingsLocation) {
        loadHandlerMappings(handlerMappingsLocation);
    }
    
    /**
     * 从配置文件加载处理器映射
     * 
     * @param handlerMappingsLocation 处理器映射文件路径
     */
    private void loadHandlerMappings(String handlerMappingsLocation) {
        try {
            Properties mappings = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream(handlerMappingsLocation);
            if (is != null) {
                try {
                    mappings.load(is);
                } finally {
                    is.close();
                }
                
                for (Map.Entry<Object, Object> entry : mappings.entrySet()) {
                    String namespaceUri = (String) entry.getKey();
                    String handlerClassName = (String) entry.getValue();
                    handlerMappings.put(namespaceUri, handlerClassName);
                }
            }
        } catch (IOException ex) {
            throw new IllegalStateException("无法加载命名空间处理器映射: " + handlerMappingsLocation, ex);
        }
    }
    
    @Override
    public NamespaceHandler resolve(String namespaceUri) {
        // 先从缓存中查找
        NamespaceHandler handler = handlerCache.get(namespaceUri);
        if (handler != null) {
            return handler;
        }
        
        // 查找处理器类名
        String handlerClassName = handlerMappings.get(namespaceUri);
        if (handlerClassName == null) {
            return null;
        }
        
        try {
            // 加载处理器类
            Class<?> handlerClass = Class.forName(handlerClassName);
            if (!NamespaceHandler.class.isAssignableFrom(handlerClass)) {
                throw new IllegalStateException("类 [" + handlerClassName + "] 不是 NamespaceHandler 的实现");
            }
            
            // 实例化处理器
            handler = (NamespaceHandler) handlerClass.newInstance();
            
            // 初始化处理器
            handler.init();
            
            // 缓存处理器
            handlerCache.put(namespaceUri, handler);
            
            return handler;
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("找不到命名空间处理器类: " + handlerClassName, ex);
        } catch (InstantiationException ex) {
            throw new IllegalStateException("无法实例化命名空间处理器类: " + handlerClassName, ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("无法访问命名空间处理器类: " + handlerClassName, ex);
        }
    }
} 