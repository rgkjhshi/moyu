package com.dodoyd.moyu.admin.service;

import com.dodoyd.moyu.admin.model.request.GenCodeRequest;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.common.model.PageResult;

import java.util.Map;

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
    PageResult<TableInfo> queryAllTableList(GenCodeRequest request);

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

    /**
     * 生成代码zip包
     */
    byte[] downloadCodeBySql(String sql);

}
