package com.tyut.shopping_admin_service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_admin_service.mapper.RoleMapper;
import com.tyut.shopping_common.pojo.Role;
import com.tyut.shopping_common.service.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/2
 * @apiNote 角色相关业务层实现类
 */
@DubboService
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 新增角色
     * @param role
     */
    @Override
    public void add(Role role) {
        roleMapper.insert(role);
    }

    /**
     * 修改角色
     * @param role
     */
    @Override
    public void update(Role role) {
        roleMapper.updateById(role);
    }

    /**
     * 删除角色
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 删除角色
        roleMapper.deleteById(id);
        // 删除角色的所有权限
        roleMapper.deleteRoleAllPermission(id);
        // 删除用户_角色中间表的相关数据
        roleMapper.deleteRoleAllAdmin(id);
    }

    /**
     * 根据ID查询角色
     * @param id
     * @return
     */
    @Override
    public Role findById(Long id) {
        return roleMapper.findById(id);
    }

    /**
     * 查询所有角色
     * @return
     */
    @Override
    public List<Role> findAll() {
        return roleMapper.selectList(null);
    }

    /**
     * 分页查询角色
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Role> search(int page, int size) {
        return roleMapper.selectPage(new Page<>(page,size),null);
    }

    /**
     * 修改角色权限
     * @param rid
     * @param pids
     */
    @Override
    public void updatePermissionToRole(Long rid, Long[] pids) {
        // 删除角色的所有权限
        roleMapper.deleteRoleAllPermission(rid);
        // 给角色添加权限
        for (Long pid : pids) {
            roleMapper.addPermissionToRole(rid,pid);
        }
    }
}
