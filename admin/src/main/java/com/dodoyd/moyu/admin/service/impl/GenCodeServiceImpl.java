package com.dodoyd.moyu.admin.service.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.dodoyd.moyu.admin.constant.Constants;
import com.dodoyd.moyu.admin.constant.GenConstants;
import com.dodoyd.moyu.admin.dao.GenCodeDao;
import com.dodoyd.moyu.admin.model.request.GenCodeRequest;
import com.dodoyd.moyu.admin.model.vo.ColumnInfo;
import com.dodoyd.moyu.admin.model.vo.TableInfo;
import com.dodoyd.moyu.admin.service.GenCodeService;
import com.dodoyd.moyu.common.enums.ExceptionEnum;
import com.dodoyd.moyu.common.exception.BaseException;
import com.dodoyd.moyu.common.model.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
    public PageResult<TableInfo> queryAllTableList(GenCodeRequest request) {
        // 分页插件
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        Page<TableInfo> page = (Page<TableInfo>) genCodeDao.selectAllTableList();
        // 组装分页数据
        PageResult<TableInfo> pageResult = new PageResult<>();
        pageResult.setPageNum(page.getPageNum());
        pageResult.setTotal(page.getTotal());
        // 设置页内数据
        pageResult.setPageData(page);
        return pageResult;
    }

    @Override
    public Map<String, String> genCodeByTable(String tableName) {
        Assert.hasText(tableName, "表名不能为空");
        TableInfo tableInfo = genCodeDao.selectTableByName(tableName);
        // 表对应的类名
        tableInfo.setClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableInfo.getTableName()));

        List<ColumnInfo> columnList = genCodeDao.selectColumnListByTableName(tableName);
        for (ColumnInfo column : columnList) {
            // 填充列信息
            fillColumnInfo(column);
        }
        tableInfo.setColumnList(columnList);
        // 填充主键信息
        List<ColumnInfo> pkColumnList = genCodeDao.selectPkColumnListByTableName(tableName);
        // 只处理只有一个主键的情况，不支持联合主键
        if (pkColumnList.size() == 1) {
            ColumnInfo pkColumn = pkColumnList.get(0);
            // 填充列信息
            fillColumnInfo(pkColumn);
            tableInfo.setPkColumn(pkColumn);
        }
        return genCode(tableInfo);
    }

    @Override
    public Map<String, String> genCodeBySql(String sql) {
        // 只处理第一个建表语句
        Optional<MySqlCreateTableStatement> opt = Optional.empty();
        // 校验格式
        try {
            Assert.hasText(sql, "SQL不能为空");
            //  解析SQL语句，获取SQLStatement对象
            List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL, true);
            // 只处理第一个建表语句
            opt = statementList.stream().filter(e -> e instanceof MySqlCreateTableStatement).findFirst().map(e -> (MySqlCreateTableStatement) e);
        } catch (Exception e) {
            throw new BaseException(ExceptionEnum.INVALID_PARAMETER.getCode(), "SQL语句语法错误：" + e.getMessage());
        }
        // 存放生成代码的map
        Map<String, String> codeMap = new HashMap<>();
        if (!opt.isPresent()) {
            return codeMap;
        }
        // 表信息
        TableInfo tableInfo = parseCreateTable(opt.get());
        // 生成代码
        codeMap = genCode(tableInfo);
        return codeMap;
    }

    @Override
    public byte[] downloadCodeByTable(String tableNames) {
        Assert.hasText(tableNames, "表名不能为空");
        List<String> tableNameList = Constants.COMMA_SPLITTER.splitToList(tableNames);
        Assert.isTrue(tableNameList.size() <= 10, "下载内容过多，每次生成不能超过10个");
        // 创建一个字节输出流来存储ZIP文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNameList) {
            Map<String, String> codeMap = genCodeByTable(tableName);
            zipCode(tableName, codeMap, zip);
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
        Assert.isTrue(!createStatementList.isEmpty() && createStatementList.size() <= 10, "每次只能处理1~10个建表语句");
        // 创建一个字节输出流来存储ZIP文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        // 遍历建表语句生成代码并添加到zip
        for (MySqlCreateTableStatement createTableStatement : createStatementList) {
            // 表信息
            TableInfo tableInfo = parseCreateTable(createTableStatement);
            // 存放生成代码的map
            Map<String, String> codeMap = genCode(tableInfo);
            // 第一个处理完后就返回,只能预览一个表的生成代码
            zipCode(tableInfo.getTableName(), codeMap, zip);
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
     * 解析创建表语句
     */
    private TableInfo parseCreateTable(MySqlCreateTableStatement createTableStatement) {
        // 表信息
        TableInfo tableInfo = new TableInfo();
        // 表名
        tableInfo.setTableName(removeQuotes(createTableStatement.getName().getSimpleName()));
        // 表注释
        if (createTableStatement.getComment() != null) {
            tableInfo.setTableComment(removeQuotes(createTableStatement.getComment().toString()));
        }
        // 表对应的类名
        tableInfo.setClassName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableInfo.getTableName()));
        // 列信息
        tableInfo.setColumnList(getColumnList(createTableStatement));
        tableInfo.setPkColumn(getPkColumn(createTableStatement));
        return tableInfo;
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
     * 通过表创建语句获取主键列信息(只处理主键为单列的情况)
     */
    private ColumnInfo getPkColumn(SQLCreateTableStatement createTableStatement) {
        // 列信息
        ColumnInfo pkColumnInfo = null;
        // 遍历建表语句的元素列表，判断是否为主键列
        for (SQLTableElement tableElement : createTableStatement.getTableElementList()) {
            // 判断是否为唯一键(一个表只有一个唯一键)
            if (tableElement instanceof SQLPrimaryKey) {
                SQLPrimaryKey sqlPrimaryKey = (SQLPrimaryKey) tableElement;
                // 获取构成唯一键的所有列信息
                List<SQLSelectOrderByItem> columns = sqlPrimaryKey.getColumns();
                // 只处理只有一个主键的情况，不支持联合主键
                if (columns.size() == 1) {
                    // 组成主键的列可以有多个，但通常只会有一个列为主键
                    SQLColumnDefinition pkColumn = createTableStatement.findColumn(columns.get(0).getExpr().toString());
                    if (pkColumn != null) {
                        // 列信息
                        pkColumnInfo = new ColumnInfo();
                        // 列名称
                        pkColumnInfo.setColumnName(removeQuotes(pkColumn.getName().getSimpleName()));
                        // jdbc类型
                        pkColumnInfo.setJdbcType(pkColumn.getDataType().getName());
                        // 填充java类型和名称
                        fillColumnInfo(pkColumnInfo);
                    }
                }
            }
        }
        return pkColumnInfo;
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
    Map<String, String> genCode(TableInfo tableInfo) {
        // code代码map
        Map<String, String> codeMap = new LinkedHashMap<>();
        List<String> templateList = Lists.newArrayList("java/Domain.java", "java/Dao.java", "java/Request.java",
                "java/Controller.java", "java/Service.java", "java/ServiceImpl.java", "js/api.js", "vue/index.vue");
        templateList.forEach(templateName -> {
            try {
                // 加载模板文件
                Template template = configuration.getTemplate(templateName + ".ftl");
                // 设置模板变量
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("packageName", GenConstants.PACKAGE_NAME);
                dataMap.put("author", GenConstants.CODE_AUTHOR);
                dataMap.put("entity", tableInfo);
                dataMap.put("columnList", tableInfo.getColumnList());
                String codeStr = FreeMarkerTemplateUtils.processTemplateIntoString(template, dataMap);
                codeMap.put(getSimpleName(templateName), codeStr);
            } catch (Exception e) {
                log.error("生成代码失败", e);
            }
        });
        return codeMap;
    }

    /**
     * 生成代码
     */
    private void zipCode(String tableName, Map<String, String> codeMap, ZipOutputStream zip) {
        if (CollectionUtils.isEmpty(codeMap)) {
            return;
        }
        for (Map.Entry<String, String> entry : codeMap.entrySet()) {
            String fileName = getFileName(entry.getKey(), tableName);
            try {
                // 添加文件到ZIP文件
                ZipEntry zipEntry = new ZipEntry(fileName);
                zip.putNextEntry(zipEntry);
                String code = entry.getValue();
                zip.write(code.getBytes(StandardCharsets.UTF_8));
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("生成文件失败，表名：" + tableName, e);
            }
        }
    }

    /**
     * 去掉引号和反引号
     */
    private String removeQuotes(String str) {
//        return SQLUtils.normalize(str);
        if (str != null) {
            str = str.replace("`", "").replace("'", "").replace("\"", "");
        }
        return str;
    }

    /**
     * 获取文件名
     */
    private String getFileName(String template, String tableName) {
        // 大写类名
        String className = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, tableName);
        // 小写类名
        String javaName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableName);
        // 文件名称
        String fileName = className;
        if (template.equalsIgnoreCase("Domain.java")) {
            fileName = String.format("domain/%s.java", className);
        } else if (template.equalsIgnoreCase("Dao.java")) {
            fileName = String.format("dao/%sDao.java", className);
        } else if (template.equalsIgnoreCase("Request.java")) {
            fileName = String.format("model/request/%sRequest.java", className);
        } else if (template.equalsIgnoreCase("Controller.java")) {
            fileName = String.format("controller/%sController.java", className);
        } else if (template.equalsIgnoreCase("Service.java")) {
            fileName = String.format("service/%sService.java", className);
        } else if (template.equalsIgnoreCase("ServiceImpl.java")) {
            fileName = String.format("service/impl/%sServiceImpl.java", className);
        } else if (template.equalsIgnoreCase("api.js")) {
            fileName = String.format("api/%s.js", javaName);
        } else if (template.equalsIgnoreCase("index.vue")) {
            fileName = String.format("views/%s/index.vue", javaName);
        }
        return fileName;
    }

    /**
     * 获取模板名，不包含路径，如api.js、Dao.java
     */
    private String getSimpleName(String templateName) {
        // 模板名
        String simpleName = templateName.replace(".ftl", "");
        simpleName = simpleName.substring(simpleName.lastIndexOf("/") + 1);
        return simpleName;
    }

}
