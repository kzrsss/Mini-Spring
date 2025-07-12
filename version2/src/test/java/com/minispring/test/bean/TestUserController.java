package com.minispring.test.bean;

/**
 * 测试用户控制器类
 */
public class TestUserController {
    private TestUserService userService;
    
    public TestUserService getUserService() {
        return userService;
    }
    
    public void setUserService(TestUserService userService) {
        this.userService = userService;
    }
} 