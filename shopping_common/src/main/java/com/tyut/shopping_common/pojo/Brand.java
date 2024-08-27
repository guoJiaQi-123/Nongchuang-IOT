package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 品牌
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brand implements Serializable{
    @TableId
    private Long id; // 品牌 id
    private String name; // 品牌名称
}