package com.moyu.system.auth.constant;


import com.auth0.jwt.algorithms.Algorithm;

/**
 * 系统常量
 *
 * @author shisong
 * @since 2024-12-27
 */
public interface SecurityConstants {
    /**
     * token相关常量
     */
    class Token {
        /**
         * token在header中的标识(Authorization)
         */
        public static final String HEADER = "X-Token";
        /**
         * token令牌前缀
         */
        public static final String PREFIX = "Bearer ";
        /**
         * jwt秘钥
         */
        public static final String SECRET = "jwt_secret";
        /**
         * JWT签名算法
         */
        public static final Algorithm SIGNATURE_ALGORITHM = Algorithm.HMAC512(SECRET);
        /**
         * JWT token有效期(100天)
         */
        public static final int TOKEN_VALID_TIME = 60 * 60 * 24 * 100;
        /**
         * token过期的错误码
         */
        public static final int EXPIRED_ERROR_CODE = 5002;
    }

    /**
     * 无需校验的URL白名单
     */
    String[] WHITE_LIST = {
            // 登陆注册类请求
            "/api/login",
            "/api/register",
            "/api/captcha/**",

            // 监控管理请求
            "/actuator/**",
            "/test/**",
            "/monitor/**",

            // 回调类
            "/callback/**",

            // 远程调用
            "/feign/**",
    };

}
