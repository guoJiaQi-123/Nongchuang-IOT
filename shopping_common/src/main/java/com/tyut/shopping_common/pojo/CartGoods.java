package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车商品/订单商品
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartGoods implements Serializable {
    @TableId
    private Long id;
    /**
     * 商品id
     */
    private Long goodId; //
    /**
     * 商品名称
     */
    private String goodsName; //
    /**
     * 单价
     */
    private BigDecimal price; //
    /**
     * 头图
     */
    private String headerPic; //
    /**
     * 购买数量
     */
    private Integer num; //
    /**
     * 属于的订单id
     */
    private String orderId; //
}
