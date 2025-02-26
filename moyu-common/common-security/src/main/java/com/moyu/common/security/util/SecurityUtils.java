package com.moyu.common.security.util;


import com.moyu.common.exception.BaseException;
import com.moyu.common.security.constant.SecurityConstants;
import com.moyu.common.security.model.LoginUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * SpringSecurity安全服务工具类
 *
 * @author shisong
 * @since 2025-01-06
 */
public class SecurityUtils {

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BaseException(HttpStatus.UNAUTHORIZED.value(), "获取用户信息异常");
        }
    }

    /**
     * 获取用户角色集合
     */
    public static Set<String> getRoles() {
        return getLoginUser().getRoles();
    }

    /**
     * 获取用户权限集合
     */
    public static Set<String> getPerms() {
        return getLoginUser().getPerms();
    }

    /**
     * 获取用户角色集合
     */
    public static Set<String> getAuthorities() {
        Collection<GrantedAuthority> authorities = getLoginUser().getAuthorities();
        return authorities == null ? new HashSet<>() : AuthorityUtils.authorityListToSet(authorities);
    }

    /**
     * 是否为root超级管理员
     */
    public static boolean isRoot() {
        return getRoles().contains(SecurityConstants.ROOT_ROLE_CODE);
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     未加密的原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配，true表示相同
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
