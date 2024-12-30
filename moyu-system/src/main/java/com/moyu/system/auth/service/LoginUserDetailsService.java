package com.moyu.system.auth.service;


import com.moyu.system.auth.model.LoginUserDetails;
import com.moyu.system.sys.enums.StatusEnum;
import com.moyu.system.sys.model.entity.SysUser;
import com.moyu.system.sys.model.param.SysUserParam;
import com.moyu.system.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户信息加载服务的自定义实现类
 * Spring Security权限认证时(AuthenticationProvider.authenticate)会调用UserDetailsService.loadUserByUsername
 *
 * @author shisong
 * @since 2024-12-27
 */
@Slf4j
@Service
public class LoginUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    /**
     * SpringSecurity权限认证时(AuthenticationProvider.authenticate)会调用此方法
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("加载{}的用户信息", username);
        // 这里应该通过远程调用获取用户信息
        SysUser sysUser = sysUserService.detail(SysUserParam.builder().account(username).build());
        if (sysUser == null) {
            log.info("登录用户:{}不存在", username);
            throw new UsernameNotFoundException("用户不存在");
        } else if (sysUser.getDeleteFlag() == 1) {
            log.info("登录用户:{}已被删除", username);
            throw new UsernameNotFoundException("用户不存在");
        } else if (Objects.equals(sysUser.getStatus(), StatusEnum.DISABLE.getCode())) {
            log.info("登录用户:{}已被停用", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        // 创建 UserDetails
        String password = new BCryptPasswordEncoder().encode("admin");
        return LoginUserDetails.withSysUser(sysUser).build();
    }
}