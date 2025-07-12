package com.minispring.core.convert.converter;

/**
 * 类型转换器接口
 * 将S类型的对象转换为T类型
 * @param <S> 源类型
 * @param <T> 目标类型
 */
@FunctionalInterface
public interface Converter<S, T> {
    
    /**
     * 将源对象转换为目标类型
     * @param source 源对象，不会为null
     * @return 转换后的对象，可能为null
     */
    T convert(S source);
} 