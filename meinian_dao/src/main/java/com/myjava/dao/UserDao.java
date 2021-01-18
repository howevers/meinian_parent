package com.myjava.dao;

import com.myjava.pojo.User;

public interface UserDao {

    /**
     * 查询用户
     * @param userName
     */
    User getUserPermission(String userName);
}
