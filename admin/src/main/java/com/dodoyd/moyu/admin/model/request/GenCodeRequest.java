package com.dodoyd.moyu.admin.model.request;

import com.dodoyd.moyu.common.model.PageRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shisong02
 * @since 2023-12-28
 */
@Getter
@Setter
@ToString
public class GenCodeRequest extends PageRequest {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 逗号分割的多个表名
     */
    private String tableNames;

    /**
     * SQL语句
     */
    private String sql;
}
