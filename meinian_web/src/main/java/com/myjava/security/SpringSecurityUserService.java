package com.myjava.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.myjava.pojo.Permission;
import com.myjava.pojo.Role;
import com.myjava.pojo.User;
import com.myjava.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        //1、查询用户
        User user = userService.getUserPermission(userName);

        //2、构建权限集合
        HashSet<GrantedAuthority> set = new HashSet<>();
        //3、查询用户拥有的角色
        Set<Role> roles = user.getRoles();
        //4、查询角色拥有的权限
        for (Role role : roles) {
            set.add(new SimpleGrantedAuthority(role.getKeyword()));
            for (Permission permission : role.getPermissions()) {
                set.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        //5、返回权限集合

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),set);
    }
}
