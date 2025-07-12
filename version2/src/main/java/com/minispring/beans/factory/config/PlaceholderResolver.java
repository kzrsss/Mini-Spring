package com.minispring.beans.factory.config;

/**
 * 占位符解析器接口
 * 用于解析属性值中的占位符，如${...}
 */
public interface PlaceholderResolver {
    
    /**
     * 解析包含占位符的字符串
     * 
     * @param value 包含占位符的字符串
     * @return 解析后的字符串
     */
    String resolvePlaceholders(String value);
    
    /**
     * 检查字符串是否包含占位符
     * 
     * @param value 要检查的字符串
     * @return 如果包含占位符则返回true，否则返回false
     */
    boolean containsPlaceholder(String value);
} 