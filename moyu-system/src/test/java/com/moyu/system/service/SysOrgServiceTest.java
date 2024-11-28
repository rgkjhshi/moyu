package com.moyu.system.service;

import com.moyu.system.BaseTest;
import com.moyu.system.sys.model.entity.SysOrg;
import com.moyu.system.sys.service.SysOrgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @author shisong
 * @since 2024-11-26
 */
@Slf4j
class SysOrgServiceTest extends BaseTest {

    @Resource
    private SysOrgService sysOrgService;


    @Test
    public void testSelect() {
        SysOrg org = sysOrgService.getById(1);
        log.info("查询结果:{}", org);
    }

}