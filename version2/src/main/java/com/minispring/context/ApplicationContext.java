package com.minispring.context;

import com.minispring.beans.factory.ListableBeanFactory;
import com.minispring.core.env.Environment;

/**
 * ApplicationContext接口
 * Spring应用上下文，继承ListableBeanFactory，提供更多企业级功能
 */
public interface ApplicationContext extends ListableBeanFactory {
    
    /**
     * 获取应用上下文的名称
     * @return 应用上下文名称
     */
    String getApplicationName();
    
    /**
     * 获取应用上下文的启动时间
     * @return 启动时间（毫秒）
     */
    long getStartupDate();
    
    /**
     * 获取Environment
     * @return Environment
     */
    Environment getEnvironment();
} 