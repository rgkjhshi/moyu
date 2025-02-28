package com.moyu.system;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.google.common.base.CaseFormat;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 本地测试类
 *
 * @author shisong
 * @since 2024-11-25
 */
@Slf4j
public class UnitTest {

    @Test
    public void test() {
        log.info("测试基类正确执行，不要改");
    }

    @Test
    public void testCamelCase() {
        String underscoreName = "Hello_world_DTO";
        String camelCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreName);
        log.info(camelCaseName);
    }

    @SneakyThrows
    @Test
    public void testSql() {
        String sqlStr = "code = 1";
        sqlStr = sqlStr + StringPool.AND + "username = 'xx'";
        log.info(sqlStr);
    }

}
