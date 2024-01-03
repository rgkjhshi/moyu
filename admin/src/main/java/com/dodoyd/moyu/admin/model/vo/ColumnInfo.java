package com.dodoyd.moyu.admin.model.vo;

import lombok.Data;

/**
 * 数据库表的列信息
 *
 * @author shisong02
 * @since 2023-12-20
 */
@Data
public class ColumnInfo {

    /**
     * 数据库中的列名称
     */
    private String columnName;

    /**
     * 数据库中的列类型
     */
    private String jdbcType;

    /**
     * 列描述
     */
    private String comment;

    /**
     * java的属性名
     */
    private String javaName;

    /**
     * java属性类型
     */
    private String javaType;

}
