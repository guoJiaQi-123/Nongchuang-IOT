package com.tyut.shopping_manager_api.security.handler;

import com.alibaba.fastjson2.JSON;
import com.tyut.shopping_common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote 登录成功处理器
 */
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 构建结果
        BaseResult result = BaseResult.builder()
                .code(200)
                .message("登录成功")
                .build();
        response.setContentType("text/json;charset=utf-8");
        String jsonString = JSON.toJSONString(result);
        // 向前端响应结果
        response.getWriter().write(jsonString);
    }
}
