package com.dodoyd.moyu.admin.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.dodoyd.moyu.admin.constant.GenConstants;
import com.dodoyd.moyu.admin.dao.GenCodeDao;
import com.dodoyd.moyu.admin.model.vo.ColumnInfo;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.enums.ExceptionEnum;
import com.dodoyd.moyu.common.exception.BaseException;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;

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
    public Map<String, String> genCodeByTable(String tableName) {
        Assert.hasText(tableName, "表名不能为空");
        TableInfo tableInfo = genCodeDao.selectTableByName(tableName);
        // 填充表信息
        fillTableInfo(tableInfo);

        List<ColumnInfo> columnList = genCodeDao.selectColumnListByTableName(tableName);
        for (ColumnInfo column : columnList) {
            // 填充列信息
            fillColumnInfo(column);
        }
        return genCode(tableInfo, columnList);
    }

    @Override
    public Map<String, String> genCodeBySql(String sql) {
        // 校验格式
        try {
            Assert.hasText(sql, "SQL不能为空");
            SQLUtils.parseStatements(sql, JdbcConstants.MYSQL, true);
        } catch (Exception e) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "SQL语句语法错误" + e.getMessage());
        }
        //  解析SQL语句，获取SQLStatement对象
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL, true);
        for (SQLStatement statement : statementList) {
            // 如果是CREATE TABLE语句，获取表名和字段信息
            if (statement instanceof MySqlCreateTableStatement) {
                MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement) statement;
                // 表信息
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(createTableStatement.getName().getSimpleName());
                tableInfo.setTableComment(createTableStatement.getComment().toString());
                //  补充表信息
                fillTableInfo(tableInfo);
                // 列信息
                List<ColumnInfo> columnList = new ArrayList<>();
                // 遍历表的元素列表，如果元素是SQLColumnDefinition类型，则获取列的名称、类型和注释。
                createTableStatement.getTableElementList().forEach(tableElement -> {
                    if (tableElement instanceof SQLColumnDefinition) {
                        SQLColumnDefinition columnDefinition = (SQLColumnDefinition) tableElement;
                        // 列信息
                        ColumnInfo columnInfo = new ColumnInfo();
                        columnInfo.setColumnName(columnDefinition.getName().getSimpleName());
                        columnInfo.setJdbcType(columnDefinition.getDataType().getName());
                        columnInfo.setComment(columnDefinition.getComment().toString());
                        // 填充列信息
                        fillColumnInfo(columnInfo);
                        columnList.add(columnInfo);
                    }
                });
                return genCode(tableInfo, columnList);
            }

        }

        return null;
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
        columnInfo.setJavaType(GenConstants.JDBC_TYPE_MAP.getOrDefault(columnInfo.getJdbcType().toUpperCase(), "String"));
    }

    /**
     * 通过组装好的结构按照模板生成代码
     */
    Map<String, String> genCode(TableInfo tableInfo, List<ColumnInfo> columnList) {
        // code代码map
        Map<String, String> codeMap = new LinkedHashMap<>();
        List<String> templateList = Lists.newArrayList("Domain.java", "Dao.java", "Service.java", "ServiceImpl.java");
        templateList.forEach(templateName -> {
            try {
                // 加载模板文件
                Template template = configuration.getTemplate(templateName + ".ftl");
                // 设置模板变量
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("packageName", GenConstants.PACKAGE_NAME);
                dataMap.put("author", GenConstants.CODE_AUTHOR);
                dataMap.put("entity", tableInfo);
                dataMap.put("columnList", columnList);
                String codeStr = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
                codeMap.put(templateName, codeStr);
            } catch (Exception e) {
                log.error("生成代码失败", e);
            }
        });
        return codeMap;
    }
}
