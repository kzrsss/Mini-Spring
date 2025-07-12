package com.minispring.beans;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 简单类型转换器
 * 实现基本的类型转换功能
 */
public class SimpleTypeConverter implements TypeConverter {

    // 转换器缓存，提高性能
    private static final Map<Class<?>, Function<Object, Object>> CONVERTERS = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, Object> convertCache = new ConcurrentHashMap<>();

    // 初始化转换器映射
    static {
        CONVERTERS.put(String.class, Object::toString);
        CONVERTERS.put(Integer.class, SimpleTypeConverter::convertToInteger);
        CONVERTERS.put(int.class, SimpleTypeConverter::convertToInteger);
        CONVERTERS.put(Long.class, SimpleTypeConverter::convertToLong);
        CONVERTERS.put(long.class, SimpleTypeConverter::convertToLong);
        CONVERTERS.put(Double.class, SimpleTypeConverter::convertToDouble);
        CONVERTERS.put(double.class, SimpleTypeConverter::convertToDouble);
        CONVERTERS.put(Float.class, SimpleTypeConverter::convertToFloat);
        CONVERTERS.put(float.class, SimpleTypeConverter::convertToFloat);
        CONVERTERS.put(Boolean.class, SimpleTypeConverter::convertToBoolean);
        CONVERTERS.put(boolean.class, SimpleTypeConverter::convertToBoolean);
        CONVERTERS.put(Character.class, SimpleTypeConverter::convertToCharacter);
        CONVERTERS.put(char.class, SimpleTypeConverter::convertToCharacter);
        CONVERTERS.put(Byte.class, SimpleTypeConverter::convertToByte);
        CONVERTERS.put(byte.class, SimpleTypeConverter::convertToByte);
        CONVERTERS.put(Short.class, SimpleTypeConverter::convertToShort);
        CONVERTERS.put(short.class, SimpleTypeConverter::convertToShort);
        
        // 添加日期时间类型转换
        CONVERTERS.put(LocalDate.class, SimpleTypeConverter::convertToLocalDate);
        CONVERTERS.put(LocalTime.class, SimpleTypeConverter::convertToLocalTime);
        CONVERTERS.put(LocalDateTime.class, SimpleTypeConverter::convertToLocalDateTime);
    }

    /**
     * 获取支持的类型列表
     * @return 支持的类型集合
     */
    public Set<Class<?>> getSupportedTypes() {
        return Collections.unmodifiableSet(CONVERTERS.keySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, Class<T> requiredType) throws TypeMismatchException {
        if (value == null) {
            return null;
        }
        
        // 如果已经是目标类型，直接返回
        if (requiredType.isInstance(value)) {
            return (T) value;
        }
        
        // 检查缓存
        String cacheKey = value.toString() + "_" + requiredType.getName();
        Object cachedValue = convertCache.get(cacheKey);
        if (cachedValue != null) {
            return (T) cachedValue;
        }
        
        try {
            Object convertedValue;
            
            // 字符串转换
            if (value instanceof String) {
                String stringValue = (String) value;
                convertedValue = convertFromString(stringValue, requiredType);
            }
            // 数字类型之间的转换
            else if (value instanceof Number) {
                convertedValue = convertNumber((Number) value, requiredType);
            }
            // 其他类型转换为字符串
            else {
                convertedValue = convertToString(value, requiredType);
            }
            
            // 缓存转换结果
            if (convertedValue != null) {
                convertCache.put(cacheKey, convertedValue);
            }
            
            return (T) convertedValue;
            
        } catch (Exception e) {
            throw new TypeMismatchException(value, requiredType, e);
        }
    }
    
    /**
     * 从字符串转换为目标类型
     */
    private Object convertFromString(String value, Class<?> requiredType) {
        // 基本类型转换
        if (requiredType == Integer.class || requiredType == int.class) {
            return Integer.parseInt(value);
        }
        if (requiredType == Long.class || requiredType == long.class) {
            return Long.parseLong(value);
        }
        if (requiredType == Double.class || requiredType == double.class) {
            return Double.parseDouble(value);
        }
        if (requiredType == Float.class || requiredType == float.class) {
            return Float.parseFloat(value);
        }
        if (requiredType == Boolean.class || requiredType == boolean.class) {
            String stringValue = value.toLowerCase().trim();
            if ("true".equals(stringValue) || 
                "yes".equals(stringValue) || 
                "1".equals(stringValue) || 
                "on".equals(stringValue) ||
                "y".equals(stringValue)) {
                return true;
            } else if ("false".equals(stringValue) || 
                      "no".equals(stringValue) || 
                      "0".equals(stringValue) || 
                      "off".equals(stringValue) ||
                      "n".equals(stringValue)) {
                return false;
            }
            throw new IllegalArgumentException("无法将字符串 [" + value + "] 转换为Boolean");
        }
        if (requiredType == Byte.class || requiredType == byte.class) {
            return Byte.parseByte(value);
        }
        if (requiredType == Short.class || requiredType == short.class) {
            return Short.parseShort(value);
        }
        if (requiredType == Character.class || requiredType == char.class) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("无法将字符串 [" + value + "] 转换为Character，字符串长度必须为1");
            }
            return value.charAt(0);
        }
        
