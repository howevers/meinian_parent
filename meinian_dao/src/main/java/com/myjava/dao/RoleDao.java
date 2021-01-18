package com.myjava.dao;

import com.myjava.pojo.Role;

import java.util.List;

public interface RoleDao {


    List<Role> getUserRoleById(Integer id);
}
