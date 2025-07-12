package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

/**
 * 将字符串转换为字符的转换器
 */
public class StringToCharacterConverter implements Converter<String, Character> {
    
    @Override
    public Character convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        if (source.length() > 1) {
            throw new ConversionException("String [" + source + "] has more than one character");
        }
        return source.charAt(0);
    }
} 