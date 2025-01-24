package com.moyu.system.auth.config;


import com.moyu.system.auth.security.handle.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * @author shisong
 * @since 2024-12-27
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    /**
     * WebSecurity配置
     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
//    }

    /**
     * HttpSecurity配置
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

        // 自定义注销登录处理器 logoutUrl指定了注销登录请求地址，默认路径为/logout
        httpSecurity.logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutSuccessHandler);

//        // 异常处理
//        httpSecurity.exceptionHandling()
//                // 认证异常处理，未认证访问的情况处理(不设置默认处理端点为：LoginUrlAuthenticationEntryPoint("/login"))
//                .authenticationEntryPoint(new AuthExceptionEntryPoint())
//                // 授权异常处理，访问权限不足时的处理
//                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    /**
     * 身份认证管理器配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置自定义身份认证接口进行身份认证，并使用BCryptPasswordEncoder进行密码加密。
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * 密码编码器(修改、重置密码使用)
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 暴露 AuthenticationManager 供使用
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
