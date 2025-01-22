package com.moyu.system.auth.service;


import com.moyu.common.exception.BaseException;
import com.moyu.system.auth.model.LoginUserDetails;
import com.moyu.system.auth.model.param.UserLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登陆服务类
 *
 * @author shisong
 * @since 2025-01-22
 */
@Slf4j
@Service
public class LoginService {

    @Resource
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 用户登陆
     *
     * @return token
     */
    public String login(UserLoginParam param) {
        // 前面的校验

        String username = param.getAccount();
        String password = param.getPassword();

        // 验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 该方法会在去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new BaseException(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
            } else {
                throw new BaseException(HttpStatus.UNAUTHORIZED.value(), "登陆失败");
            }
        }
        LoginUserDetails loginUser = (LoginUserDetails) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }
}
