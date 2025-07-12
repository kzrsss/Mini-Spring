package com.minispring.core.convert.support;

import com.minispring.core.convert.converter.Converter;

import java.util.HashSet;
import java.util.Set;

/**
 * 将字符串转换为布尔值的转换器
 * 支持以下形式的true值：
 * "true", "on", "yes", "y", "1"
 * 
 * 支持以下形式的false值：
 * "false", "off", "no", "n", "0"
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {
    
    private static final Set<String> TRUE_VALUES = new HashSet<>(8);
    private static final Set<String> FALSE_VALUES = new HashSet<>(8);
    
    static {
        // 初始化true值集合
        TRUE_VALUES.add("true");
        TRUE_VALUES.add("on");
        TRUE_VALUES.add("yes");
        TRUE_VALUES.add("y");
        TRUE_VALUES.add("1");
        
        // 初始化false值集合
        FALSE_VALUES.add("false");
        FALSE_VALUES.add("off");
        FALSE_VALUES.add("no");
        FALSE_VALUES.add("n");
        FALSE_VALUES.add("0");
    }
    
    @Override
    public Boolean convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        
        String value = source.trim().toLowerCase();
        if (TRUE_VALUES.contains(value)) {
            return Boolean.TRUE;
        }
        else if (FALSE_VALUES.contains(value)) {
            return Boolean.FALSE;
        }
        else {
            // 默认返回false
            return Boolean.FALSE;
        }
    }
} 