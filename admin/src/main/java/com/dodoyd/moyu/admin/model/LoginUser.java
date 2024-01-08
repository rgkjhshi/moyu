package com.dodoyd.moyu.admin.model;

import com.dodoyd.moyu.admin.domain.SysUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

/**
 * @author shisong02
 * @since 2024-01-04
 */
@Getter
@Setter
@ToString
public class LoginUser extends User {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户账号信息
     */
    private SysUser sysUser;

    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
