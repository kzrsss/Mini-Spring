package com.minispring.core.convert;

/**
 * 类型转换服务接口
 * 提供统一的类型转换入口
 */
public interface ConversionService {
    
    /**
     * 判断是否可以将源类型对象转换为目标类型
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 如果可以转换则返回true
     */
    boolean canConvert(Class<?> sourceType, Class<?> targetType);
    
    /**
     * 将源对象转换为目标类型
     * @param source 源对象
     * @param targetType 目标类型
     * @param <T> 目标类型泛型
     * @return 转换后的目标类型对象
     * @throws ConversionException 如果转换失败
     */
    <T> T convert(Object source, Class<T> targetType);
} 