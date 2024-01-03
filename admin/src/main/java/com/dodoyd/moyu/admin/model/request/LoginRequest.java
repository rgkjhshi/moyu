package com.dodoyd.moyu.admin.model.request;

import lombok.Data;

/**
 * 用户登录请求对象
 *
 * @author shisong02
 * @since 2024-01-03
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

}