        // 日期时间类型转换
        if (requiredType == LocalDate.class) {
            return LocalDate.parse(value);
        }
        if (requiredType == LocalTime.class) {
            return LocalTime.parse(value);
        }
        if (requiredType == LocalDateTime.class) {
            try {
                // 尝试使用ISO格式解析
                return LocalDateTime.parse(value);
            } catch (DateTimeParseException e) {
                // 尝试使用常见格式解析
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(value, formatter);
                    } catch (DateTimeParseException ignored) {
                        // 继续尝试下一个格式
                    }
                }
                
                // 如果是纯日期格式，尝试转换为当天开始时间
                try {
                    return LocalDate.parse(value).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // 继续尝试其他格式
                }
                
                throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalDateTime，支持的格式：" +
                    "ISO, yyyy-MM-dd HH:mm:ss, yyyy/MM/dd HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss, " +
                    "yyyy.MM.dd HH:mm:ss, yyyy-MM-dd HH:mm, yyyy/MM/dd HH:mm, yyyy-MM-dd");
            }
        }
        
        // 如果目标类型就是String，直接返回
        if (requiredType == String.class) {
            return value;
        }
        
        throw new TypeMismatchException("不支持的类型转换: " + value.getClass().getName() + 
                " -> " + requiredType.getName());
    }
    
    /**
     * 数字类型之间的转换
     */
    private Object convertNumber(Number value, Class<?> requiredType) {
        if (requiredType == Integer.class || requiredType == int.class) {
            return value.intValue();
        }
        if (requiredType == Long.class || requiredType == long.class) {
            return value.longValue();
        }
        if (requiredType == Double.class || requiredType == double.class) {
            return value.doubleValue();
        }
        if (requiredType == Float.class || requiredType == float.class) {
            return value.floatValue();
        }
        if (requiredType == Byte.class || requiredType == byte.class) {
            return value.byteValue();
        }
        if (requiredType == Short.class || requiredType == short.class) {
            return value.shortValue();
        }
        if (requiredType == String.class) {
            return value.toString();
        }
        
        throw new TypeMismatchException("不支持的数字类型转换: " + value.getClass().getName() + 
                " -> " + requiredType.getName());
    }
    
    /**
     * 转换为字符串
     */
    private Object convertToString(Object value, Class<?> requiredType) {
        if (requiredType != String.class) {
            throw new TypeMismatchException("不支持的类型转换: " + value.getClass().getName() + 
                    " -> " + requiredType.getName());
        }
        return value.toString();
    }
    
    private static Integer convertToInteger(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            return Integer.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Integer");
    }
    
    private static Long convertToLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Long");
    }
    
    private static Double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Double");
    }
    
    private static Float convertToFloat(Object value) {
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            return Float.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Float");
    }
    
    private static Boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            String stringValue = ((String) value).toLowerCase().trim();
            if ("true".equals(stringValue) || 
                "yes".equals(stringValue) || 
                "1".equals(stringValue) || 
                "on".equals(stringValue) ||
                "y".equals(stringValue)) {
                return true;
            } else if ("false".equals(stringValue) || 
                      "no".equals(stringValue) || 
                      "0".equals(stringValue) || 
                      "off".equals(stringValue) ||
                      "n".equals(stringValue)) {
                return false;
            }
            throw new IllegalArgumentException("无法将字符串 [" + value + "] 转换为Boolean");
        } else if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Boolean");
    }
    
    private static Character convertToCharacter(Object value) {
        if (value instanceof Character) {
            return (Character) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.length() == 1) {
                return stringValue.charAt(0);
            }
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Character");
    }
    
    private static Byte convertToByte(Object value) {
        if (value instanceof Number) {
            return ((Number) value).byteValue();
        } else if (value instanceof String) {
            return Byte.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Byte");
    }
    
    private static Short convertToShort(Object value) {
        if (value instanceof Number) {
            return ((Number) value).shortValue();
        } else if (value instanceof String) {
            return Short.valueOf((String) value);
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为Short");
    }
    
    // 日期时间类型转换方法
    
    private static LocalDate convertToLocalDate(Object value) {
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // 尝试使用ISO格式解析
                return LocalDate.parse(stringValue);
            } catch (DateTimeParseException e) {
                // 尝试使用常见格式解析
                try {
                    return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e1) {
                    try {
                        return LocalDate.parse(stringValue, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                    } catch (DateTimeParseException e2) {
                        throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalDate，支持的格式：ISO, yyyy-MM-dd, yyyy/MM/dd", e2);
                    }
                }
            }
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalDate();
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalDate");
    }
    
    private static LocalTime convertToLocalTime(Object value) {
        if (value instanceof LocalTime) {
            return (LocalTime) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // 尝试使用ISO格式解析
                return LocalTime.parse(stringValue);
            } catch (DateTimeParseException e) {
                // 尝试使用常见格式解析
                try {
                    return LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm:ss"));
                } catch (DateTimeParseException e1) {
                    try {
                        return LocalTime.parse(stringValue, DateTimeFormatter.ofPattern("HH:mm"));
                    } catch (DateTimeParseException e2) {
                        throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalTime，支持的格式：ISO, HH:mm:ss, HH:mm", e2);
                    }
                }
            }
        } else if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).toLocalTime();
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalTime");
    }
    
    private static LocalDateTime convertToLocalDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof String) {
            String stringValue = (String) value;
            try {
                // 尝试使用ISO格式解析
                return LocalDateTime.parse(stringValue);
            } catch (DateTimeParseException e) {
                // 尝试使用常见格式解析
                DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")
                };
                
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        return LocalDateTime.parse(stringValue, formatter);
                    } catch (DateTimeParseException ignored) {
                        // 继续尝试下一个格式
                    }
                }
                
                // 如果是纯日期格式，尝试转换为当天开始时间
                try {
                    return LocalDate.parse(stringValue).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // 继续尝试其他格式
                }
                
                throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalDateTime，支持的格式：" +
                    "ISO, yyyy-MM-dd HH:mm:ss, yyyy/MM/dd HH:mm:ss, yyyy-MM-dd'T'HH:mm:ss, " +
                    "yyyy.MM.dd HH:mm:ss, yyyy-MM-dd HH:mm, yyyy/MM/dd HH:mm, yyyy-MM-dd");
            }
        } else if (value instanceof LocalDate) {
            return ((LocalDate) value).atStartOfDay();
        }
        throw new IllegalArgumentException("无法将 [" + value + "] 转换为LocalDateTime");
    }
} 