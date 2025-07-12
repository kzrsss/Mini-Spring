package com.minispring.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件系统资源实现类
 */
public class FileSystemResource implements Resource {
    
    private final String path;
    private final File file;
    
    public FileSystemResource(String path) {
        if (path == null) {
            throw new IllegalArgumentException("路径不能为空");
        }
        this.path = path;
        this.file = new File(path).getAbsoluteFile();
    }
    
    public FileSystemResource(File file) {
        if (file == null) {
            throw new IllegalArgumentException("文件不能为空");
        }
        this.path = file.getPath();
        this.file = file.getAbsoluteFile();
    }
    
    @Override
    public boolean exists() {
        return file.exists();
    }
    
    @Override
    public boolean isReadable() {
        return file.canRead();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return new FileInputStream(file);
        } catch (IOException ex) {
            throw new IOException("无法打开文件 [" + path + "]", ex);
        }
    }
    
    @Override
    public String getDescription() {
        return "文件系统资源 [" + path + "]";
    }
    
    public String getPath() {
        return path;
    }
    
    public File getFile() {
        return file;
    }
    
    /**
     * 获取文件的最后修改时间
     */
    public long lastModified() throws IOException {
        return Files.getLastModifiedTime(file.toPath()).toMillis();
    }
    
    /**
     * 创建相对于此资源的新资源
     */
    public Resource createRelative(String relativePath) {
        if (relativePath == null) {
            throw new IllegalArgumentException("相对路径不能为空");
        }
        
        try {
            // 获取当前文件的路径
            Path basePath = file.toPath();
            
            // 如果当前路径是文件，使用其父目录作为基础路径
            if (Files.isRegularFile(basePath)) {
                basePath = basePath.getParent();
            }
            
            // 解析相对路径
            Path resolvedPath = basePath.resolve(relativePath).normalize();
            return new FileSystemResource(resolvedPath.toFile());
        } catch (Exception ex) {
            throw new IllegalArgumentException("无法创建相对路径资源 [" + relativePath + "]", ex);
        }
    }
} 