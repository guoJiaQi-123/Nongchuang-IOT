package com.tyut.shopping_manager_api.security.handler;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 权限不足处理器
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        BaseResult<Object> result = BaseResult.builder()
                .code(403)
                .message("权限不足")
                .build();
        response.setContentType("text/json;charset=utf-8");
        String jsonString = JSON.toJSONString(result);
        response.getWriter().write(jsonString);
    }
}
