package com.dodoyd.moyu.admin.security.filter;

import com.dodoyd.moyu.admin.security.service.CustomUserDetailsService;
import com.dodoyd.moyu.admin.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

/**
 * JWT令牌认证过滤器，用于从请求头中获取令牌并进行验证。
 *
 * @author shisong02
 * @link <a href="https://mikechen.cc/30272.html">Security工作流程参考这里</a>
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
}
