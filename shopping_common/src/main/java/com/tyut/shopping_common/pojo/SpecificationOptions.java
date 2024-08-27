package com.tyut.shopping_common.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品规格项集合，用于新增规格项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationOptions implements Serializable{
    private Long specId; // 规格id
    private String[] optionName; // 规格项名数组
}