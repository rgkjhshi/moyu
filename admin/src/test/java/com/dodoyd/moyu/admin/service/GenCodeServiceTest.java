package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
class GenCodeServiceTest extends BaseTest {

    @Resource
    private GenCodeService genCodeService;

    @Test
    void genCode() {
        String tableName = "mt_tab_info";
        Map<String, String> map = genCodeService.genCodeByTable(tableName);
        System.out.println(map.get("Service.java"));
    }
}
