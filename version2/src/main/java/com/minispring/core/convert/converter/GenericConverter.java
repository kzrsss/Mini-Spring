package com.minispring.core.convert.converter;

import java.util.Set;

/**
 * 通用转换器接口
 * 支持更复杂的类型转换场景
 */
public interface GenericConverter {
    
    /**
     * 返回该转换器支持的源类型到目标类型的转换对
     * @return 支持的转换类型对集合，如果该转换器不支持任何类型则返回null
     */
    Set<ConvertiblePair> getConvertibleTypes();
    
    /**
     * 转换给定的源对象到目标类型
     * @param source 源对象
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 转换后的对象
     */
    Object convert(Object source, Class<?> sourceType, Class<?> targetType);
    
    /**
     * 描述源类型到目标类型的转换对
     */
    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;
        
        /**
         * 创建一个新的转换对
         * @param sourceType 源类型
         * @param targetType 目标类型
         */
        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }
        
        /**
         * 获取源类型
         * @return 源类型
         */
        public Class<?> getSourceType() {
            return this.sourceType;
        }
        
        /**
         * 获取目标类型
         * @return 目标类型
         */
        public Class<?> getTargetType() {
            return this.targetType;
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            ConvertiblePair otherPair = (ConvertiblePair) other;
            return this.sourceType.equals(otherPair.sourceType) && this.targetType.equals(otherPair.targetType);
        }
        
        @Override
        public int hashCode() {
            return this.sourceType.hashCode() * 31 + this.targetType.hashCode();
        }
    }
} 