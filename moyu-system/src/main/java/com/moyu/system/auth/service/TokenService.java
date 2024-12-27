package com.moyu.system.auth.service;


import cn.hutool.core.util.ObjectUtil;
import com.moyu.system.auth.model.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shisong
 * @since 2024-12-27
 */
@Slf4j
@Service
public class TokenService {

    /**
     * 令牌
     */
    public static final String TOKEN = "X-Token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";


    /**
     * 获取用户身份信息
     *
     * @return 用户凭证
     */
    public LoginUserDetails getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (ObjectUtil.isNotEmpty(token)) {
            try {
//                Claims claims = parseToken(token);
                // 解析对应的权限以及用户信息
//                String uuid = (String) claims.get(LOGIN_USER_KEY);
//                String tokenKey = getTokenKey(uuid);
                // 从缓存中获取用户(通过tokenKey) TODO
                LoginUserDetails user = LoginUserDetails.builder().build();
                return user;
            } catch (Exception e) {
                log.error("获取用户信息异常'{}'", e.getMessage());
            }
        }
        return null;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     */
    public void verifyToken(LoginUserDetails loginUser) {
//        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
    }

    /**
     * 删除用户身份信息
     */
    public void removeLoginUser(String token) {
        if (ObjectUtil.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            // TODO 从redis中删除token
        }
    }

    /**
     * 从http请求中获取token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        if (ObjectUtil.isNotEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            token = token.replace(TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return LOGIN_TOKEN_KEY + uuid;
    }
}
