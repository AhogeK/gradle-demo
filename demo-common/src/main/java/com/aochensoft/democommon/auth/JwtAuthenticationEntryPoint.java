package com.aochensoft.democommon.auth;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.aochensoft.democommon.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 10:18:09
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 这是响应体是json格式的
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        // LocalDateTime 转 yyyy-MM-dd HH:mm:ss
        JSONConfig jsonConfig = JSONConfig.create().setDateFormat("yyyy-MM-dd HH:mm:ss");
        response.getWriter().write(JSONUtil.toJsonStr(new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED,
                "认证失败"), jsonConfig));
    }
}
