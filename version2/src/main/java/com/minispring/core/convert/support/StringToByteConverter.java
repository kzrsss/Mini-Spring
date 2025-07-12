package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * 将字符串转换为字节的转换器
 */
public class StringToByteConverter implements Converter<String, Byte> {
    
    @Override
    public Byte convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return Byte.valueOf(source.trim());
        }
        catch (NumberFormatException ex) {
            throw new ConversionException("Failed to convert String to Byte: " + ex.getMessage(), ex);
        }
    }
} 