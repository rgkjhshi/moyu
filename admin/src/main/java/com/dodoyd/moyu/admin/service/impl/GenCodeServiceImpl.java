package com.dodoyd.moyu.admin.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.dodoyd.moyu.admin.constant.Constants;
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
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "SQL语句语法错误：" + e.getMessage());
        }
        // 存放生成代码的map
        Map<String, String> codeMap = new HashMap<>();
        //  解析SQL语句，获取SQLStatement对象
        List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL, true);
        for (SQLStatement statement : statementList) {
            // 如果是CREATE TABLE语句，获取表名和字段信息
            if (statement instanceof MySqlCreateTableStatement) {
                MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement) statement;
                // 表信息
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(removeQuotes(createTableStatement.getName().getSimpleName()));
                if (createTableStatement.getComment() != null) {
                    tableInfo.setTableComment(removeQuotes(createTableStatement.getComment().toString()));
                }
                // 补充表信息
                fillTableInfo(tableInfo);
                // 列信息
                List<ColumnInfo> columnList = getColumnList(createTableStatement);
                // 生成代码
                codeMap = genCode(tableInfo, columnList);
                // 第一个处理完后就返回,只能预览一个表的生成代码
                break;
            }
        }
        return codeMap;
    }

    @Override
    public byte[] downloadCodeByTable(String tableNames) {
        Assert.hasText(tableNames, "表名不能为空");
        List<String> tableNameList = Constants.COMMA_SPLITTER.splitToList(tableNames);
        Assert.isTrue(tableNameList.size() <= 10, "下载内容过多，单次下载不能超过10个");
        // 创建一个字节输出流来存储ZIP文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : Constants.COMMA_SPLITTER.split(tableNames)) {
            Map<String, String> codeMap = genCodeByTable(tableName);
            genCode(tableName, codeMap, zip);
        }
        try {
            // 完成所有文件的添加
            zip.finish();
            zip.close();
        } catch (IOException e) {
            log.error("生成文件失败", e);
        }
        return outputStream.toByteArray();
    }

    @Override
    public byte[] downloadCodeBySql(String sql) {
        List<SQLStatement> statementList = new ArrayList<>();
        // 校验格式
        try {
            Assert.hasText(sql, "SQL不能为空");
            statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL, true);
        } catch (Exception e) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "SQL语句语法错误：" + e.getMessage());
        }
        List<MySqlCreateTableStatement> createStatementList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(statementList)) {
            createStatementList = statementList.stream().filter(statement -> statement instanceof MySqlCreateTableStatement).map(e -> (MySqlCreateTableStatement) e).collect(Collectors.toList());
        }
        Assert.isTrue(createStatementList.size() > 0 && createStatementList.size() <= 10, "只能处理1~10个建表语句");
        // 创建一个字节输出流来存储ZIP文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 遍历建表语句生成代码并添加到zip
        for (MySqlCreateTableStatement createTableStatement : createStatementList) {
            // 表信息
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableName(removeQuotes(createTableStatement.getName().getSimpleName()));
            if (createTableStatement.getComment() != null) {
                tableInfo.setTableComment(removeQuotes(createTableStatement.getComment().toString()));
            }
            //  补充表信息
            fillTableInfo(tableInfo);
            // 列信息
            List<ColumnInfo> columnList = getColumnList(createTableStatement);
            // 存放生成代码的map
            Map<String, String> codeMap = genCode(tableInfo, columnList);
            // 第一个处理完后就返回,只能预览一个表的生成代码
            genCode(tableInfo.getTableName(), codeMap, zip);
        }
        try {
            // 完成所有文件的添加
            zip.finish();
            zip.close();
        } catch (IOException e) {
            log.error("生成文件失败", e);
        }
        return outputStream.toByteArray();
    }

    /**
     * 通过表创建语句获取列信息列表
     */
    private List<ColumnInfo> getColumnList(MySqlCreateTableStatement createTableStatement) {
        // 列信息
        List<ColumnInfo> columnList = new ArrayList<>();
        // 遍历表的元素列表，如果元素是SQLColumnDefinition类型，则获取列的名称、类型和注释。
        createTableStatement.getTableElementList().forEach(tableElement -> {
            if (tableElement instanceof SQLColumnDefinition) {
                SQLColumnDefinition columnDefinition = (SQLColumnDefinition) tableElement;
                // 列信息
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(removeQuotes(columnDefinition.getName().getSimpleName()));
                columnInfo.setJdbcType(columnDefinition.getDataType().getName());
                if (columnDefinition.getComment() != null) {
                    columnInfo.setComment(removeQuotes(columnDefinition.getComment().toString()));
                }
                // 填充列信息
                fillColumnInfo(columnInfo);
                columnList.add(columnInfo);
            }
        });
        return columnList;
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

    /**
     * 生成代码
     */
    private void genCode(String tableName, Map<String, String> codeMap, ZipOutputStream zip) {
        if (CollectionUtils.isEmpty(codeMap)) {
            return;
        }
        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            String fileName = getFileName(entry.getKey(), tableName);
            try {
                // 添加文件到ZIP文件
                ZipEntry zipEntry = new ZipEntry(fileName);
                zip.putNextEntry(zipEntry);
                String code = codeMap.get("Domain.java");
                zip.write(code.getBytes(StandardCharsets.UTF_8));
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("生成文件失败，表名：" + tableName, e);
            }
        }
    }

    /**
     * 去掉反引号和单引号
     */
    private String removeQuotes(String str) {
        if (str != null) {
            str = str.replace("`", "").replace("'", "");
        }
        return str;
    }

    /**
     * 获取文件名
     */
    private String getFileName(String template, String tableName) {
        // 大写类名
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        // 文件名称
        String fileName = className;
        if (template.equalsIgnoreCase("Domain.java")) {
            fileName = String.format("domain/%s.java", className);
        } else if (template.equalsIgnoreCase("Dao.java")) {
            fileName = String.format("dao/%sDao.java", className);
        } else if (template.equalsIgnoreCase("Service.java")) {
            fileName = String.format("service/%sService.java", className);
        } else if (template.equalsIgnoreCase("ServiceImpl.java")) {
            fileName = String.format("service/impl/%sServiceImpl.java", className);
        } else if (template.equalsIgnoreCase("Controller.java")) {
            fileName = String.format("controller/%sController.java", className);
        }
        return fileName;
    }

}
