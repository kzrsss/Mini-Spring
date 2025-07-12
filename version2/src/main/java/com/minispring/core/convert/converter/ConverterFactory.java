package com.minispring.core.convert.converter;

/**
 * 转换器工厂接口
 * 根据目标类型创建对应的转换器
 * 
 * @param <S> 源类型
 * @param <R> 目标类型的基类
 */
public interface ConverterFactory<S, R> {
    
    /**
     * 获取从S转换到目标类型T的转换器，其中T是R的子类
     * 
     * @param targetType 目标类型
     * @param <T> 目标类型的泛型参数，是R的子类
     * @return 对应的转换器
     */
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
} 