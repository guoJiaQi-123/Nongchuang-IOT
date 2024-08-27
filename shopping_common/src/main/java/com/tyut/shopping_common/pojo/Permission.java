package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后台管理员权限
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission implements Serializable {
    @TableId
    private Long pid; // 权限id
    private String permissionName; // 权限名
    private String url; // 权限的资源路径
}