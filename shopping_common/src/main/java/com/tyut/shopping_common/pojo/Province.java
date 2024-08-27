package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 省份
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province implements Serializable{
    @TableId
    private String id; // 省份id
    private String provinceName; // 省份名
}