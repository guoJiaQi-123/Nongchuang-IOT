package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductType implements Serializable{
    @TableId
    private Long id; // 类型id
    private String name; // 类型名称
    private Integer level; // 类型级别，分为1,2,3级,不能超过3级。
    private Long parentId; // 上级类型id，上级类型id=0时，代表一级类目
}