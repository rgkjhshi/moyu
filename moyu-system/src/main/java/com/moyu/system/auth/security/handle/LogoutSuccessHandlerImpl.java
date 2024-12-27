package com.moyu.system.auth.security.handle;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyu.common.model.BaseResponse;
import com.moyu.system.auth.model.LoginUserDetails;
import com.moyu.system.auth.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author shisong
 * @since 2024-12-27
 */
@Slf4j
@Service
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUserDetails loginUser = tokenService.getLoginUser(request);
        if (loginUser != null) {
            // 删除用户缓存记录
            tokenService.removeLoginUser(loginUser.getToken());
            // 记录用户退出日志
            log.info("退出登录:{}", loginUser.getUsername());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(new ObjectMapper().writeValueAsString(BaseResponse.getSuccessResponse("退出成功")));
    }
}