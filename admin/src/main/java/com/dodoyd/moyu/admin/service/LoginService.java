package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.exception.ParameterErrorException;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 登录服务实现类
 *
 * @author shisong02
 * @since 2024-01-03
 */
@Service
public class LoginService {

    @Resource
    private TokenService tokenService;

    public String login(String username, String password) {
        // 参数校验
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new ParameterErrorException("用户名或密码错误");
        }
        // 验证码

        // Perform login logic
        return tokenService.createToken(1L);
    }
}
