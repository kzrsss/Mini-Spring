package com.minispring.test.bean;

/**
 * 生命周期Bean类
 * 用于测试初始化和销毁方法
 */
public class LifecycleBean {
    
    private String name;
    private boolean initialized = false;
    private boolean destroyed = false;
    
    public LifecycleBean() {
        System.out.println("LifecycleBean构造函数");
    }
    
    public void init() {
        System.out.println("LifecycleBean初始化方法");
        this.initialized = true;
    }
    
    public void destroy() {
        System.out.println("LifecycleBean销毁方法");
        this.destroyed = true;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
    
    public boolean isDestroyed() {
        return destroyed;
    }
    
    @Override
    public String toString() {
        return "LifecycleBean{" +
                "name='" + name + '\'' +
                ", initialized=" + initialized +
                ", destroyed=" + destroyed +
                '}';
    }
} 