package com.minispring.test.bean;

/**
 * 用户数据访问接口
 * 用于测试IoC容器
 */
public interface UserDao {

    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    String queryUserName(String userName);
} 