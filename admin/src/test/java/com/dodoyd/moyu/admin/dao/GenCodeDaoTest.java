package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.BaseTest;
import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
class GenCodeDaoTest extends BaseTest {

    @Resource
    private GenCodeDao genCodeDao;

    @Test
    void selectAllDbTableList() {
        List<TableInfo> list = genCodeDao.selectAllDbTableList();
        log.info(Constants.gson.toJson(list));
    }
}
