package com.dodoyd.moyu.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author shisong02
 * @since 2023-12-19
 */
@Data
public class TableInfo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表描述
     */
    private String tableComment;

    /**
     * 表名对应的类名(首字母大写)
     */
    private String className;

    /**
     * 表创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
