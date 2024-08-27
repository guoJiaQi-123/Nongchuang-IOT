package com.tyut.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 统一结果返回集
 */
@Data
@AllArgsConstructor
@Builder
public class BaseResult<T> implements Serializable {

    // 响应码 200 正常  非200 不正常
    private Integer code;

    // 提示信息
    private String message;

    // 返回数据
    private T data;

    /**
     * 无返回数据的成功方法
     * @return
     * @param <T>
     */
    public static <T> BaseResult<T> ok() {
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 有返回数据的成功方法
     * @return
     * @param <T>
     */
    public static <T> BaseResult<T> ok(T data) {
        return new BaseResult<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), data);
    }

}
