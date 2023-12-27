package com.dodoyd.moyu.admin.constant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成通用常量
 *
 * @author shisong02
 * @since 2023-12-21
 */
public class GenConstants {
    /**
     * 作者
     */
    public static final String CODE_AUTHOR = "moyusi";
    /**
     * 表前缀（生成类名不会包含表前缀，多个用逗号分隔）
     */
    public static final String PACKAGE_NAME = "com.dodoyd.moyu.admin";

    /**
     * 用于存储jdbcType与javaType的映射关系的常量Map
     */
    public static final Map<String, String> JDBC_TYPE_MAP;

    static {
        Map<String, String> map = new HashMap<>();
        // 数字
        map.put("TINYINT", "Integer");
        map.put("SMALLINT", "Integer");
        map.put("MEDIUMINT", "Integer");
        map.put("INT", "Integer");
        map.put("INTEGER", "Integer");
        map.put("NUMBER", "Integer");
        map.put("BIGINT", "Long");
        map.put("FLOAT", "Float");
        map.put("DOUBLE", "Double");
        map.put("DECIMAL", "BigDecimal");
        // 字符
        map.put("CHAR", "String");
        map.put("VARCHAR", "String");
        map.put("TINYTEXT", "String");
        map.put("MEDIUMTEXT", "String");
        map.put("TEXT", "String");
        map.put("LONGTEXT", "String");
        map.put("BINARY", "String");
        map.put("VARBINARY", "String");
        map.put("BLOB", "String");
        // 日期时间
        map.put("DATE", "Date");
        map.put("TIME", "Date");
        map.put("DATETIME", "Date");
        map.put("TIMESTAMP", "Date");
        // 其他
        map.put("BIT", "Integer");
        map.put("BOOL", "Boolean");

        // 添加其他jdbcType与javaType的映射关系
        JDBC_TYPE_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * 数据库数字类型
     */
    public static final String[] JDBC_TYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer", "bigint", "float", "double", "decimal"};
    /**
     * 数据库字符串类型
     */
    public static final String[] JDBC_TYPE_STR = {"char", "varchar", "nvarchar", "longvarchar"};

    /**
     * 数据库文本类型
     */
    public static final String[] JDBC_TYPE_TEXT = {"tinytext", "text", "mediumtext", "longtext"};

    /**
     * 数据库时间类型
     */
    public static final String[] JDBC_TYPE_TIME = {"date", "time", "datetime", "timestamp"};

    /**
     * 数据库其他类型
     */
    public static final String[] JDBC_TYPE_BIT = {"bit", "bool"};

    /**
     * 字符串类型
     */
    public static final String JAVA_TYPE_STRING = "String";

    /**
     * 整型
     */
    public static final String JAVA_TYPE_INTEGER = "Integer";

    /**
     * 长整型
     */
    public static final String JAVA_TYPE_LONG = "Long";

    /**
     * 浮点型
     */
    public static final String JAVA_TYPE_DOUBLE = "Double";

    /**
     * 高精度计算类型
     */
    public static final String JAVA_TYPE_BIGDECIMAL = "BigDecimal";

    /**
     * 时间类型
     */
    public static final String JAVA_TYPE_DATE = "Date";

}
