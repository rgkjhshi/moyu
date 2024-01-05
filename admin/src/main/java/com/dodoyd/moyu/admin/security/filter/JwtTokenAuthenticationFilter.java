package com.dodoyd.moyu.admin.security.filter;

import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.security.service.CustomUserDetailsService;
import com.dodoyd.moyu.admin.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shisong02
 * @since 2024-01-04
 */
@Slf4j
@Service
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private TokenService tokenService;

    @Resource
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String token = tokenService.getToken(request);
        log.info("JwtToken认证");
        // 如果token存在，进行验证并设置SecurityContext
        if (token != null) {
            // 验证token并获取用户信息
            String username = tokenService.verifyAndGetSubject(token);
            // 注意：这里的验证逻辑需要你根据实际情况进行实现
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (userDetails != null) {
                // 创建Authentication并设置到SecurityContext中
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 设置认证的详细信息，如远程地址remoteAddress
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行后续的过滤器链
        filterChain.doFilter(request, response);
    }

    private UserDetails verifyToken(String token) {
        // 这里是验证token的逻辑，需要你根据实际情况进行实现
        String username = tokenService.verifyAndGetSubject(token);
        // 从数据库获取用户

        // 获取授权列表
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 通过用户获取角色
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(authority);
        LoginUser loginUser = new LoginUser(username, null, authorities);
        // Create UserDetails object
        return loginUser;
    }
}
