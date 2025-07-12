package com.minispring.test.bean;

/**
 * 测试用户服务类（构造函数注入）
 */
public class TestUserServiceWithConstructor {
    private final TestUserDao userDao;
    
    public TestUserServiceWithConstructor() {
        this.userDao = null;
    }
    
    public TestUserServiceWithConstructor(TestUserDao userDao) {
        this.userDao = userDao;
    }
    
    public TestUserDao getUserDao() {
        return userDao;
    }
} 