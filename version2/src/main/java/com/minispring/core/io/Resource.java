package com.minispring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 资源访问接口
 * 定义了资源的基本操作方法
 */
public interface Resource {
    
    /**
     * 检查资源是否存在
     * @return 资源是否存在
     */
    boolean exists();
    
    /**
     * 检查资源是否可读
     * @return 资源是否可读
     */
    boolean isReadable();
    
    /**
     * 获取资源的输入流
     * @return 资源输入流
     * @throws IOException 如果无法获取输入流
     */
    InputStream getInputStream() throws IOException;
    
    /**
     * 获取资源的描述信息
     * @return 资源的描述信息
     */
    String getDescription();
} 