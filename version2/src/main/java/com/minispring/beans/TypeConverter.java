package com.minispring.beans;

/**
 * 类型转换器接口
 * 用于在属性注入过程中将值转换为目标类型
 */
public interface TypeConverter {

    /**
     * 将值转换为指定类型
     * @param value 要转换的值
     * @param requiredType 目标类型
     * @param <T> 目标类型
     * @return 转换后的值
     * @throws TypeMismatchException 如果转换失败
     */
    <T> T convert(Object value, Class<T> requiredType) throws TypeMismatchException;
}