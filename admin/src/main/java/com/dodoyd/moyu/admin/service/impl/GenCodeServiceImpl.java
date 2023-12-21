package com.dodoyd.moyu.admin.service.impl;

import com.dodoyd.moyu.admin.constant.GenConstants;
import com.dodoyd.moyu.admin.dao.GenCodeDao;
import com.dodoyd.moyu.admin.model.vo.ColumnInfo;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.google.common.base.CaseFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shisong02
 * @since 2023-12-19
 */

@Slf4j
@Service
public class GenCodeServiceImpl implements GenCodeService {

    @Resource
    private GenCodeDao genCodeDao;

    @Resource
    private Configuration configuration;

    @Override
    public List<TableInfo> queryAllTableList() {
        return genCodeDao.selectAllTableList();
    }

    @Override
    public String genCode() {
        String tableName = "mt_tab_info";
        TableInfo tableInfo = genCodeDao.selectTableByName(tableName);
        // 填充表信息
        fillTableInfo(tableInfo);

        List<ColumnInfo> columnList = genCodeDao.selectColumnListByTableName(tableName);
        for (ColumnInfo column : columnList) {
            // 填充列信息
            fillColumnInfo(column);
        }

        String result = "";
        try {
            // 加载模板文件
            Template template = configuration.getTemplate("Dao.java.str");
            // 设置模板变量
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("packageName", GenConstants.PACKAGE_NAME);
            dataMap.put("author", GenConstants.CODE_AUTHOR);
            dataMap.put("entity", tableInfo);
            dataMap.put("columnList", columnList);
            result = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
        } catch (Exception e) {
            log.error("生成代码失败", e);
        }
        return result;
    }

    /**
     * 填充表信息
     */
    private void fillTableInfo(TableInfo tableInfo) {
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableInfo.getTableName());
        tableInfo.setClassName(className);
    }

    /**
     * 填充字段信息
     */
    private void fillColumnInfo(ColumnInfo columnInfo) {
        columnInfo.setJavaName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, columnInfo.getColumnName()));
        columnInfo.setJavaType(GenConstants.JDBC_TYPE_MAP.getOrDefault(columnInfo.getJdbcType(), "String"));
    }
}
