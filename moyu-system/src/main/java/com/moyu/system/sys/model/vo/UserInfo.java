package com.moyu.system.sys.model.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 当前登陆用户信息视图对象
 *
 * @author shisong
 * @since 2025-01-07
 */
@Getter
@Setter
@ToString
@Builder
public class UserInfo {

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户角色集合 ["root","admin"]
     */
    private Set<String> roles;

    /**
     * 用户权限标识集合
     */
    private Set<String> perms;
}
