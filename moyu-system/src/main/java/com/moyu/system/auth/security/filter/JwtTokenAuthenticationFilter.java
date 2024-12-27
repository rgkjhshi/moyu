package com.moyu.system.auth.security.filter;

import cn.hutool.core.util.ObjectUtil;
import com.moyu.system.auth.model.LoginUserDetails;
import com.moyu.system.auth.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头中获取token进而获取用户信息
        LoginUserDetails userDetails = tokenService.getLoginUser(request);
        // 如果 loginUser 存在，进行验证并设置SecurityContext
//        if (ObjectUtil.isNotNull(userDetails) && ObjectUtil.isEmpty(SecurityUtils.getAuthentication())) {
        if (ObjectUtil.isNotNull(userDetails)) {
            // 验证token
            tokenService.verifyToken(userDetails);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 继续执行后续的过滤器链
        filterChain.doFilter(request, response);
    }
}
