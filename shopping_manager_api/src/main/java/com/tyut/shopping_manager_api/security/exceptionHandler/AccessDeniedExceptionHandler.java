package com.tyut.shopping_manager_api.security.exceptionHandler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 统一异常处理器
 */
@RestControllerAdvice
public class AccessDeniedExceptionHandler {

    // 处理权限不足异常，捕获到异常后再次抛出，交给AccessDeniedHandler处理
    @ExceptionHandler(AccessDeniedException.class)
    public void defaultExceptionHandler(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

}
