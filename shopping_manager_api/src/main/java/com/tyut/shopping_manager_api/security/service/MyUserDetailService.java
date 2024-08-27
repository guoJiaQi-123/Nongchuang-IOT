package com.tyut.shopping_manager_api.security.service;

import com.tyut.shopping_common.pojo.Admin;
import com.tyut.shopping_common.pojo.Permission;
import com.tyut.shopping_common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 自定义认证授权逻辑
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @DubboReference
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.认证
        Admin admin = adminService.findByAdminName(username);
        if (admin == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2. 授权
        List<Permission> permissions = adminService.findPermissionByAdminName(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (permissions.get(0) != null) {
            for (Permission permission : permissions) {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getUrl()));
            }
        }
        // 3.封装userDetail对象并返回
        return User.withUsername(username)
                .password(admin.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}
