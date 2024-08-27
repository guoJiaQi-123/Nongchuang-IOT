package com.tyut.shopping_common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders implements Serializable {
    /**
     * 订单编号,后台自动生成
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 购物车商品集合
     */
    @TableField(exist = false)
    private List<CartGoods> cartGoods;
    /**
     * 支付金额
     */
    private BigDecimal payment;
    /**
     * 支付方式  1、微信支付   2、支付宝支付
     */
    private Integer paymentType;
    /**
     * 邮费
     */
    private BigDecimal postFee;
    /**
     * 订单状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
     */
    private Integer status;
    /**
     * 订单创建时间
     */
    private Date createTime;
    /**
     * 付款时间
     */
    private Date paymentTime;
    /**
     * 发货时间
     */
    private Date consignTime;
    /**
     * 交易完成时间
     */
    private Date endTime;
    /**
     * 交易关闭时间
     */
    private Date closeTime;
    /**
     * 物流名称
     */
    private String shippingName;
    /**
     * 物流单号
     */
    private String shippingCode;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 买家留言
     */
    private String buyerMessage;
    /**
     * 买家昵称
     */
    private String buyerNick;
    /**
     * 收货地址
     */
    private String receiverAreaName;
    /**
     * 收货手机号
     */
    private String receiverMobile;
    /**
     * 收货邮编
     */
    private String receiverZipCode;
    /**
     * 收货人
     */
    private String receiver;
    /**
     * 订单过期时间
     */
    private Date expire;
}