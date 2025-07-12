package com.minispring.test.bean;

/**
 * 原型Bean类
 * 用于测试原型作用域
 */
public class PrototypeBean {
    
    private String name;
    private long createTime;
    
    public PrototypeBean() {
        this.createTime = System.currentTimeMillis();
        System.out.println("PrototypeBean构造函数，创建时间：" + createTime);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getCreateTime() {
        return createTime;
    }
    
    @Override
    public String toString() {
        return "PrototypeBean{" +
                "name='" + name + '\'' +
                ", createTime=" + createTime +
                '}';
    }
} 