package com.dodoyd.moyu.admin;

import com.dodoyd.moyu.common.annotation.EnableCommonLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shisong
 * @since 2023-02-21
 */
@EnableCommonLog
@EnableScheduling
@SpringBootApplication
@ImportResource({"classpath:spring/root.xml"})
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootApplication.class);
        application.run(args);
    }
}
