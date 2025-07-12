package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.ConversionService;
import com.minispring.core.convert.converter.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用类型转换服务
 * 提供完整的类型转换功能和注册API
 */
public class GenericConversionService implements ConversionService, ConverterRegistry {
    
    /**
     * 类型转换器注册表
     * 保存所有注册的通用转换器
     */
    private final Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new ConcurrentHashMap<>(256);
    
    /**
     * 转换缓存，用于提高性能
     */
    private final Map<Class<?>, Map<Class<?>, GenericConverter>> converterCache = new ConcurrentHashMap<>(256);
    
    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        if (sourceType == null) {
            return true;
        }
        if (targetType == null) {
            throw new IllegalArgumentException("Target type to convert to cannot be null");
        }
        
        // 如果源类型可分配给目标类型，不需要转换
        if (targetType.isAssignableFrom(sourceType)) {
            return true;
        }
        
        // 查找转换器，如果找到则可以转换
        return getConverter(sourceType, targetType) != null;
    }
    
    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        if (source == null) {
            return null;
        }
        
        Class<?> sourceType = source.getClass();
        
        // 如果源类型可分配给目标类型，直接转换
        if (targetType.isAssignableFrom(sourceType)) {
            @SuppressWarnings("unchecked")
            T result = (T) source;
            return result;
        }
        
        // 查找转换器
        GenericConverter converter = getConverter(sourceType, targetType);
        if (converter == null) {
            throw new ConversionException("No converter found for converting from " + sourceType.getName() + " to " + targetType.getName());
        }
        
        // 执行转换
        @SuppressWarnings("unchecked")
        T result = (T) converter.convert(source, sourceType, targetType);
        return result;
    }
    
    @Override
    public void addConverter(Converter<?, ?> converter) {
        ResolvableType[] typeInfo = getRequiredTypeInfo(converter.getClass(), Converter.class);
        Class<?> sourceType = typeInfo[0].resolve();
        Class<?> targetType = typeInfo[1].resolve();
        
        addConverter(new ConverterAdapter(converter, sourceType, targetType));
    }
    
    @Override
    public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
        addConverter(new ConverterAdapter(converter, sourceType, targetType));
    }
    
    @Override
    public void removeConvertibles() {
        this.converters.clear();
        this.converterCache.clear();
    }
    
    /**
     * 添加转换器工厂
     * @param factory 转换器工厂
     */
    public void addConverterFactory(ConverterFactory<?, ?> factory) {
        ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
        Class<?> sourceType = typeInfo[0].resolve();
        Class<?> targetType = typeInfo[1].resolve();
        
        addConverter(new ConverterFactoryAdapter(factory, sourceType, targetType));
    }
    
    /**
     * 添加通用转换器
     * @param converter 通用转换器
     */
    public void addConverter(GenericConverter converter) {
        Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
        if (convertibleTypes != null) {
            for (GenericConverter.ConvertiblePair convertiblePair : convertibleTypes) {
                this.converters.put(convertiblePair, converter);
                this.converterCache.clear();
            }
        }
    }
    
    /**
     * 获取源类型到目标类型的转换器
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 找到的转换器，如果没有找到则返回null
     */
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        // 先从缓存中查找
        Map<Class<?>, GenericConverter> targetConverters = this.converterCache.get(sourceType);
        if (targetConverters != null) {
            GenericConverter converter = targetConverters.get(targetType);
            if (converter != null) {
                return converter;
            }
        }
        
        // 查找所有可能的转换器
        GenericConverter converter = find(sourceType, targetType);
        if (converter != null) {
            // 缓存找到的转换器
            if (targetConverters == null) {
                targetConverters = new ConcurrentHashMap<>(4);
                this.converterCache.put(sourceType, targetConverters);
            }
            targetConverters.put(targetType, converter);
        }
        
        return converter;
    }
    
    /**
     * 查找所有可能的转换器
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 找到的转换器，如果没有找到则返回null
     */
    private GenericConverter find(Class<?> sourceType, Class<?> targetType) {
        // 遍历所有转换器，查找匹配的转换器
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        
        for (Class<?> sourceCandidate : sourceCandidates) {
            for (Class<?> targetCandidate : targetCandidates) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = this.converters.get(convertiblePair);
                if (converter != null) {
                    // 检查有条件转换器的匹配条件
                    if (!(converter instanceof ConditionalConverter) || 
                            ((ConditionalConverter) converter).matches(sourceType, targetType)) {
                        return converter;
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * 获取类及其所有父类和接口的层次结构
     * @param type 要处理的类型
     * @return 类层次结构列表
     */
    private List<Class<?>> getClassHierarchy(Class<?> type) {
        List<Class<?>> hierarchy = new ArrayList<>();
        Set<Class<?>> visited = new HashSet<>();
        collectClassHierarchy(type, hierarchy, visited);
        return hierarchy;
    }
    
    /**
     * 递归收集类及其所有父类和接口
     * @param type 要处理的类型
     * @param hierarchy 类层次结构列表
     * @param visited 已访问过的类型集合
     */
    private void collectClassHierarchy(Class<?> type, List<Class<?>> hierarchy, Set<Class<?>> visited) {
        if (type == null || visited.contains(type)) {
            return;
        }
        
        visited.add(type);
        hierarchy.add(type);
        
        // 收集接口
        for (Class<?> interfaceType : type.getInterfaces()) {
            collectClassHierarchy(interfaceType, hierarchy, visited);
        }
        
        // 收集父类
        collectClassHierarchy(type.getSuperclass(), hierarchy, visited);
    }
    
    /**
     * 从转换器类型中获取所需的类型信息
     * @param converterClass 转换器类
     * @param genericIfc 泛型接口
     * @return 类型信息数组
     */
    private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
        ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
        ResolvableType[] generics = resolvableType.getGenerics();
        if (generics.length < 2) {
            throw new IllegalArgumentException("Unable to determine source and target types for converter " + 
                    converterClass.getName());
        }
        return generics;
    }
    
    /**
     * Converter适配器，将Converter适配为GenericConverter
     */
    private final class ConverterAdapter implements ConditionalGenericConverter {
        private final Converter<Object, Object> converter;
        private final GenericConverter.ConvertiblePair convertiblePair;
        private final ConversionService conversionService;
        
        @SuppressWarnings("unchecked")
        public ConverterAdapter(Converter<?, ?> converter, Class<?> sourceType, Class<?> targetType) {
            this.converter = (Converter<Object, Object>) converter;
            this.convertiblePair = new GenericConverter.ConvertiblePair(sourceType, targetType);
            this.conversionService = GenericConversionService.this;
        }
        
        @Override
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(this.convertiblePair);
        }
        
        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            // 简单实现，始终返回true，表示无条件匹配
            return true;
        }
        
        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            if (source == null) {
                return null;
            }
            return this.converter.convert(source);
        }
    }
    
    /**
     * ConverterFactory适配器，将ConverterFactory适配为GenericConverter
     */
    private final class ConverterFactoryAdapter implements ConditionalGenericConverter {
        private final ConverterFactory<Object, Object> converterFactory;
        private final Class<?> sourceType;
        private final Class<?> targetType;
        
        @SuppressWarnings("unchecked")
        public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, Class<?> sourceType, Class<?> targetType) {
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
            this.sourceType = sourceType;
            this.targetType = targetType;
        }
        
        @Override
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(new GenericConverter.ConvertiblePair(this.sourceType, this.targetType));
        }
        
        @Override
        public boolean matches(Class<?> sourceType, Class<?> targetType) {
            // 检查targetType是否是targetType的子类
            return this.sourceType.isAssignableFrom(sourceType) && targetType.isAssignableFrom(this.targetType);
        }
        
        @Override
        public Object convert(Object source, Class<?> sourceType, Class<?> targetType) {
            if (source == null) {
                return null;
            }
            return this.converterFactory.getConverter(targetType).convert(source);
        }
    }
    
    /**
     * 可解析类型，用于处理泛型类型
     */
    private static class ResolvableType {
        private final Class<?> resolved;
        private final ResolvableType superType;
        private final Class<?> rawClass;
        
        private ResolvableType(Class<?> resolved, ResolvableType superType, Class<?> rawClass) {
            this.resolved = resolved;
            this.superType = superType;
            this.rawClass = rawClass;
        }
        
        public static ResolvableType forClass(Class<?> clazz) {
            return new ResolvableType(clazz, null, clazz);
        }
        
        public ResolvableType as(Class<?> type) {
            if (this.rawClass != null && type.isAssignableFrom(this.rawClass)) {
                return forClass(type);
            }
            return forClass(type);
        }
        
        public ResolvableType[] getGenerics() {
            // 简化实现，返回空数组
            // 实际应该解析泛型参数
            return new ResolvableType[0];
        }
        
        public Class<?> resolve() {
            return this.resolved;
        }
    }
} 