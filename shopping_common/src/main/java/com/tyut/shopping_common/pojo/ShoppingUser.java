package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商城用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingUser implements Serializable {
    @TableId
    private Long id; // 用户id
    private String username; // 用户名
    private String password; // 用户密码
    private String phone; // 手机号
    private String nickName; // 昵称
    private String name; // 真实姓名
    private String headPic; // 头像地址
    private String sex; // 性别
    private String status; // 用户状态（Y正常 N非正常）
}