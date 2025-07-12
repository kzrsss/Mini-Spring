package com.minispring.test;

import com.minispring.core.io.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 资源加载器测试类
 */
public class ResourceLoaderTest {
    
    private ResourceLoader resourceLoader;
    private Path resourcesDir;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() throws IOException {
        resourceLoader = new DefaultResourceLoader();
        
        // 创建测试资源目录
        resourcesDir = tempDir.resolve("test-resources");
        Files.createDirectories(resourcesDir);
        
        // 创建测试资源文件
        Path testFile = resourcesDir.resolve("test.txt");
        Files.write(testFile, "Hello, MiniSpring!".getBytes(StandardCharsets.UTF_8));
        
        // 创建自定义类加载器
        URL[] urls = new URL[]{resourcesDir.toUri().toURL()};
        URLClassLoader classLoader = new URLClassLoader(urls);
        resourceLoader = new DefaultResourceLoader(classLoader);
    }
    
    @Test
    void testClassPathResource() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:test.txt");
        assertTrue(resource instanceof ClassPathResource);
        assertTrue(resource.isReadable());
        
        // 验证资源内容
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals("Hello, MiniSpring!", reader.readLine());
        }
    }
    
    @Test
    void testFileSystemResource() throws IOException {
        // 创建测试文件
        String content = "File System Resource Test";
        Path filePath = tempDir.resolve("fs-test.txt");
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));
        
        // 测试文件系统资源
        Resource resource = resourceLoader.getResource(filePath.toString());
        assertTrue(resource instanceof FileSystemResource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        
        // 验证资源内容
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals(content, reader.readLine());
        }
        
        // 测试不存在的文件
        Resource nonExistentResource = resourceLoader.getResource(tempDir.resolve("non-existent.txt").toString());
        assertFalse(nonExistentResource.exists());
    }
    
    @Test
    void testUrlResource() throws IOException {
        // 测试HTTP URL资源
        Resource resource = resourceLoader.getResource("https://www.baidu.com");
        assertTrue(resource instanceof UrlResource);
        assertTrue(resource.isReadable());
        
        // 验证资源内容（仅验证能够获取输入流）
        try (InputStream is = resource.getInputStream()) {
            assertNotNull(is);
            assertTrue(is.read() > -1);
        }
        
        // 测试无效的URL
        Resource invalidResource = resourceLoader.getResource("https://invalid.example.com");
        assertFalse(invalidResource.exists());
    }
    
    @Test
    void testResourceDescription() {
        // 测试各种资源的描述信息
        Resource classpathResource = resourceLoader.getResource("classpath:test.txt");
        assertTrue(classpathResource.getDescription().contains("类路径资源"));
        
        Resource fileResource = resourceLoader.getResource("/path/to/file.txt");
        assertTrue(fileResource.getDescription().contains("文件系统资源"));
        
        Resource urlResource = resourceLoader.getResource("https://www.example.com");
        assertTrue(urlResource.getDescription().contains("URL资源"));
    }
    
    @Test
    void testNullAndEmptyLocations() {
        // 测试空路径
        assertThrows(IllegalArgumentException.class, () -> resourceLoader.getResource(null));
        assertThrows(IllegalArgumentException.class, () -> resourceLoader.getResource(""));
    }
    
    @Test
    void testCustomClassLoader() {
        // 测试自定义类加载器
        ClassLoader customClassLoader = new URLClassLoader(new URL[0]);
        ResourceLoader customLoader = new DefaultResourceLoader(customClassLoader);
        assertEquals(customClassLoader, customLoader.getClassLoader());
    }
    
    @Test
    void testRelativeFileSystemResource() throws IOException {
        // 创建测试目录结构
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        System.out.println("子目录路径: " + subDir.toAbsolutePath());
        
        // 创建测试文件
        String content = "Test Content";
        Path testFile = subDir.resolve("test.txt");
        Files.write(testFile, content.getBytes(StandardCharsets.UTF_8));
        System.out.println("测试文件路径: " + testFile.toAbsolutePath());
        
        // 测试相对路径资源
        FileSystemResource baseResource = new FileSystemResource(subDir.toFile());
        System.out.println("基础资源路径: " + baseResource.getPath());
        assertTrue(baseResource.exists(), "基础目录应该存在");
        
        Resource relativeResource = baseResource.createRelative("test.txt");
        System.out.println("相对资源路径: " + ((FileSystemResource)relativeResource).getPath());
        System.out.println("相对资源是否存在: " + relativeResource.exists());
        System.out.println("相对资源文件是否存在: " + ((FileSystemResource)relativeResource).getFile().exists());
        assertTrue(relativeResource.exists(), "相对路径资源应该存在");
        
        // 验证资源内容
        try (InputStream is = relativeResource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            assertEquals(content, reader.readLine(), "资源内容应该匹配");
        }
        
        // 测试不存在的相对路径
        Resource nonExistentResource = baseResource.createRelative("non-existent.txt");
        assertFalse(nonExistentResource.exists(), "不存在的资源应该返回false");
    }
} 