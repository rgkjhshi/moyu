package com.dodoyd.moyu.admin.security.handle;

import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.common.model.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shisong02
 * @since 2024-01-08
 */
@Service
public class ExceptionAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 有些异常已经在LoginService中处理过了，未处理的异常将在此处处理
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(Constants.gson.toJson(new BaseResponse<>(Constants.ErrorCode.TOKEN_INVALID, "认证失败，无法访问系统资源")));
    }
}
