package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionService;
import com.minispring.core.convert.converter.Converter;
import com.minispring.core.convert.converter.ConverterFactory;
import com.minispring.core.convert.converter.ConverterRegistry;
import com.minispring.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * 默认的类型转换服务实现
 * 同时实现了ConversionService和ConverterRegistry接口
 */
public class DefaultConversionService implements ConversionService, ConverterRegistry {
    
    private static final DefaultConversionService SHARED_INSTANCE = new DefaultConversionService();
    
    private final GenericConversionService conversionService;
    
    /**
     * 创建一个新的DefaultConversionService实例
     */
    public DefaultConversionService() {
        this.conversionService = new GenericConversionService();
        registerDefaultConverters();
    }
    
    /**
     * 获取共享的DefaultConversionService实例
     * @return 共享实例
     */
    public static ConversionService getSharedInstance() {
        return SHARED_INSTANCE;
    }
    
    /**
     * 注册默认的类型转换器
     */
    public void registerDefaultConverters() {
        // 注册基本类型转换器
        addConverter(new StringToIntegerConverter());
        addConverter(new StringToLongConverter());
        addConverter(new StringToDoubleConverter());
        addConverter(new StringToFloatConverter());
        addConverter(new StringToBooleanConverter());
        addConverter(new StringToCharacterConverter());
        addConverter(new StringToShortConverter());
        addConverter(new StringToByteConverter());
        
        // 注册日期时间转换器
        addConverter(new StringToDateConverter());
        
        // 注册集合转换器
        // 这里可以扩展更多的集合类型转换器
    }
    
    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return this.conversionService.canConvert(sourceType, targetType);
    }
    
    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        return this.conversionService.convert(source, targetType);
    }
    
    @Override
    public void addConverter(Converter<?, ?> converter) {
        this.conversionService.addConverter(converter);
    }
    
    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        this.conversionService.addConverter(sourceType, targetType, converter);
    }
    
    @Override
    public void removeConvertibles() {
        this.conversionService.removeConvertibles();
    }
    
    /**
     * 添加转换器工厂
     * @param factory 转换器工厂
     */
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        this.conversionService.addConverterFactory(factory);
    }
    
    /**
     * 添加通用转换器
     * @param converter 通用转换器
     */
    public void addConverter(GenericConverter converter) {
        this.conversionService.addConverter(converter);
    }
} 