package com.tyut.shopping_manager_api.security.handler;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 登录失败处理器
 */
public class MyLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        BaseResult<Object> result = BaseResult.builder()
                .code(402)
                .message("用户名或密码错误")
                .build();
        response.setContentType("text/json;charset=utf-8");
        String jsonString = JSON.toJSONString(result);
        response.getWriter().write(jsonString);
    }
}
