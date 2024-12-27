package com.moyu.system.auth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyu.system.sys.model.entity.SysUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


/**
 * @author shisong
 * @see org.springframework.security.core.userdetails.User
 * @since 2024-12-27
 */
@Getter
@Setter
@ToString
@Builder
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户token
     */
    private String token;

    /**
     * 权限集合
     */
    private Set<String> perms;

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 默认字段
     *
     * @see org.springframework.security.core.userdetails.User
     */
    private String username;

    /**
     * 不允许序列化只允许反序列化
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    private Collection<GrantedAuthority> authorities;

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    private boolean accountNonExpired;
    /**
     * 用户是否解锁,锁定的用户无法进行身份验证
     */
    @JsonIgnore
    private boolean accountNonLocked;
    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     */
    @JsonIgnore
    private boolean credentialsNonExpired;
    /**
     * 是否可用 ,禁用的用户不能身份验证
     */
    @JsonIgnore
    private boolean enabled;
}
