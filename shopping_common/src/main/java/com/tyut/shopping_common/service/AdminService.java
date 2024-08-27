package com.tyut.shopping_common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Admin;
import com.tyut.shopping_common.pojo.Permission;
import com.tyut.shopping_common.result.BaseResult;

import java.util.List;

/**
 * @version v1.0
 * @apiNote 管理员相关接口
 * @author OldGj 2024/6/1
 */
public interface AdminService {


    /**
     * 新增管理员
     * @param admin
     */
    void addAdmin(Admin admin);

    /**
     * 修改管理员
     * @param admin
     * @return
     */
    void updateAdmin(Admin admin);

    /**
     * 删除管理员
     * @param aid
     * @return
     */
    void deleteAdmin(Long aid);

    /**
     * 根据id查询管理员
     * @param aid
     * @return
     */
    Admin findById(Long aid);

    /**
     * 分页查询管理员
     * @param page
     * @param size
     * @return
     */
    Page<Admin> pageQueryAdmin(Integer page, Integer size);

    /**
     * 修改管理员角色
     * @param aid
     * @param rids
     */
    void updateRoleToAdmin(Long aid, Long[] rids);

    /**
     * 根据管理员名字查询管理员
     * @param userName
     * @return
     */
    Admin findByAdminName(String userName);

    /**
     * 根据管理员名查询当前管理员的所有权限
     * @param userName
     * @return
     */
    List<Permission> findPermissionByAdminName(String userName);

}
