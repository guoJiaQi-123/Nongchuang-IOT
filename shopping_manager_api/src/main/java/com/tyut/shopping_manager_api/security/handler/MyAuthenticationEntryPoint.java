package com.tyut.shopping_manager_api.security.handler;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 未登录处理器
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        BaseResult<Object> result = BaseResult.builder()
                .code(401)
                .message("用户未登录")
                .build();
        response.setContentType("text/json;charset=utf-8");
        String jsonString = JSON.toJSONString(result);
        response.getWriter().write(jsonString);
    }
}
