package com.moyu.common.security.util;


import cn.hutool.core.util.IdUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import com.moyu.common.security.model.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * JWT工具类
 *
 * @author shisong
 * @since 2025-01-23
 */
@Slf4j
public class JwtUtils {
    /**
     * jwt秘钥，至少256-bit(32-byte)，如：7nPXLm0zLVdqKM5QTb03ahcRiWzoC2UC
     * RandomUtil.randomString(32)
     */
    public static final String SECRET = "7nPXLm0zLVdqKM5QTb03ahcRiWzoC2UC";

    /**
     * 生成token
     * 参考 <a href="https://hutool.cn/docs/#/jwt/%E6%A6%82%E8%BF%B0">这里</a>
     */
    public static String createToken(LoginUserDetails loginUser) {
        String token = JWT.create()
                .setSubject(loginUser.getUsername())
                .setJWTId(IdUtil.fastSimpleUUID())
                .setPayload("perms", loginUser.getPerms())
                .setKey(SECRET.getBytes())
                .sign();
        return token;
    }

    /**
     * 从http请求中获取token
     */
    public static LoginUserDetails getLoginUserFromToken(String token) {
        // 解析jwt token
        JWT jwt = JWT.of(token);
        // 根据token获取claims
        String username = (String) jwt.getPayload(JWTPayload.SUBJECT);
        List<String> perms = (List<String>) jwt.getPayload("perms");
        // 转换成登录用户
        LoginUserDetails loginUser = LoginUserDetails.builder().enabled(true).username(username).build();
        loginUser.setAuthorities(perms);
        // 返回当前登陆用户
        return loginUser;
    }

    /**
     * 仅验证签名是否有效
     */
    public static String getId(String token) {
        return (String) JWT.of(token).getPayload(JWTPayload.JWT_ID);
    }

    /**
     * 仅验证签名是否有效
     */
    public static boolean verifyToken(String token) {
        // 默认验证HS265的算法
        return JWT.of(token).setKey(SECRET.getBytes()).verify();
    }

    /**
     * 验证签名和时间，一般时间线是：(签发时间)---(生效时间)---(当前时间)---(失效时间)
     */
    public static boolean checkToken(String token) {
        return JWT.of(token).setKey(SECRET.getBytes()).validate(0);
    }
}
