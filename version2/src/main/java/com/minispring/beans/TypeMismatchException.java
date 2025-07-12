package com.minispring.beans;

/**
 * 类型不匹配异常
 * 当类型转换失败时抛出此异常
 */
public class TypeMismatchException extends BeansException {

    private final Class<?> requiredType;
    private final Object value;
    private String additionalContext;

    /**
     * 构造类型不匹配异常
     * 
     * @param value 需要转换的值
     * @param requiredType 目标类型
     */
    public TypeMismatchException(Object value, Class<?> requiredType) {
        super("无法将值 '" + value + "' 转换为类型 '" + requiredType.getName() + "'");
        this.value = value;
        this.requiredType = requiredType;
    }

    /**
     * 构造类型不匹配异常
     * 
     * @param value 需要转换的值
     * @param requiredType 目标类型
     * @param cause 原始异常
     */
    public TypeMismatchException(Object value, Class<?> requiredType, Throwable cause) {
        super("无法将值 '" + value + "' 转换为类型 '" + requiredType.getName() + "'", cause);
        this.value = value;
        this.requiredType = requiredType;
    }

    /**
     * 构造类型不匹配异常
     * @param message 错误消息
     */
    public TypeMismatchException(String message) {
        super(message);
        this.value = null;
        this.requiredType = null;
    }

    /**
     * 获取目标类型
     * 
     * @return 目标类型
     */
    public Class<?> getRequiredType() {
        return this.requiredType;
    }

    /**
     * 获取需要转换的值
     * 
     * @return 需要转换的值
     */
    public Object getValue() {
        return this.value;
    }
    
    /**
     * 添加上下文信息
     * 
     * @param context 上下文信息
     * @return 当前异常实例，用于链式调用
     */
    public TypeMismatchException addContext(String context) {
        this.additionalContext = context;
        return this;
    }
    
    /**
     * 获取上下文信息
     * 
     * @return 上下文信息
     */
    public String getAdditionalContext() {
        return this.additionalContext;
    }
    
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (additionalContext != null && !additionalContext.isEmpty()) {
            message = message + ". " + additionalContext;
        }
        return message;
    }
} 