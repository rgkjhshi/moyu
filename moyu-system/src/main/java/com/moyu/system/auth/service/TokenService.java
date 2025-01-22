package com.moyu.system.auth.service;


import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.system.auth.constant.SecurityConstants;
import com.moyu.system.auth.model.LoginUserDetails;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

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
     * JWT header 是用来描述JWT元数据的JSON对象，包括两部分
     * 1. alg - 签名使用的算法
     * 2. typ - 表示令牌的类型，在JWT令牌统一写为JWT
     */
    private static final JWSHeader HEADER = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

    /**
     * 获取用户身份信息
     *
     * @return 用户凭证
     */
    public LoginUserDetails getLoginUser(JWTClaimsSet jwt) {
        LoginUserDetails user = null;
        // 获取请求携带的令牌
        if (ObjectUtil.isNotEmpty(jwt)) {
            // 从 redis 中获取用户信息
            String jwtId = jwt.getJWTID();
            String userKey = getUserKey(jwtId);
            // 从缓存中获取用户(通过userKey) TODO
//            user = redisCache.getCacheObject(userKey);
            user = LoginUserDetails.builder().username(jwt.getSubject()).authorities(AuthorityUtils.NO_AUTHORITIES).build();
        }
        return user;
    }


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
            JWTClaimsSet jwt = verifyToken(token);
            // 从 redis 中获取用户信息
            String jwtId = jwt.getJWTID();
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
     * 创建jwtToken
     */
    public static String createToken(LoginUserDetails loginUser) {
        DateTime now = DateTime.now();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                // 主题，即username
                .subject(loginUser.getUsername())
                // 签发时间
                .issueTime(now.toDate())
                // 过期时间
                .expirationTime(now.plusSeconds(SecurityConstants.Token.TOKEN_VALID_TIME).toDate())
                .jwtID(IdUtil.fastSimpleUUID())
                // 自定义声明
                .claim("claim", true)
                .build();
        return createToken(claimsSet);
    }

    /**
     * 创建JwtToken
     * JWT payload 是JWT的主体内容部分,也是一个JSON对象,包含需要传递的数据,在JWT中默认有七个字段供选择
     * 各个字段含义参考 <a href="http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html">这里</a>
     * <p>
     * 预定义字段并不要求强制使用,我们可以自定义私有字段,例如将包含用户信息的数据放到 payload 中
     */
    public static String createToken(String username) {
        DateTime now = DateTime.now();
        // 声明
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                // 主题，即username
                .subject(username)
                // 签发时间
                .issueTime(now.toDate())
                // 过期时间
                .expirationTime(now.plusSeconds(SecurityConstants.Token.TOKEN_VALID_TIME).toDate())
                .jwtID(IdUtil.fastSimpleUUID())
                .build();
        return createToken(claimsSet);
    }

    /**
     * 生成jwtToken
     */
    private static String createToken(JWTClaimsSet claimsSet) {
        String token;
        try {
            // 签名器
            JWSSigner jwsSigner = new MACSigner(SecurityConstants.Token.SECRET);
            // 创建jwt对象
            SignedJWT signedJWT = new SignedJWT(HEADER, claimsSet);
            // 签名，根据header和payload生成签名
            signedJWT.sign(jwsSigner);
            // 生成token字符串
            token = signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("生成JWT失败", e);
            throw new BaseException(ExceptionEnum.BUSINESS_ERROR, "生成jwtToken失败");
        }
        return token;
    }

    /**
     * 验证token并返回解密后的jwtClaims
     */
    public static JWTClaimsSet verifyToken(String token) throws BaseException {
        JWTClaimsSet claimsSet;
        try {
            // 验证器
            JWSVerifier jwsVerifier = new MACVerifier(SecurityConstants.Token.SECRET);
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(jwsVerifier)) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "token签名不合法！");
            }
            claimsSet = jwt.getJWTClaimsSet();
        } catch (Exception e) {
            log.error("token校验失败", e);
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "登录信息有误");
        }
        // 校验过期时间
        if (Objects.nonNull(claimsSet.getExpirationTime()) && claimsSet.getExpirationTime().after(new Date())) {
            throw new BaseException(SecurityConstants.Token.EXPIRED_ERROR_CODE, "token已失效, 请重新登录");
        }
        return claimsSet;
    }

    // 从 JWT 中解析 Claims
    public static JWTClaimsSet parseToken(String token) throws ParseException {
        SignedJWT jwt = SignedJWT.parse(token);
        return jwt.getJWTClaimsSet();
    }
}
