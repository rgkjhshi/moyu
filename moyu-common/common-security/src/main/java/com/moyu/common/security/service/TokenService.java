package com.moyu.common.security.service;


import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.security.model.LoginUserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shisong
 * @since 2025-01-24
 */
public class TokenService {

    /**
     * 生成token
     */
    public static String generateToken(LoginUserDetails loginUser) {

        return null;
    }


    /**
     * 从http请求中获取token
     */
    public static String getTokenFromRequest(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("Authorization");
        if (ObjectUtil.isNotEmpty(token) && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }
        return token;
    }


    /**
     * 从http请求中获取token
     */
    public static LoginUserDetails getLoginUserByToken(String token) {
        // 校验token，错误则抛异常

        // 根据token获取claims

        // 获取登录用户(从jwt中或者缓存中)

        // 用户不存在则表示登录已过期


        // 转换成登录用户
        LoginUserDetails loginUser = LoginUserDetails.builder().build();
        // 用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间

        // 返回当前登陆用户
        return loginUser;
    }


}
