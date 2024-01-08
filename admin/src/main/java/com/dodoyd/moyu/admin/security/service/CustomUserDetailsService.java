package com.dodoyd.moyu.admin.security.service;

import com.dodoyd.moyu.admin.domain.SysRole;
import com.dodoyd.moyu.admin.domain.SysUser;
import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.service.SysRoleService;
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

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库获取用户
        SysUser sysUser = sysUserService.querySysUserByUsername(username);
        if (sysUser == null) {
            log.info("登录用户:{}不存在", username);
            throw new UsernameNotFoundException("用户不存在");
        } else if (sysUser.getDeleted() == 1) {
            log.info("登录用户:{}已被删除", username);
            throw new UsernameNotFoundException("用户不存在");
        } else if (sysUser.getStatus() == 1) {
            log.info("登录用户:{}已被停用", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        // 查询用户的权限
        List<SysRole> roleList = sysRoleService.queryUserRoleList(sysUser.getUserId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysRole sysRole : roleList) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(sysRole.getRoleKey());
            authorities.add(authority);
        }
        // 创建 UserDetails
        String password = new BCryptPasswordEncoder().encode("admin");
        LoginUser loginUser = new LoginUser(username, password, authorities);
        loginUser.setUserId(sysUser.getUserId());
        return loginUser;
    }
}
