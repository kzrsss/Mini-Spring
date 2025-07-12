package com.minispring.core.convert;

/**
 * 类型转换异常
 * 当类型转换失败时抛出
 */
public class ConversionException extends RuntimeException {
    
    /**
     * 创建一个新的类型转换异常
     * @param message 异常消息
     */
    public ConversionException(String message) {
        super(message);
    }
    
    /**
     * 创建一个新的类型转换异常
     * @param message 异常消息
     * @param cause 原始异常
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }
} 