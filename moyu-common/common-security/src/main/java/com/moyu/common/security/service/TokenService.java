package com.moyu.common.security.service;


import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.security.model.LoginUserDetails;
import com.moyu.common.security.util.JwtUtils;
import com.nimbusds.jwt.JWTClaimsSet;

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

        return JwtUtils.generateToken(loginUser);
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
        JWTClaimsSet claims = JwtUtils.verifyToken(token);
        // 根据token获取claims
        String tokenId = claims.getJWTID();
        // 从缓存中获取登录用户 TODO
        // userCache.get(tokenId)
        LoginUserDetails loginUser = JwtUtils.getLoginUserFromToken(token);
        // 用户不存在则表示登录已过期

        // 用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间

        // 返回当前登陆用户
        return loginUser;
    }

}
