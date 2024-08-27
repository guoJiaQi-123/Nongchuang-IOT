package com.tyut.shopping_common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 自定义业务异常类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusException extends RuntimeException{

    // 状态码
    private Integer code;
    // 错误消息
    private String msg;

    public BusException(CodeEnum codeEnum){
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMessage();
    }

}
