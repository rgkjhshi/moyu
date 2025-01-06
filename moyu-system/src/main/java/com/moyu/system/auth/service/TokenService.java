package com.moyu.system.auth.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.system.auth.constant.SecurityConstants;
import com.moyu.system.auth.model.LoginUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
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
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";


    /**
     * 获取用户身份信息
     *
     * @return 用户凭证
     */
    public LoginUserDetails getLoginUser(HttpServletRequest request) {
        LoginUserDetails user = null;
        // 获取请求携带的令牌
        String token = getToken(request);
        if (ObjectUtil.isNotEmpty(token)) {
            DecodedJWT jwt = verifyToken(token);
            // 从 redis 中获取用户信息
            String jwtId = jwt.getId();
            String userKey = getUserKey(jwtId);
            // 从缓存中获取用户(通过userKey) TODO
//            user = redisCache.getCacheObject(userKey);
            user = LoginUserDetails.builder().build();
        }
        return user;
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
            String userKey = getUserKey(token);
            // TODO 从redis中删除token
        }
    }

    /**
     * 从http请求中获取token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.Token.HEADER);
        if (ObjectUtil.isNotEmpty(token) && token.startsWith(SecurityConstants.Token.PREFIX)) {
            token = token.replace(SecurityConstants.Token.PREFIX, "");
        }
        return token;
    }

    private String getUserKey(String uuid) {
        return LOGIN_TOKEN_KEY + uuid;
    }

    /**
     * 创建jwtToken TODO
     */
    public String createToken(LoginUserDetails loginUser) {
        String token = "";
        DateTime now = DateTime.now();
        // 各个字段含义参考 http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
        token = JWT.create()
                // 主题,即username
                .withSubject(loginUser.getUsername())
                // 签发时间
                .withIssuedAt(now.toDate())
                // 过期时间
                .withExpiresAt(now.plusSeconds(SecurityConstants.Token.TOKEN_VALID_TIME).toDate())
                .withJWTId(IdUtil.fastUUID())
                .withClaim("userId", loginUser.getUsername())
                .sign(SecurityConstants.Token.SIGNATURE_ALGORITHM);
        return token;
    }

    /**
     * 创建JwtToken
     * JWT header 是用来描述JWT元数据的JSON对象，包括两部分
     * 1. alg - 签名使用的算法
     * 2. typ - 表示令牌的类型，在JWT令牌统一写为JWT
     * JWT payload 是JWT的主体内容部分,也是一个JSON对象,包含需要传递的数据,在JWT中默认有一下七个字段供选择
     * 这七个预定义字段并不要求强制使用,并且除以上默认字段外,我们还可以自定义私有字段,例如将包含用户信息的数据放到 payload 中
     * 1. iss - 发行人
     * 2. sub - 主题
     * 3. aud - 用户
     * 4. iat - JWT的签发时间
     * 5. exp - JWT的过期时间
     * 6. jti - JWT的唯一标识
     * 7. nbf - 在此之前不可用
     */
    public static String createToken(String username) {
        DateTime now = DateTime.now();
        // 各个字段含义参考 http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
        String token = JWT.create()
                .withSubject(username)
                .withIssuedAt(now.toDate())
                .withExpiresAt(now.plusSeconds(SecurityConstants.Token.TOKEN_VALID_TIME).toDate())
                .withJWTId(IdUtil.fastUUID())
                .sign(SecurityConstants.Token.SIGNATURE_ALGORITHM);
        return token;
    }

    /**
     * 验证token并返回解密后的token
     */
    public static DecodedJWT verifyToken(String token) throws BaseException {
        JWTVerifier verifier = JWT.require(SecurityConstants.Token.SIGNATURE_ALGORITHM).build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new BaseException(SecurityConstants.Token.EXPIRED_ERROR_CODE, "token已失效, 请重新登录");
        } catch (Exception e) {
            log.error("token校验失败", e);
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "登录信息有误");
        }
        return jwt;
    }

    public Long verifyAndGetUserId(String token) throws BaseException {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("userId").asLong();
    }

    public String verifyAndGetSubject(String token) throws BaseException {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getSubject();
    }
}
