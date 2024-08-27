package com.tyut.shopping_common.result;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version v1.0
 * @author OldGj 2024/6/1
 * @apiNote 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param busException
     * @return
     */
    @ExceptionHandler({BusException.class})
    public BaseResult defaultExceptionHandler(BusException busException) {
        return BaseResult.builder()
                .code(busException.getCode()) // 业务异常响应码
                .message(busException.getMsg()) // 业务异常提示信息
                .build();
    }

    /**
     * 处理系统异常
     * @param e
     * @return
     */
    @ExceptionHandler({Exception.class})
    public BaseResult defaultExceptionHandler(Exception e) {
        e.printStackTrace();
        return BaseResult.builder()
                .code(CodeEnum.SYSTEM_ERROR.getCode()) // 系统异常响应码
                .message(CodeEnum.SYSTEM_ERROR.getMessage()) // 系统异常提示信息
                .build();
    }

}
