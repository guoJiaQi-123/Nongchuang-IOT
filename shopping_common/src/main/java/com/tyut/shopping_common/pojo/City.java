package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 城市
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City implements Serializable{
    @TableId
    private String id; // 城市id
    private String city; // 城市名
    private String provinceId; // 省份id
}