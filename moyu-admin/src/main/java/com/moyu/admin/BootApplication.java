package com.moyu.admin;

import com.moyu.common.annotation.EnableCommonLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shisong
 * @since 2023-02-21
 */
@EnableCommonLog
@EnableScheduling
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootApplication.class);
        application.run(args);
    }
}
