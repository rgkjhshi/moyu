package com.moyu.system.auth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.moyu.system.sys.model.entity.SysUser;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
public class LoginUserDetails implements UserDetails, CredentialsContainer {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(String... authorities) {
        this.authorities = AuthorityUtils.createAuthorityList(authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    public static LoginUserDetailsBuilder withSysUser(SysUser sysUser) {
        return builder().sysUser(sysUser)
                .username(sysUser.getAccount())
                .password(sysUser.getPassword())
                .enabled(sysUser.getStatus() == 0);
    }
}
