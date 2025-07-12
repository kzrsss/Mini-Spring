package com.minispring.core.env;

/**
 * Profile接口
 * 表示一个或多个配置环境
 */
public interface Profile {
    
    /**
     * 判断Profile是否活跃
     * 
     * @return 如果活跃返回true，否则返回false
     */
    boolean isActive();
    
    /**
     * 获取Profile名称
     * 
     * @return Profile名称
     */
    String getName();
} 