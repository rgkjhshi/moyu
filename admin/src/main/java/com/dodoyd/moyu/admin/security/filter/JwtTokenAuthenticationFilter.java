package com.dodoyd.moyu.admin.security.filter;

import com.dodoyd.moyu.admin.model.LoginUser;
import com.dodoyd.moyu.admin.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token
        String token = tokenService.getToken(request);
        log.info("JwtToken认证");
        // 如果token存在，进行验证并设置SecurityContext
        if (token != null) {
            // 验证token并获取用户信息
            tokenService.verifyToken(token);
            // 注意：这里的验证逻辑需要你根据实际情况进行实现
            UserDetails userDetails = verifyToken(token);
            if (userDetails != null) {
                // 创建Authentication并设置到SecurityContext中
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 继续执行后续的过滤器链
        filterChain.doFilter(request, response);
    }

    private UserDetails verifyToken(String token) {
        // 这里是验证token的逻辑，需要你根据实际情况进行实现
        Long userId = tokenService.verifyAndGetUserId(token);

        return null;
    }
}
