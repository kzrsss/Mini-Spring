package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * 将字符串转换为单精度浮点数的转换器
 */
public class StringToFloatConverter implements Converter<String, Float> {
    
    @Override
    public Float convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Float.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Float: " + ex.getMessage(), ex);
        }
    }
} 