package com.tyut.shopping_admin_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Permission;

public interface PermissionMapper extends BaseMapper<Permission> {
    // 删除角色_权限表中的相关数据
    void deletePermissionAllRole(Long pid);
}