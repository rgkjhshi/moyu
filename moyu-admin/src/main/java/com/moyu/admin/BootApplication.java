package com.moyu.admin;

import com.moyu.common.annotation.EnableCommonLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shisong
 * @since 2023-02-21
 */
@EnableCommonLog
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootApplication.class);
        application.run(args);
    }
}
