package com.moyu.common.security.util;


import cn.hutool.core.util.IdUtil;
import com.moyu.common.enums.ExceptionEnum;
import com.moyu.common.exception.BaseException;
import com.moyu.common.security.model.LoginUserDetails;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

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
     * JWT header 是用来描述JWT元数据的JSON对象，包括两部分
     * 1. alg - 签名使用的算法
     * 2. typ - 表示令牌的类型，在JWT令牌统一写为JWT
     */
    private static final JWSHeader HEADER = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();

    /**
     * 生成token
     */
    String generateToken(LoginUserDetails loginUser) {

        return null;
    }

    /**
     * 从http请求中获取token
     */
    public static LoginUserDetails getLoginUserFromToken(String token) {
        // 校验token，错误则抛异常

        // 根据token获取claims

        // 获取登录用户(从jwt中或者缓存中)

        // 转换成登录用户
        LoginUserDetails loginUser = LoginUserDetails.builder().build();
        // 用户存在, 无痛刷新缓存，在登录过期前活动的用户自动刷新缓存时间

        // 返回当前登陆用户
        return loginUser;
    }

    public static boolean checkToken(String token) {
        return true;
    }


    /**
     * 创建jwtToken
     */
    public static String createToken(LoginUserDetails loginUser) {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                // 主题，即username
                .subject(loginUser.getUsername())
                .jwtID(IdUtil.fastSimpleUUID())
                // 自定义声明
                .claim("claim", true)
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
            JWSSigner jwsSigner = new MACSigner(SECRET);
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
            JWSVerifier jwsVerifier = new MACVerifier(SECRET);
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(jwsVerifier)) {
                throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "token签名不合法！");
            }
            claimsSet = jwt.getJWTClaimsSet();
        } catch (Exception e) {
            log.error("token解析失败", e);
            throw new BaseException(ExceptionEnum.BUSINESS_ERROR, "token解析失败");
        }
        // 校验过期时间
        if (isExpired(token)) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "token已失效, 请重新登录");
        }
        return claimsSet;
    }

    // 从 JWT 中解析 Claims
    public static JWTClaimsSet parseToken(String token) {
        JWTClaimsSet claims;
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            claims = jwt.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new BaseException(ExceptionEnum.BUSINESS_ERROR, "token解析失败");
        }
        return claims;
    }

    // 检查 JWT 是否过期
    private static boolean isExpired(String token) {
        JWTClaimsSet claims = parseToken(token);
        return Objects.nonNull(claims) && Objects.nonNull(claims.getExpirationTime()) && claims.getExpirationTime().before(new Date());
    }
}
