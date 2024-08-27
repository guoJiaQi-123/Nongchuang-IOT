package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsDesc implements Serializable {
    /**
     * 商品ID
     */
    private Long id; // 商品id
    /**
     * 商品名称
     */
    private String goodsName; // 商品名称
    /**
     * 副标题
     */
    private String caption; // 副标题
    /**
     * 商品价格
     */
    private BigDecimal price; // 价格
    /**
     * 商品头图
     */
    private String headerPic; // 头图
    /**
     * 是否上架
     */
    private Boolean isMarketable; // 是否上架
    /**
     * 商品介绍
     */
    private String introduction; // 商品介绍

    /**
     * 品牌
     */
    private Brand brand; // 品牌
    /**
     * 一级类目
     */
    private ProductType productType1; // 一级类目
    /**
     * 二级类目
     */
    private ProductType productType2;  // 二级类目
    /**
     * 三级类目
     */
    private ProductType productType3;  // 三级类目
    /**
     * 商品图片
     */
    private List<GoodsImage> images; // 商品图片
    /**
     * 商品规格
     */
    private List<Specification> specifications; // 商品规格
}