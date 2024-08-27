package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 区/县
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area implements Serializable{
    @TableId
    private String id; // 区/县Id
    private String area; // 区/县名
    private String cityId; // 城市Id

}