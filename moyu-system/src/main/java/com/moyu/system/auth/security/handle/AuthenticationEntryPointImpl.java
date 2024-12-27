package com.moyu.system.auth.security.handle;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyu.common.model.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 自定义 token 认证失败异常
 *
 * @author shisong
 * @since 2024-12-27
 */
@Service
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 有些异常已经在LoginService中处理过了，未处理的异常将在此处处理
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        int code = HttpStatus.UNAUTHORIZED.value();
        String message = "认证失败，无法访问：" + request.getRequestURI();
        response.getWriter().print(new ObjectMapper().writeValueAsString(new BaseResponse<>(code, message)));
    }
}

