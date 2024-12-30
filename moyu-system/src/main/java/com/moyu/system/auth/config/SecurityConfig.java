package com.moyu.system.auth.config;


import cn.hutool.core.util.ObjectUtil;
import com.moyu.system.auth.constant.SecurityConstants;
import com.moyu.system.auth.security.filter.JwtTokenAuthenticationFilter;
import com.moyu.system.auth.security.handle.CustomAuthFailureHandler;
import com.moyu.system.auth.security.handle.CustomAuthSuccessHandler;
import com.moyu.system.auth.security.handle.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author shisong
 * @since 2024-12-27
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Resource
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    /**
     * 是否启用springSecurity的鉴权功能
     */
    @Value("${custom.security.enable:true}")
    private Boolean enabled;

    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 设置跨域访问可以携带cookie
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头 允许携带任何头信息
        config.addAllowedHeader("*");
        // 设置访问源请求方法 允许所有的请求方法
        config.addAllowedMethod("*");
        // 初始化cors配置源对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 给配置源对象设置过滤的参数 == > 所有的路径都要求校验是否跨域
        source.registerCorsConfiguration("/**", config);
        // 返回配置好的CorsFilter
        return new CorsFilter(source);
    }

    /**
     * Security配置
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // CSRF禁用，否则POST工具报403错误
        httpSecurity.csrf().disable();
        // 允许跨域访问
        httpSecurity.cors();

        // 禁用HTTP响应头的缓存控制，以确保敏感数据不会被缓存。默认情况下，会添加一些缓存控制头部，如no-store和private
        httpSecurity.headers().cacheControl().disable().frameOptions().sameOrigin();
        // 设置会话会话创建策略为无状态, 基于token，不使用session
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 放行白名单
        List<String> whiteList = Arrays.asList(SecurityConstants.WHITE_LIST);
        // 如果没有开启认证，则全放行
        if (ObjectUtil.notEqual(enabled, true)) {
            whiteList.add("/**");
        }
        // 白名单放行
        httpSecurity.authorizeRequests().antMatchers(whiteList.toArray(new String[0])).permitAll();
        // 静态资源放行
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/static/**", "/public/**", "/**/*.ico").permitAll();

        // 其他的都需要授权访问
        httpSecurity.authorizeRequests().anyRequest().authenticated();

        // 配置表单认证
        httpSecurity.formLogin().loginProcessingUrl("/api/login")
                .usernameParameter("username")
                .passwordParameter("password")
                // 认证成功处理类
                .successHandler(new CustomAuthSuccessHandler())
                // 认证失败处理类
                .failureHandler(new CustomAuthFailureHandler());
        // 不使用默认退出，自定义退出 httpSecurity.logout().disable();
        // 自定义注销登录处理器 logoutUrl指定了注销登录请求地址，默认路径为/logout
        httpSecurity.logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter(), JwtTokenAuthenticationFilter.class);
    }

    /**
     * 身份认证管理器配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置自定义身份认证接口进行身份认证，并使用BCryptPasswordEncoder进行密码加密。
        auth.userDetailsService(userDetailsService);//.passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * 密码编码器(修改、重置密码使用)
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
