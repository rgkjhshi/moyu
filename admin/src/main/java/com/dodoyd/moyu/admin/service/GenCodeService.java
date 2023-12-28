package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.model.vo.TableInfo;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 生成代码服务接口
 *
 * @author shisong02
 * @since 2023-12-19
 */
public interface GenCodeService {

    /**
     * 查询所有表的列表
     */
    List<TableInfo> queryAllTableList();

    /**
     * 生成代码
     */
    Map<String, String> genCodeByTable(String tableName);

    /**
     * 解析SQL生成代码
     */
    Map<String, String> genCodeBySql(String sql);

    /**
     * 生成代码zip包
     */
    byte[] downloadCodeByTable(String tableNames);

}
