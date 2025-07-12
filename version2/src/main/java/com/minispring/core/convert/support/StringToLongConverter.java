package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * 将字符串转换为长整数的转换器
 */
public class StringToLongConverter implements Converter<String, Long> {
    
    @Override
    public Long convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Long: " + ex.getMessage(), ex);
        }
    }
} 