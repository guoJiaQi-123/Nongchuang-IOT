package com.tyut.shopping_manager_api.security.config;

import com.tyut.shopping_manager_api.security.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @version v1.0
 * @author OldGj 2024/6/4
 * @apiNote security配置类
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 自定义表单登录
        httpSecurity.formLogin(
                form -> {
                    form.usernameParameter("username") // 用户名
                            .passwordParameter("password") // 密码
                            .loginProcessingUrl("/admin/login") // 请求路径
                            .successHandler(new MyLoginSuccessHandler()) // 登录成功处理器
                            .failureHandler(new MyLoginFailureHandler()); // 登录失败处理器
                }
        );

        // 权限拦截配置
        httpSecurity.authorizeHttpRequests(
                resp -> {
                    resp.requestMatchers("/login", "/admin/login").permitAll(); // 登录请求不需要认证
                    resp.anyRequest().authenticated();// 其余请求都需要认证
                }
        );


        // 退出登录配置
        httpSecurity.logout(
                logout -> {
                    logout.logoutUrl("/admin/logout") // 注销的路径
                            .logoutSuccessHandler(new MyLogoutSuccessHandler())// 登出成功处理器
                            .clearAuthentication(true)// 清除认证数据
                            .invalidateHttpSession(true); // 清除session
                }
        );

        // 异常处理器
        httpSecurity.exceptionHandling(
                exception -> {
                    exception.accessDeniedHandler(new MyAccessDeniedHandler()) // 权限不足处理器
                            .authenticationEntryPoint(new MyAuthenticationEntryPoint()); // 未登录处理器
                }
        );

        // 跨域访问
        httpSecurity.cors();

        // 关闭csrf防护
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    /**
     * 注入加密工具
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
