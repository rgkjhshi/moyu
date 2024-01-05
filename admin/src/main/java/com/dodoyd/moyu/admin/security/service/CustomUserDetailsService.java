package com.dodoyd.moyu.admin.security.service;

import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("用户验证:{}", username);
        // 从数据库获取用户
        SysUser sysUser = sysUserService.querySysUserByUsername(username);

        // 获取授权列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 通过用户获取角色
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(authority);
        // Load user from database or other data source
        String password = new BCryptPasswordEncoder().encode("admin");
        LoginUser loginUser = new LoginUser(username, password, authorities);
        // Create UserDetails object
        return loginUser;
    }
}
