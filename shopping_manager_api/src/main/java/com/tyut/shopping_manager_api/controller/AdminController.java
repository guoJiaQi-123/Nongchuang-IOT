package com.tyut.shopping_manager_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tyut.shopping_common.pojo.Admin;
import com.tyut.shopping_common.result.BaseResult;
import com.tyut.shopping_common.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 管理员控制层
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @DubboReference
    private AdminService adminService;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * 新增管理员
     * @param admin
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('/admin/add')")
    public BaseResult add(@RequestBody Admin admin) {
        log.info("新增管理员：{}", admin);
        // 对密码加密
        String password = admin.getPassword();
        password = encoder.encode(password);
        admin.setPassword(password);
        adminService.addAdmin(admin);
        return BaseResult.ok();
    }

    /**
     * 修改管理员
     * @param admin
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Admin admin) {
        log.info("修改管理员：{}", admin);
        String password = admin.getPassword();
        if (StringUtils.hasText(password)) {
            password = encoder.encode(password);
            admin.setPassword(password);
        }
        adminService.updateAdmin(admin);
        return BaseResult.ok();
    }

    /**
     * 删除管理员
     * @param aid
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long aid) {
        log.info("删除管理员，管理员ID：{}", aid);
        adminService.deleteAdmin(aid);
        return BaseResult.ok();
    }

    /**
     * 根据ID查询管理员
     * @param aid
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Admin> findById(Long aid) {
        log.info("根据ID查询管理员，管理员ID：{}", aid);
        Admin admin = adminService.findById(aid);
        log.info("查询到的管理员：{}", admin);
        return BaseResult.ok(admin);
    }

    /**
     * 分页查询管理员
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('/admin/search')")
    public BaseResult<Page<Admin>> pageQuery(int page, int size) {
        log.info("分页查询管理员，页码：{}，每页条数：{}", page, size);
        Page<Admin> adminPage = adminService.pageQueryAdmin(page, size);
        return BaseResult.ok(adminPage);
    }

    /**
     * 修改管理员角色
     * @param aid
     * @param rids
     * @return
     */
    @PutMapping("/updateRoleToAdmin")
    public BaseResult updateRoleToAdmin(Long aid, Long[] rids) {
        log.info("修改管理员角色，管理员ID：{}，角色ID：{}", aid, rids);
        adminService.updateRoleToAdmin(aid, rids);
        return BaseResult.ok();
    }

    /**
     * 获取登录管理员名
     * @return
     */
    @GetMapping("/getUsername")
    public BaseResult<String> getUserName(){
        // 1. 获取会话对象
        SecurityContext context = SecurityContextHolder.getContext();
        // 2. 获取认证对象
        Authentication authentication = context.getAuthentication();
        // 3. 获取用户登录信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 获取用户名
        String username = userDetails.getUsername();
        return BaseResult.ok(username);
    }
}
