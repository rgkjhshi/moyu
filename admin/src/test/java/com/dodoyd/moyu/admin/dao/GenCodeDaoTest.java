package com.dodoyd.moyu.admin.dao;

import com.dodoyd.moyu.admin.BaseTest;
import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.model.vo.ColumnInfo;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
class GenCodeDaoTest extends BaseTest {

    @Resource
    private GenCodeDao genCodeDao;

    @Test
    void selectAllTableList() {
        List<TableInfo> list = genCodeDao.selectAllTableList();
        log.info(Constants.gson.toJson(list));
    }

    @Test
    void selectTableListByNameList() {
        List<String> nameList = Lists.newArrayList("mt_platform_info", "mt_tab_info", "mt_status_info");
        List<TableInfo> list = genCodeDao.selectTableListByNameList(nameList);
        log.info(Constants.gson.toJson(list));
    }

    @Test
    void selectTableByName() {
        TableInfo table = genCodeDao.selectTableByName("mt_tab_info");
        log.info(Constants.gson.toJson(table));
    }

    @Test
    void selectColumnListByTableName() {
        List<ColumnInfo> columnList = genCodeDao.selectColumnListByTableName("mt_tab_info");
        log.info(Constants.gson.toJson(columnList));
    }

    @Test
    void selectPkColumnListByTableName() {
        List<ColumnInfo> columnList = genCodeDao.selectPkColumnListByTableName("sys_user");
        log.info(Constants.gson.toJson(columnList));
    }
}
