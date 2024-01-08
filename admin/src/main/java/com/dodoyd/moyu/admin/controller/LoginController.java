package com.dodoyd.moyu.admin.controller;


import com.dodoyd.moyu.admin.model.dto.UserInfoDTO;
import com.dodoyd.moyu.admin.model.request.LoginRequest;
import com.dodoyd.moyu.admin.service.LoginService;
import com.dodoyd.moyu.admin.service.TokenService;
import com.dodoyd.moyu.common.model.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户信息处理器
 *
 * @author shisong
 * @since 2019-12-25
 */
@RestController
@RequestMapping(value = "/api")
public class LoginController {

    @Resource
    private LoginService loginService;

    @Resource
    private TokenService tokenService;

    /**
     * 登录
     * 不使用httpSecurity.formLogin(),自行处理登录请求
     */
    @PostMapping(value = "/login")
    public BaseResponse<String> login(LoginRequest request) {
        // 获取userInfo后生成用户token
        String token = loginService.login(request.getUsername(), request.getPassword());
        return BaseResponse.getSuccessResponse(token);
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/logout")
    public BaseResponse<?> logout() {
        return BaseResponse.getSuccessResponse("成功");
    }


    /**
     * 获取userInfo
     */
    @GetMapping(value = "/info")
    public BaseResponse<UserInfoDTO> userInfo(String token) {
        return BaseResponse.getSuccessResponse(loginService.getUserInfo());
    }

}
