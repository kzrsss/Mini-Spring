package com.minispring.core.io;

import com.minispring.util.ClassUtils;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 默认资源加载器实现
 * 支持以下资源类型：
 * 1. URL资源：http://, https://, file://, ftp://等
 * 2. ClassPath资源：classpath:
 * 3. 文件系统资源：默认
 */
public class DefaultResourceLoader implements ResourceLoader {
    
    private ClassLoader classLoader;
    
    public DefaultResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }
    
    public DefaultResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    @Override
    public Resource getResource(String location) {
        if (location == null || location.isEmpty()) {
            throw new IllegalArgumentException("资源路径不能为空");
        }
        
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // 加载classpath资源
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
        }
        
        try {
            // 尝试作为URL资源加载
            URL url = new URL(location);
            return new UrlResource(url);
        } catch (MalformedURLException ex) {
            // 作为文件系统资源加载
            return new FileSystemResource(location);
        }
    }
    
    @Override
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader());
    }
    
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
} 