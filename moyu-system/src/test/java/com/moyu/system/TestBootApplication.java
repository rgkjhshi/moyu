package com.moyu.system;

import com.moyu.common.annotation.EnableCommonLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shisong
 * @since 2023-02-21
 */
@EnableCommonLog
@SpringBootApplication
public class TestBootApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TestBootApplication.class);
        application.run(args);
    }
}
