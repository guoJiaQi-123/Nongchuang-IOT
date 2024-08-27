package com.tyut.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回状态码枚举类
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {

    /**
     * 正常
     */
    SUCCESS(200, "OK"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),
    /**
     * 业务异常
     */
    PARAMETER_ERROR(601, "参数异常"),
    /**
     * 添加商品类型异常
     */
    INSERT_PRODUCT_TYPE_ERROR(602, "3级目录不能添加子目录"),
    /**
     * 删除商品类型异常
     */
    DELETE_PRODUCT_TYPE_ERROR(603, "当前类型存在子类型，无法删除"),
    /**
     * 上传文件异常
     */
    FILE_UPLOAD_ERROR(604, "上传文件异常"),
    /**
     * 注册验证码异常
     */
    REGISTER_CODE_ERROR(605, "注册验证码错误"),
    /**
     * 注册用户名重复
     */
    REGISTER_REPEAT_PHONE_ERROR(606, "注册手机号重复"),
    /**
     * 注册手机号重复
     */
    REGISTER_REPEAT_NAME_ERROR(607, "注册用户名重复"),
    /**
     * 登录失败异常：用户名或密码错误
     */
    LOGIN_NAME_PASSWORD_ERROR(608, "用户名或密码错误"),
    /**
     * 验证码登录失败
     */
    LOGIN_CHECK_CODE_ERROR(609, "验证码错误"),
    /**
     * 用户手机号不存在异常
     */
    LOGIN_NOT_PHONE_ERROR(610, "用户手机号不存在"),

    /**
     * 支付宝支付异常
     */
    QR_CODE_ERROR(612, "生成支付宝支付二维码异常"),
    /**
     * 验签失败，抛出异常
     */
    CHECK_SIGN_ERROR(613, "支付宝支付异常"),
    /**
     * 判断订单状态，未支付才会生成二维码
     */
    ORDER_STATUS_ERROR(614, "订单状态异常"),
    /**
     * 商品库存异常
     */
    NO_STOCK_ERROR(615, "商品库存异常"),
    /**
     * 订单不存在或已过期异常
     */
    ORDER_EXPIRED_ERROR(616,"订单不存在或已过期"),
    /**
     * 用户状态异常
     */
    LOGIN_STATE_ERROR(611, "用户状态异常");


    private final Integer code;

    private final String message;

}
