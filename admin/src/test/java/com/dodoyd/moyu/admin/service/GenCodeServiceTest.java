package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.BaseTest;
import com.dodoyd.moyu.admin.constant.Constants;
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
        Map<String, String> map = genCodeService.genCode(tableName);
        System.out.println(map.get("Dao.java"));
    }
}
