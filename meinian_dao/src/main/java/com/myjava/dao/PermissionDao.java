package com.myjava.dao;

import com.myjava.pojo.Permission;

import java.util.List;

public interface PermissionDao {

    List<Permission> getUserPermissionsById(Integer id);
}
