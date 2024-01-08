package com.dodoyd.moyu.admin.security.handle;

import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.service.TokenService;
import com.dodoyd.moyu.common.model.BaseResponse;
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

/**
 * @author shisong02
 * @since 2024-01-08
 */
@Slf4j
@Service
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Resource
    private TokenService tokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String token = tokenService.getToken(request);
        String username = tokenService.verifyAndGetSubject(token);
        log.info("用户{}退出登录", username);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().print(Constants.gson.toJson(BaseResponse.getSuccessResponse("退出成功")));

    }
}
