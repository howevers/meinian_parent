package com.myjava.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.myjava.dao.UserDao;
import com.myjava.pojo.User;
import com.myjava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {


    @Autowired
    UserDao userDao;

    /**
     * 查询用户
     * @param userName
     * @return
     */
    @Override
    public User getUserPermission(String userName) {
        return userDao.getUserPermission(userName);
    }
}
