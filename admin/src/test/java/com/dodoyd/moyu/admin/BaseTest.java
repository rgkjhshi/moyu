package com.dodoyd.moyu.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = BootApplication.class)
public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @Test
    public void test() {
        log.info("测试基累正确执行，不要改");
    }
}
