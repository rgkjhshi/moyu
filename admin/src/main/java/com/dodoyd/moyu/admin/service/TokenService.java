package com.dodoyd.moyu.admin.service;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.dodoyd.moyu.common.exception.BaseException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shisong
 * @since 2019-02-26
 */
public interface TokenService {

    /**
     * 从http请求头中读取token
     */
    String getToken(HttpServletRequest request);

    /**
     * 创建token
     *
     * @param userId 用户userId, 将会放到token中
     * @return token
     */
    String createToken(Long userId);

    /**
     * 校验token合法性, 验证失败会抛出业务异常
     *
     * @param token 用户token
     * @return jwt解析对象
     * @throws BaseException 校验失败抛出异常
     */
    DecodedJWT verifyToken(String token) throws BaseException;

    /**
     * 校验token并返回token中的userId
     *
     * @param token 用户token
     * @return 成功返回userId, 失败返回null
     * @throws BaseException 校验失败抛出异常
     */
    Long getUserIdByToken(String token) throws BaseException;
}
