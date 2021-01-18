package com.myjava.service;


import com.myjava.pojo.User;

public interface UserService {

    /**
     * 查询用户
     * @param userName
     * @return
     */
    User getUserPermission(String userName);
}
