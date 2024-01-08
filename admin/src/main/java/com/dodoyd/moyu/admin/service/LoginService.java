package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.exception.ParameterErrorException;
import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.model.dto.UserInfoDTO;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录服务实现类
 *
 * @author shisong02
 * @since 2024-01-03
 */
@Slf4j
@Service
public class LoginService {

    @Resource
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 用户登录并返回token
     * 使用security配置表单登录处理器formLogin().loginProcessingUrl()可自动进行登录校验，使用此方法登录则不可设置表单登录处理器
     *
     * @link <a href="https://blog.csdn.net/crazymakercircle/article/details/130276558">参考自定义登录部分</a>
     */
    public String login(String username, String password) {
        // 验证码
        // 参数校验
        if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
            throw new ParameterErrorException("登录失败，用户名或密码错误");
        }
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            // 该方法会去调用UserDetailsService.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                // 用户信息不匹配
                throw new ParameterErrorException("登录失败，用户名或密码错误");
            } else {
                log.error("用户{}登录失败", username, e);
                throw new ParameterErrorException("登录失败");
            }
        }
        // 通过当前的认证情况获取用户，如果没有认证，那么就是用户名，如果认证了，返回UserDetails
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 记录登录信息
        log.info("用户登录:{}", loginUser.getUsername());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    public UserInfoDTO getUserInfo() {
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(loginUser.getUserId());
        dto.setRoles(getRoles(loginUser));
        dto.setNickname(loginUser.getSysUser().getNickName());
        dto.setAvatar(loginUser.getSysUser().getAvatar());
        dto.setGender(loginUser.getSysUser().getGender());
        return dto;
    }

    private List<String> getRoles(LoginUser loginUser) {
        List<String> roles = new ArrayList<>();
        if (CollectionUtils.isEmpty(loginUser.getAuthorities())) {
            return roles;
        }
        loginUser.getAuthorities().forEach(e -> roles.add(e.getAuthority()));
        return roles;
    }
}
