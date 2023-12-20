package com.dodoyd.moyu.admin;

import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试基础类
 *
 * @author song.shi
 * @since 2018-01-17
 */
public class UnitTest {
    private static final Logger log = LoggerFactory.getLogger(UnitTest.class);

    @Test
    public void test() {
        log.info("测试基累正确执行，不要改");
    }

    @Test
    public void testCamelCase() {
        String underscoreName = "Hello_world_DTO";
        String camelCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreName);
        log.info(camelCaseName);
    }

    @Test
    public void testGson() {
        String path = "/pages/activity/index/index";
        String query = "activityNo=" + "Abc1234";

        // 将请求体转换为 JSON 字符串
        Map<String, String> objMap = new HashMap<>(4);
        objMap.put("path", path);
        objMap.put("query", query);
        Map<String, Object> bodyMap = new HashMap<>(4);
        bodyMap.put("jump_wxa", objMap);
        Gson htmlGson = new GsonBuilder().disableHtmlEscaping().create();
        String requestBody = htmlGson.toJson(bodyMap);
        log.info(requestBody);
    }
}
