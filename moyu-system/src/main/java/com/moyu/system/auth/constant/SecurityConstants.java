package com.moyu.system.auth.constant;


/**
 * 系统常量
 *
 * @author shisong
 * @since 2024-12-27
 */
public interface SecurityConstants {

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
