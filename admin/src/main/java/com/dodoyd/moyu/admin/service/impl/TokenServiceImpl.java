package com.dodoyd.moyu.admin.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.service.TokenService;
import com.dodoyd.moyu.common.enums.ExceptionEnum;
import com.dodoyd.moyu.common.exception.BaseException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shisong
 * @since 2019-02-26
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    @Override
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.Token.HEADER);
        if (!Strings.isNullOrEmpty(token) && token.startsWith(Constants.Token.PREFIX)) {
            token = token.replace(Constants.Token.PREFIX, "");
        }
        return token;
    }

    @Override
    public String createToken(LoginUser loginUser) {
        String token = "";
        DateTime now = DateTime.now();
        // 各个字段含义参考 http://www.ruanyifeng.com/blog/2018/07/json_web_token-tutorial.html
        token = JWT.create()
                // 主题,即username
                .withSubject(loginUser.getUsername())
                // 签发时间
                .withIssuedAt(DateTime.now().toDate())
                // 过期时间
                .withExpiresAt(now.plusSeconds(Constants.Token.TOKEN_VALID_TIME).toDate())
                .withClaim("userId", loginUser.getUserId())
                .sign(Constants.Token.SIGNATURE_ALGORITHM);
        return token;
    }

    @Override
    public DecodedJWT verifyToken(String token) throws BaseException {
        JWTVerifier verifier = JWT.require(Constants.Token.SIGNATURE_ALGORITHM).build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new BaseException(Constants.ErrorCode.TOKEN_EXPIRED, "token已失效, 请重新登录");
        } catch (Exception e) {
            log.error("token校验失败", e);
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "登录信息有误");
        }
        return jwt;
    }

    @Override
    public Long verifyAndGetUserId(String token) throws BaseException {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim("userId").asLong();
    }

    @Override
    public String verifyAndGetSubject(String token) throws BaseException {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getSubject();
    }
}
