package com.tyut.shopping_admin_service.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_admin_service.mapper.AdminMapper;
import com.tyut.shopping_common.pojo.Admin;
import com.tyut.shopping_common.pojo.Permission;
import com.tyut.shopping_common.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 管理员业务层实现类
 */
@DubboService
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 新增管理员
     * @param admin
     */
    @Override
    public void addAdmin(Admin admin) {
        adminMapper.insert(admin);
    }

    /**
     * 修改管理员
     * @param admin
     */
    @Override
    public void updateAdmin(Admin admin) {
        String password = admin.getPassword();
        // 如果前端传来空密码，则密码还是原来的密码
        if(!StringUtils.hasText(password)){
            // 查询原来的密码
            String oldPassword = adminMapper.selectById(admin.getAid()).getPassword();
            admin.setPassword(oldPassword);
        }
        adminMapper.updateById(admin);
    }

    /**
     * 删除管理员
     * @param aid
     */
    @Override
    public void deleteAdmin(Long aid) {
        // 删除管理员对应的所有角色
        adminMapper.deleteAllRole(aid);
        // 删除管理员
        adminMapper.deleteById(aid);
    }

    /**
     * 根据ID查询管理员
     * @param aid
     * @return
     */
    @Override
    public Admin findById(Long aid) {
        return adminMapper.findById(aid);
    }

    /**
     * 分页查询管理员
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Admin> pageQueryAdmin(Integer page, Integer size) {
        return adminMapper.selectPage(new Page<>(page, size), null);
    }

    /**
     * 修改管理员角色
     * @param aid
     * @param rids
     */
    @Override
    public void updateRoleToAdmin(Long aid, Long[] rids) {
        // 删除当前管理员的全部角色
        adminMapper.deleteAllRole(aid);
        // 修改当前管理员的角色
        adminMapper.updateRole(aid, rids);
    }

    /**
     * 根据管理员名字查询管理员
     * @param userName
     * @return
     */
    @Override
    public Admin findByAdminName(String userName) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getUsername, userName);
        return adminMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 根据管理员名查询当前管理员的所有权限
     * @param userName
     * @return
     */
    @Override
    public List<Permission> findPermissionByAdminName(String userName) {
        return adminMapper.findPermissionByAdminName(userName);
    }
}
