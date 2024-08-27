package com.tyut.shopping_admin_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tyut.shopping_common.pojo.Admin;
import com.tyut.shopping_common.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 管理员相关持久层
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 删除管理员对应的所有角色
     * @param aid
     */
    void deleteAllRole(Long aid);

    /**
     * 根据ID查询管理员（包括角色和权限）
     * @param aid
     * @return
     */
    Admin findById(Long aid);

    /**
     * 修改管理员角色
     * @param aid
     * @param rids
     */
    void updateRole(@Param("aid") Long aid, @Param("rids") Long[] rids);

    /**
     * 根据管理员名字查询当前管理员的所有权限
     * @param userName
     * @return
     */
    List<Permission> findPermissionByAdminName(String userName);
}
