package com.dodoyd.moyu.admin.model.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户信息POJO
 *
 * @author shisong02
 * @since 2023-04-27
 */
@Data
public class UserInfoDTO {
    /**
     * 用户唯一id
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别,0:未知,1:男,2:女
     */
    private Integer gender;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 角色
     */
    private List<String> roles;
}
