package com.dodoyd.moyu.admin.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息加载服务的自定义实现类
 * Spring Security权限认证时(AuthenticationProvider.authenticate)会调用UserDetailsService.loadUserByUsername
 *
 * @author shisong02
 * @since 2024-01-04
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("用户验证:{}", username);
        // 权限
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        // Load user from database or other data source
        User user = new User("admin", "admin", authorities);
        // Create UserDetails object
        return user;
    }
}
