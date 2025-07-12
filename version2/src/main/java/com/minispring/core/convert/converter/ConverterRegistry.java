package com.minispring.core.convert.converter;

/**
 * 转换器注册表接口
 * 用于注册和管理类型转换器
 */
public interface ConverterRegistry {
    
    /**
     * 注册一个转换器
     * @param converter 转换器实例
     */
    void addConverter(Converter<?, ?> converter);
    
    /**
     * 注册一个特定类型的转换器
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @param converter 转换器
     * @param <S> 源类型泛型
     * @param <T> 目标类型泛型
     */
    <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter);
    
    /**
     * 移除所有转换器
     */
    void removeConvertibles();
} 