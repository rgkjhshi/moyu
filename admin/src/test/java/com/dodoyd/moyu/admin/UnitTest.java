package com.dodoyd.moyu.admin;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.util.JdbcConstants;
import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 单元测试基础类
 *
 * @author song.shi
 * @since 2018-01-17
 */
public class UnitTest {
    private static final Logger log = LoggerFactory.getLogger(UnitTest.class);

    @Test
    public void test() {
        log.info("测试基累正确执行，不要改");
    }

    @Test
    public void testCamelCase() {
        String underscoreName = "Hello_world_DTO";
        String camelCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreName);
        log.info(camelCaseName);
    }

    @Test
    public void testGson() {
        String path = "/pages/activity/index/index";
        String query = "activityNo=" + "Abc1234";

        // 将请求体转换为 JSON 字符串
        Map<String, String> objMap = new HashMap<>(4);
        objMap.put("path", path);
        objMap.put("query", query);
        Map<String, Object> bodyMap = new HashMap<>(4);
        bodyMap.put("jump_wxa", objMap);
        Gson htmlGson = new GsonBuilder().disableHtmlEscaping().create();
        String requestBody = htmlGson.toJson(bodyMap);
        log.info(requestBody);
    }

    @Test
    public void testSql() {
        String sql = "CREATE TABLE `mt_tab_info`\n" +
                "(\n" +
                "    `id`       bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',\n" +
                "    `tab_code` int(5)              NOT NULL DEFAULT '0' COMMENT '聚合tab值',\n" +
                "    `tab_name` varchar(32)         NOT NULL DEFAULT '' COMMENT '聚合tab名称',\n" +
                "    `tab_desc` varchar(64)         NOT NULL DEFAULT '' COMMENT '名称',\n" +
                "    `c_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "    `u_time`   datetime            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    UNIQUE KEY `uniq_tab_code` (`tab_code`)\n" +
                ") ENGINE = InnoDB COMMENT ='平台聚合方式信息元数据表';";

        try {
            SQLStatement statement = SQLUtils.parseSingleStatement(sql, JdbcConstants.MYSQL, true);
            System.out.println("SQL语句语法正确");
        } catch (ParserException e) {
            System.out.println("SQL语句语法错误：" + e.getMessage());
            return;
        }

        SQLStatement statement = SQLUtils.parseSingleStatement(sql, JdbcConstants.MYSQL, SQLParserFeature.KeepComments, SQLParserFeature.IgnoreNameQuotes);

        // 打印解析结果
        System.out.println("SQL Type: " + statement.getClass().getSimpleName());

        // 如果是CREATE TABLE语句，获取表名和字段信息
        if (statement instanceof MySqlCreateTableStatement) {
            MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement) statement;
            // 表名
            System.out.println("Table Name: " + createTableStatement.getName().getSimpleName());
            if (createTableStatement.getComment() != null) {
                System.out.println("Table Name: " + createTableStatement.getComment().toString());
            }

            // 获取字段信息
            // 遍历表的元素列表，如果元素是SQLColumnDefinition类型，则输出列的名称、类型和注释。
            createTableStatement.getTableElementList().forEach(tableElement -> {
                if (tableElement instanceof SQLColumnDefinition) {
                    SQLColumnDefinition columnDefinition = (SQLColumnDefinition) tableElement;
                    System.out.println("Column name: " + columnDefinition.getName().getSimpleName());
                    System.out.println("Column type: " + columnDefinition.getDataType().getName());
                    System.out.println("Column comment: " + columnDefinition.getComment().toString());
                }
            });
        }

    }
}
