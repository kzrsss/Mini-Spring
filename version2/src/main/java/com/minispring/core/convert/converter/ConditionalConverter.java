package com.minispring.core.convert.converter;

/**
 * 有条件的转换器接口
 * 可以根据特定条件判断是否可以执行转换
 */
public interface ConditionalConverter {
    
    /**
     * 判断是否可以将源类型转换为目标类型
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 如果可以转换则返回true
     */
    boolean matches(Class<?> sourceType, Class<?> targetType);
} 