package com.minispring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 类路径资源实现类
 */
public class ClassPathResource implements Resource {
    
    private final String path;
    private final ClassLoader classLoader;
    
    public ClassPathResource(String path) {
        this(path, null);
    }
    
    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path == null) {
            throw new IllegalArgumentException("路径不能为空");
        }
        this.path = path.startsWith("/") ? path.substring(1) : path;
        this.classLoader = classLoader != null ? classLoader : getDefaultClassLoader();
    }
    
    @Override
    public boolean exists() {
        return classLoader.getResource(path) != null;
    }
    
    @Override
    public boolean isReadable() {
        return true;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null) {
            throw new FileNotFoundException("类路径资源 [" + path + "] 不存在");
        }
        return is;
    }
    
    @Override
    public String getDescription() {
        return "类路径资源 [" + path + "]";
    }
    
    public String getPath() {
        return path;
    }
    
    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    /**
     * 获取默认的类加载器
     */
    private static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // 无法获取线程上下文类加载器时忽略
        }
        if (cl == null) {
            cl = ClassPathResource.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // 无法获取系统类加载器时忽略
                }
            }
        }
        return cl;
    }
} 