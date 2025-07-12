package com.minispring.test.bean;

/**
 * 测试用户服务类（属性注入）
 */
public class TestUserService {
    private String name;
    private TestUserDao userDao;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public TestUserDao getUserDao() {
        return userDao;
    }
    
    public void setUserDao(TestUserDao userDao) {
        this.userDao = userDao;
    }
} 