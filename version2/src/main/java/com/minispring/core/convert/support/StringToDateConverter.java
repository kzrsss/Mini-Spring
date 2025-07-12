package com.minispring.core.convert.support;

import com.minispring.core.convert.ConversionException;
import com.minispring.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将字符串转换为日期的转换器
 * 支持多种日期格式
 */
public class StringToDateConverter implements Converter<String, Date> {
    
    private static final String[] DATE_FORMATS = {
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd HH:mm",
            "yyyy-MM-dd",
            "yyyy/MM/dd HH:mm:ss",
            "yyyy/MM/dd HH:mm",
            "yyyy/MM/dd",
            "yyyy.MM.dd HH:mm:ss",
            "yyyy.MM.dd HH:mm",
            "yyyy.MM.dd"
    };
    
    @Override
    public Date convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        
        String value = source.trim();
        
        // 尝试不同的日期格式
        for (String format : DATE_FORMATS) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                return dateFormat.parse(value);
            }
            catch (ParseException ex) {
                // 尝试下一个格式
            }
        }
        
        throw new ConversionException("Cannot parse date from String [" + source + "]");
    }
} 