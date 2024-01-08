package com.dodoyd.moyu.admin.config;

import com.dodoyd.moyu.admin.security.filter.JwtTokenAuthenticationFilter;
import com.dodoyd.moyu.admin.security.handle.ExceptionAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * @author shisong02
 * @since 2024-01-04
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    @Resource
    private ExceptionAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Security配置
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // 注解标记允许匿名访问的url
//        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();
//        permitAllUrl.getUrls().forEach(url -> registry.antMatchers(url).permitAll());

        httpSecurity
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 允许跨域访问
                .cors()
                // 表单登录处理器地址
//                .and().formLogin().loginProcessingUrl("/api/login")
                // 禁用HTTP响应头的缓存控制。默认情况下，会添加一些缓存控制头部，如no-store和private，以确保敏感数据不会被缓存。
                .and().headers().cacheControl().disable()
                // 认证失败处理
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                // 设置会话会话创建策略为无状态, 基于token，不使用session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 过滤请求
                .and().authorizeRequests()
                // 对于登录login 注册register 验证码captchaImage 允许匿名访问
                .antMatchers("/api/login", "/api/register", "/api/captchaImage").permitAll()
                // 静态资源，可匿名访问
                .antMatchers(HttpMethod.GET, "/static/**", "/public/**", "/**/*.css", "/**/*.js").permitAll()
//                .antMatchers("/api/**").authenticated()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 认证管理器配置
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 设置自定义身份认证接口进行身份认证，并使用BCryptPasswordEncoder进行密码加密。
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * AuthenticationManager作为Bean声明，使用时可直接注入
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
