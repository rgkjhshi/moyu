package com.dodoyd.moyu.common.config;

import com.dodoyd.moyu.common.aop.LogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shisong02
 * @since 2023-05-06
 */
@Configuration
public class CommonLogConfig {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
