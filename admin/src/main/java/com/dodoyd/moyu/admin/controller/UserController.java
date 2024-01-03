package com.dodoyd.moyu.admin.controller;


import com.dodoyd.moyu.admin.model.dto.UserInfoDTO;
import com.dodoyd.moyu.admin.model.request.LoginRequest;
import com.dodoyd.moyu.admin.service.LoginService;
import com.dodoyd.moyu.admin.service.TokenService;
import com.dodoyd.moyu.admin.util.UserLocalUtils;
import com.dodoyd.moyu.common.model.BaseResponse;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息处理器
 *
 * @author shisong
 * @since 2019-12-25
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Resource
    private LoginService loginService;

    @Resource
    private TokenService tokenService;

    /**
     * 登录
     */
    @PostMapping(value = "/login")
    public BaseResponse<String> login(@RequestBody LoginRequest request) {
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
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(10001L);
        dto.setRoles(Lists.newArrayList("admin"));
        return BaseResponse.getSuccessResponse(dto);
    }

    /**
     * 获取userInfo
     */
    @PostMapping(value = "/getUserInfo")
    public BaseResponse<UserInfoDTO> getUserInfo() {
        Long userId = UserLocalUtils.getUserId();
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(userId);
        return BaseResponse.getSuccessResponse(dto);
    }

}
